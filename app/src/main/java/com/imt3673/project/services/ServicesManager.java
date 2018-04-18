package com.imt3673.project.services;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
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
import com.imt3673.project.main.MainActivity;
import com.imt3673.project.main.R;

/**
 * Services Manager - Manages the various services the app uses.
 */
public class ServicesManager {

    private final Context               context;
    private GoogleSignInAccount         googleAccount;
    private final GoogleApiAvailability googleApiAvailability;
    private GoogleSignInClient          googleClient;
    private LeaderboardsClient          leaderboardsClient;
    private String                      leaderboardID;
    private Player                      player;

    /**
     * Services Manager - default constructor
     * @param context Context from calling activity
     */
    public ServicesManager(final Context context) {
        this.context               = context;
        this.googleApiAvailability = GoogleApiAvailability.getInstance();
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
     * Validates and initializes all the required services.
     */
    public void init() {
        // Try to re-use existing user account if possible before authenticating the user.
        this.googleAccount = GoogleSignIn.getLastSignedInAccount(this.context);

        if ((this.googleAccount != null) && this.isGoogleApiAvailable())
            this.updatePlayer();
        else
            this.authenticate();
    }

    /**
     * TODO: Set view after creating a view (if we decide to use custom popup views)
     * Assigns the specified resource view to be used for popups like achievements etc.
     * @param resourceID Resource ID of the view
     */
    public void setPopupView(final int resourceID) {
        GamesClient gamesClient = Games.getGamesClient(this.context, this.googleAccount);
        gamesClient.setViewForPopups(((MainActivity)this.context).findViewById(resourceID));

        // Align the popup relative to the screen
        gamesClient.setGravityForPopups(Gravity.TOP | Gravity.CENTER_HORIZONTAL);
    }

    /**
     * TODO: Link to menu/options
     * Signs the user out of the Google Play Games Services.
     */
    public void signOut() {
        this.googleClient.signOut().addOnCompleteListener(
            (Task<Void> task) -> {
                // TODO: Perform any necessary cleanup after the user signed out
            }
        );
    }

    /**
     * TODO: Link to menu/options
     * Displays the leaderboard UI.
     */
    public void showLeaderboard() {
        this.leaderboardsClient.getLeaderboardIntent(this.leaderboardID)
            .addOnSuccessListener(
                (Intent intent) -> ((MainActivity)context).startActivityForResult(
                    intent, Constants.LEADERBOARD_UI
                )
            );
    }

    /**
     * TODO: Call when the game ends to update the score on the leaderboard
     * Updates the current leaderboard with the specified score for the current user.
     * @param score User score
     */
    public void updateLeaderboard(final long score) {
        this.leaderboardsClient.submitScore(this.leaderboardID, score);
    }

    /**
     * Tries to authenticate the user silently first if they have already signed in,
     * otherwise they get an explicit UI to install the necessary services and pick a user.
     */
    private void authenticate() {
        this.googleClient = GoogleSignIn.getClient(this.context, GoogleSignInOptions.DEFAULT_GAMES_SIGN_IN);

        this.googleClient.silentSignIn().addOnCompleteListener((Task<GoogleSignInAccount> task) -> {
            // User is authenticated
            if (task.isSuccessful() && isGoogleApiAvailable()) {
                googleAccount = task.getResult();
                updatePlayer();
            // Open Google Sign-in (intent result will be handled in the main activity)
            } else {
                ((MainActivity)context).startActivityForResult(googleClient.getSignInIntent(), Constants.GOOGLE_SIGNIN_RESULT);
            }
        });
    }

    /**
     * Gets the result from the sign in intent (user account, player details etc.).
     * @param data Intent data
     */
    public void authenticateHandleIntent(final int resultCode, final Intent data) {
        GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);

        if (result.isSuccess())
            this.googleAccount = result.getSignInAccount();

        if ((this.googleAccount != null) && this.isGoogleApiAvailable())
            this.updatePlayer();
        // TODO: Handle user cancel, may not be needed if user must manually sign in/out in the options.
        // Find a way to separate user cancel from auth fail.
        // resultCode is -1 on success, and 0 on cancel, but also when it fails.
        else
            Log.d("ServicesManager", "authenticateHandleIntent: Failed to authenticate.");
    }

    /**
     * Checks if the necessary Google API services are available.
     */
    private boolean isGoogleApiAvailable() {
        int result = this.googleApiAvailability.isGooglePlayServicesAvailable(this.context);

        // Tell the user that the service is not available if check failed
        if ((result != ConnectionResult.SUCCESS) || !GoogleSignIn.hasPermissions(this.googleAccount, Games.SCOPE_GAMES_LITE)) {
            Dialog errorDialog = this.googleApiAvailability.getErrorDialog(
                (MainActivity)this.context, result, 0, (dialog) -> dialog.dismiss()
            );

            if (errorDialog != null)
                errorDialog.show();

            return false;
        }

        return true;
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
     * Sets the leaderboard for the current user account.
     */
    private void setLeaderboard() {
        this.leaderboardID      = this.context.getString(R.string.leaderboard_id);
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

                Log.d("ServicesManager", "setPlayer: Authentication succeeded.");
            }
        });
    }

    /**
     * Sets the player and leaderboard based on the the authenticated google account.
     */
    private void updatePlayer() {
        this.setLeaderboard();
        this.setPlayer();
    }

}
