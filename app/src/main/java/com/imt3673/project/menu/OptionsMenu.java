package com.imt3673.project.menu;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.CheckBox;
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
        int progress = settings.getInt(Constants.PREFERENCE_VOLUME_SLIDER, 1);
        volumeBar.setProgress(progress);

        ((CheckBox)findViewById(R.id.OptionsMenu_vibration_box)).setChecked(settings.getBoolean(Constants.PREFERENCE_VIBRATE,true));
        ((CheckBox)findViewById(R.id.OptionsMenu_gpScore_box)).setChecked(settings.getBoolean(Constants.PREFERENCE_GP_SCORE,true));
    }

    /**
     * Initialize Save button listener
     * Saves current volume change to SharedPreferences
     */
    private void initSaveButton() {
        (findViewById(R.id.OptionMenu_save_btn)).setOnClickListener(v -> {
            SharedPreferences settings = getSharedPreferences(Constants.PREFERENCE_FILE, 0);
            SharedPreferences.Editor editor = settings.edit();
            editor.putInt(Constants.PREFERENCE_VOLUME_SLIDER, (((SeekBar)findViewById(R.id.OptionMenu_volume_control)).getProgress()));
            editor.putBoolean(Constants.PREFERENCE_VIBRATE, ((CheckBox)findViewById(R.id.OptionsMenu_vibration_box)).isChecked());
            editor.putBoolean(Constants.PREFERENCE_GP_SCORE, ((CheckBox)findViewById(R.id.OptionsMenu_gpScore_box)).isChecked());
            editor.apply();
            finish();
        });
    }
}
