package com.imt3673.project.menu;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.imt3673.project.main.R;
import com.imt3673.project.services.GooglePlayService;

import static com.imt3673.project.services.Constants.GOOGLE_SIGNIN_RESULT;

/**
 * class StartupMenu
 * Runs at Startup giving user, options to play,
 * edit options or exit the application.
 */
public class StartupMenu extends AppCompatActivity {

    private GooglePlayService googlePlayService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_startup_menu);

        //Hide actionbar
        if (getSupportActionBar() != null)
            getSupportActionBar().hide();

        this.googlePlayService = new GooglePlayService(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        this.initButtons();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Sign-In to the Google Play Games Services
        if (requestCode == GOOGLE_SIGNIN_RESULT)
            this.googlePlayService.authenticateHandleIntent(resultCode, data);
    }

    /**
     * Initialize button listeners
     */
    private void initButtons() {
        //Play button
        (findViewById(R.id.StartupMenu_play_btn)).setOnClickListener(v -> startActivity( new Intent(this, LevelChooser.class)));

        //Options button
        (findViewById(R.id.StartupMenu_option_btn)).setOnClickListener(v -> startActivity(new Intent(this, OptionsMenu.class)));

        // Google Play button
        this.updateGooglePlayButton();

        //Exit button
        (findViewById(R.id.StartupMenu_exit_btn)).setOnClickListener(v -> finishAffinity());
    }

    /**
     * Updates the button label and functionality based on Google Play authentication status.
     */
    public Void updateGooglePlayButton() {
        Button googlePlayBtn  = findViewById(R.id.StartupMenu_googlePlay_btn);
        Button leaderboardBtn = findViewById(R.id.StartupMenu_googlePlayLeaderboard_btn);

        if (!this.googlePlayService.isSignedIn()) {
            googlePlayBtn.setText(R.string.menu_signIn_button);
            googlePlayBtn.setOnClickListener((View v) -> googlePlayService.signIn());

            leaderboardBtn.setEnabled(false);
        } else {
            googlePlayBtn.setText(R.string.menu_signOut_button);
            googlePlayBtn.setOnClickListener((View v) -> googlePlayService.signOut(() -> updateGooglePlayButton()));

            leaderboardBtn.setEnabled(true);
            leaderboardBtn.setOnClickListener((View v) -> googlePlayService.showLeaderboard());
        }

        return null;
    }

}
