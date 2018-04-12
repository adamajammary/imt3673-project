package com.imt3673.project.main;

import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

import com.imt3673.project.Objects.Ball;
import com.imt3673.project.Objects.Level;
import com.imt3673.project.graphics.CanvasView;
import com.imt3673.project.media.Constants;
import com.imt3673.project.media.MediaManager;
import com.imt3673.project.graphics.GLView;
import com.imt3673.project.sensors.HapticFeedbackManager;
import com.imt3673.project.sensors.SensorListenerManager;
import com.imt3673.project.utils.Vector2;

public class MainActivity extends AppCompatActivity {

    private AcceleratorListener   acceleratorListener;
    private Sensor                acceleratorSensor;
    private HapticFeedbackManager hapticManager;
    private MediaManager          mediaManager;
    private SensorListenerManager sensorManager;

    private CanvasView canvas;
    int w;
    int h ;
    private Boolean ready = false;
    private long lastUpdateTime = 0;

    //GameObjects
    private Ball ball;
    private Level level;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Set window fullscreen and remove title bar, and force landscape orientation
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        //setContentView(new GLView(this));
        canvas = new CanvasView(this);
        setContentView(canvas);

        this.sensorManager       = new SensorListenerManager(this);
        this.acceleratorListener = new AcceleratorListener();
        this.acceleratorSensor   = this.sensorManager.getSensor(Sensor.TYPE_ACCELEROMETER);
        this.hapticManager       = new HapticFeedbackManager(this);
        this.mediaManager        = new MediaManager(this);

        this.loadResources();

        lastUpdateTime = System.currentTimeMillis();

        canvas.post(new Runnable() {
            @Override
            public void run() { //So that we wait until the UI system is ready
                w = canvas.getWidth();
                h = canvas.getHeight();

                new LoadLevel().execute();
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        this.sensorManager.removeListener(this.acceleratorListener);
    }

    @Override
    protected void onResume() {
        super.onResume();
        this.sensorManager.addListener(this.acceleratorListener, this.acceleratorSensor);
    }

    /**
     * Usage: Copy resources to the res/raw folder, and access with R.raw.file.
     * TODO:  This could be done asynchronously for optimization.
     */
    private void loadResources() {
        this.mediaManager.loadResource(R.raw.ping_001, Constants.MEDIA_TYPE_SOUND);
    }

    /**
     * Accelerator Sensor Listener
     */
    private class AcceleratorListener implements SensorEventListener {

        /**
         * Handles sensor events when a sensor has changed.
         * SENSOR_DELAY_GAME: Updates roughly about 60 times a second.
         * @param sensorEvent The sensor event
         */
        @Override
        public void onSensorChanged(final SensorEvent sensorEvent) {
            if (sensorEvent.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
                long  currentTime = System.currentTimeMillis();
                float deltaTime   = ((float)(currentTime - lastUpdateTime) / 1000.0f);
                lastUpdateTime = currentTime;

                if (ready) { //Because we dont know when the graphics will be initialized
                    //ball.physicsUpdate(sensorEvent.values, deltaTime, );
                    canvas.draw();
                }
            }
        }

        /**
         * Not used, but must be overridden to implement abstract interface SensorEventListener.
         * @param sensor The sensor
         * @param delta The new accuracy
         */
        @Override
        public void onAccuracyChanged(final Sensor sensor, int delta) {
        }
    }

    private class LoadLevel extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            level = new Level();
            level.buildFromPNG(mediaManager.loadLevelPNG("level1"), w, h);

            ball = new Ball(new Vector2(w / 2, h / 2), w * 0.02f);

            return null;
        }

        @Override
        protected void onProgressUpdate(Void... voids) {

        }

        @Override
        protected void onPostExecute(Void voids) {
            canvas.setLevel(level);
            canvas.addGameObject(ball);
            ready = true;
        }
    }
}
