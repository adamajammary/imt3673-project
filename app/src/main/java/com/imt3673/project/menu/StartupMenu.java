package com.imt3673.project.menu;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.imt3673.project.main.MainActivity;
import com.imt3673.project.main.R;

/**
 * class StartupMenu
 * Runs at Startup giving user, options to play,
 * edit options or exit the application.
 */
public class StartupMenu extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_startup_menu);

        //Hide actionbar
        if(getSupportActionBar() != null)
            getSupportActionBar().hide();


        initButtons();


    }


    /**
     * Initialize button listeners
     */
    private void initButtons() {
        //Play button
        (findViewById(R.id.StartupMenu_play_btn)).setOnClickListener(v -> {
            startActivity( new Intent(this, MainActivity.class));
        });

        //Options button
        (findViewById(R.id.StartupMenu_option_btn)).setOnClickListener(v -> {
            startActivity(new Intent(this, OptionsMenu.class));
        });

        //Exit button
        (findViewById(R.id.StartupMenu_exit_btn)).setOnClickListener(v -> {
            finishAffinity();
        });
    }



}
