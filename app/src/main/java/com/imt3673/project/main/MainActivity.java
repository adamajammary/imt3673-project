package com.imt3673.project.main;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.imt3673.project.media.Constants;
import com.imt3673.project.media.MediaManager;
import com.imt3673.project.graphics.GLView;
import com.imt3673.project.sensors.HapticFeedbackManager;

public class MainActivity extends AppCompatActivity {


    private HapticFeedbackManager hapticManager;
    private MediaManager          mediaManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(new GLView(this));

        this.hapticManager = new HapticFeedbackManager(this);
        this.mediaManager  = new MediaManager(this);

        this.loadResources();
    }

    /**
     * Usage: Copy resources to the res/raw folder, and access with R.raw.file.
     * TODO:  This could be done asynchronously for optimization.
     */
    private void loadResources() {
        this.mediaManager.loadResource(R.raw.ping_001, Constants.MEDIA_TYPE_SOUND);
    }

}
