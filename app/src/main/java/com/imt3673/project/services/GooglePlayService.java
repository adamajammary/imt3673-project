package com.imt3673.project.services;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.Gravity;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.games.Games;
import com.google.android.gms.games.GamesClient;
import com.google.android.gms.games.LeaderboardsClient;
import com.google.android.gms.games.Player;
import com.google.android.gms.games.PlayersClient;
import com.google.android.gms.tasks.Task;
import com.imt3673.project.main.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;

/**
 * Google Play Service - Manages the Google Play Services including authentication, leaderboards etc.
 */
public class GooglePlayService {

    private final Context               context;
    private GoogleSignInAccount         googleAccount;
    private final GoogleApiAvailability googleApiAvailability;
    private LeaderboardsClient          leaderboardsClient;
    private Map<String, String>         leaderboards = new HashMap<>();
    private Player                      player;

    /**
     * Google Play Service - default constructor
     * @param context Context from calling activity
     */
    public GooglePlayService(final Context context) {
        this.context               = context;
        this.googleApiAvailability = GoogleApiAvailability.getInstance();
    }

    /**
     * Gets the result from the sign in intent (user account, player details etc.).
     * @param data Intent data
     */
    public void authenticateHandleIntent(final int resultCode, final Intent data) {
        GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);

        if (result.isSuccess())
            this.googleAccount = result.getSignInAccount();

        if (!this.isSignedIn())
            Log.w("GooglePlayService", context.getString(R.string.error_authentication));
    }

    /**
     * @return The player ID on Google Play Games
     */
    public String getPlayerID() {
        return this.player.getPlayerId();
    }

    /**
     * @return The player name on Google Play Games
     */
    public String getPlayerName() {
        return this.player.getDisplayName();
    }

    /**
     * Checks if the specified package is installed on the device.
     * @param packageName Package name, ex: 'com.google.android.play.games'
     * @return True if the package is installed, False otherwise
     */
    public boolean isPackageInstalled(final String packageName) {
        boolean installed;

        try {
            this.context.getPackageManager().getPackageInfo(packageName, 0);
            installed = true;
        } catch (PackageManager.NameNotFoundException e) {
            installed = false;
        }

        return installed;
    }

    /**
     * @return True if the user is already signed in, False otherwise
     */
    public boolean isSignedIn() {
        boolean signedIn;

        // Try to re-use existing user account if possible before authenticating the user.
        if (this.googleAccount == null)
            this.googleAccount = GoogleSignIn.getLastSignedInAccount(this.context);

        signedIn = ((this.googleAccount != null) && this.isGoogleApiAvailable());

        if (signedIn)
            this.updatePlayer();

        return signedIn;
    }

    /**
     * Assigns the specified resource view to be used for popups like achievements etc.
     * @param resourceID Resource ID of the view
     */
    public void setPopupView(final int resourceID) {
        GamesClient gamesClient = Games.getGamesClient(this.context, this.googleAccount);
        gamesClient.setViewForPopups(((Activity)this.context).findViewById(resourceID));

        // Align the popup relative to the screen
        gamesClient.setGravityForPopups(Gravity.TOP | Gravity.CENTER_HORIZONTAL);
    }

    /**
     * Displays the leaderboard UI.
     */
    public void showLeaderboard() {
        ArrayList<String> levels = new ArrayList<>(this.leaderboards.keySet());
        Collections.sort(levels, (String a, String b) -> a.compareTo(b));

        this.selectLevel(levels);
    }

    /**
     * Tries to sign the user in to Google Play Games Services.
     */
    public void signIn() {
        if (!this.isSignedIn())
            this.authenticate();
    }

    /**
     * Signs the user out of the Google Play Games Services.
     */
    public void signOut(Callable<Void> cleanupFunction) {
        GoogleSignInClient googleClient = GoogleSignIn.getClient(this.context, GoogleSignInOptions.DEFAULT_GAMES_SIGN_IN);
        googleClient.signOut().addOnCompleteListener((Task<Void> task) -> cleanup(cleanupFunction));
    }

    /**
     * Updates the current leaderboard with the specified time for the current user.
     * https://developers.google.com/android/reference/com/google/android/gms/games/LeaderboardsClient
     * @param level Level Name (ID)
     * @param time Time (in milliseconds) used to complete the level
     */
    public void updateLeaderboard(final String level, final long time) {
        this.leaderboardsClient.submitScore(this.leaderboards.get(level), time);
    }

    /**
     * Tries to authenticate the user silently first if they have already signed in,
     * otherwise they get an explicit UI to install the necessary services and pick a user.
     */
    private void authenticate() {
        GoogleSignInClient googleClient = GoogleSignIn.getClient(this.context, GoogleSignInOptions.DEFAULT_GAMES_SIGN_IN);

        googleClient.silentSignIn().addOnCompleteListener((Task<GoogleSignInAccount> task) -> {
            // User is authenticated
            if (task.isSuccessful() && isGoogleApiAvailable()) {
                googleAccount = task.getResult();

                if (!this.isSignedIn())
                    Log.w("GooglePlayService", context.getString(R.string.error_authentication));

                updatePlayer();
            // Open Google Sign-in (intent result will be handled in the calling activity)
            } else {
                ((Activity)context).startActivityForResult(googleClient.getSignInIntent(), Constants.GOOGLE_SIGNIN_RESULT);
            }
        });
    }

    /**
     * Cleans up by resetting resource variables.
     */
    private void cleanup(Callable<Void> cleanupFunction) {
        this.googleAccount      = null;
        this.leaderboardsClient = null;
        this.player             = null;

        this.leaderboards.clear();

        try {
            cleanupFunction.call();
        } catch (Exception e) {
            Log.w("GooglePlayService", e);
        }
    }

    /**
     * Checks if the necessary Google API services are available.
     */
    private boolean isGoogleApiAvailable() {
        int result = this.googleApiAvailability.isGooglePlayServicesAvailable(this.context);

        // Tell the user that the service is not available if check failed
        if ((result != ConnectionResult.SUCCESS) || !GoogleSignIn.hasPermissions(this.googleAccount, Games.SCOPE_GAMES_LITE)) {
            Dialog errorDialog = this.googleApiAvailability.getErrorDialog(
                (Activity)this.context, result, 0, (dialog) -> dialog.dismiss()
            );

            if (errorDialog != null)
                errorDialog.show();

            return false;
        }

        return true;
    }

    /**
     * Displays a list of levels the user can select from.
     * @param levels List of levels
     */
    private void selectLevel(final ArrayList<String> levels) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this.context);

        dialog.setTitle(R.string.level_chooser_title);
        dialog.setIcon(R.mipmap.ic_launcher);

        dialog.setItems(
            levels.toArray(new CharSequence[levels.size()]),
            (DialogInterface d, int i) -> {
                d.dismiss();
                showLeaderboard(levels.get(i));
            }
        );

        dialog.show();
    }

    /**
     * TODO: Uncomment new levels added (levels list is only available in the LevelChooser activity)
     * Sets the leaderboard for the current user account.
     */
    private void setLeaderboard() {
        this.leaderboards.put("level1", this.context.getString(R.string.leaderboard_level1));
        this.leaderboards.put("level2", this.context.getString(R.string.leaderboard_level2));
        //this.leaderboards.put("level3", this.context.getString(R.string.leaderboard_level3));
        //this.leaderboards.put("level4", this.context.getString(R.string.leaderboard_level4));
        //this.leaderboards.put("level5", this.context.getString(R.string.leaderboard_level5));

        this.leaderboardsClient = Games.getLeaderboardsClient(this.context, this.googleAccount);
    }

    /**
     * Sets the current player object from the authenticated google account.
     */
    private void setPlayer() {
        // Link the players client to the google account
        PlayersClient client = Games.getPlayersClient(this.context, this.googleAccount);

        // Set the player
        client.getCurrentPlayer().addOnCompleteListener((Task<Player> task) -> {
            if (task.isSuccessful()) {
                player = task.getResult();
            }
        });
    }

    /**
     * Displays the leaderboard UI for the specified level.
     */
    private void showLeaderboard(final String level) {
        this.leaderboardsClient.getLeaderboardIntent(this.leaderboards.get(level))
            .addOnSuccessListener((Intent intent) -> ((Activity)context).startActivityForResult(
                intent, Constants.LEADERBOARD_UI
            ));
    }

    /**
     * Sets the player and leaderboard based on the the authenticated google account.
     */
    private void updatePlayer() {
        this.setLeaderboard();
        this.setPlayer();
    }

}
