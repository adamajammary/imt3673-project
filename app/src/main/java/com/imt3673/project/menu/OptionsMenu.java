package com.imt3673.project.menu;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.SeekBar;

import com.imt3673.project.graphics.Constants;
import com.imt3673.project.main.R;

/**
 * Options controls volume, vibration etc
 */
public class OptionsMenu extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_options_menu);

        //Hide actionbar
        if(getSupportActionBar() != null)
            getSupportActionBar().hide();

        loadUserPreferences();
        initSaveButton();
    }

    /**
     * Load saved user preferences and set values
     */
    private void loadUserPreferences() {
        SharedPreferences settings = getSharedPreferences(Constants.PREFERENCE_FILE, 0);
        SeekBar volumeBar = findViewById(R.id.OptionMenu_volume_control);
        volumeBar.setProgress(settings.getInt(Constants.PREFERENCE_VOLUME_SLIDER,0));
    }

    /**
     * Initialize Save button listener
     * Saves current volume change to SharedPreferences
     */
    private void initSaveButton() {
        (findViewById(R.id.OptionMenu_save_btn)).setOnClickListener(v -> {
            SharedPreferences settings = getSharedPreferences(Constants.PREFERENCE_FILE, 0);
            SharedPreferences.Editor editor = settings.edit();
            editor.putInt(Constants.PREFERENCE_VOLUME_SLIDER, ((SeekBar)findViewById(R.id.OptionMenu_volume_control)).getProgress());
            editor.apply();
            finish();
        });
    }
}
