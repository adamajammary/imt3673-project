package com.imt3673.project.main;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;

import com.imt3673.project.Objects.Ball;
import com.imt3673.project.Objects.Boundry;
import com.imt3673.project.graphics.CanvasView;
import com.imt3673.project.media.Constants;
import com.imt3673.project.media.MediaManager;
import com.imt3673.project.sensors.HapticFeedbackManager;
import com.imt3673.project.sensors.SensorListenerManager;
import com.imt3673.project.services.ServicesManager;
import com.imt3673.project.utils.Vector2;

import static com.imt3673.project.services.Constants.GOOGLE_SIGNIN_RESULT;

public class MainActivity extends AppCompatActivity {

    private AcceleratorListener   acceleratorListener;
    private Sensor                acceleratorSensor;
    private HapticFeedbackManager hapticManager;
    private MediaManager          mediaManager;
    private SensorListenerManager sensorManager;
    private ServicesManager       servicesManager;

    private CanvasView canvas;
    private long lastUpdateTime;

    // Game Objects
    private Ball ball;
    private Boundry boundry;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.init();
        this.lastUpdateTime = System.currentTimeMillis();
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
        this.servicesManager.init();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Sign-In to the Google Play Games Services
        if (requestCode == GOOGLE_SIGNIN_RESULT)
            this.servicesManager.authenticateHandleIntent(resultCode, data);
    }

    /**
     * Sets up the necessary app resources.
     */
    private void init() {
        this.initWindow();
        this.initCanvas();

        this.servicesManager     = new ServicesManager(this);
        this.sensorManager       = new SensorListenerManager(this);
        this.acceleratorListener = new AcceleratorListener();
        this.acceleratorSensor   = this.sensorManager.getSensor(Sensor.TYPE_ACCELEROMETER);
        this.hapticManager       = new HapticFeedbackManager(this);
        this.mediaManager        = new MediaManager(this);

        this.loadResources();

        //this.servicesManager.init();
    }

    /**
     * Sets up the canvas settings and game objects.
     */
    private void initCanvas() {
        //setContentView(new GLView(this));
        this.canvas = new CanvasView(this);
        setContentView(this.canvas);

        // So that we wait until the UI system is ready
        this.canvas.post(() -> {
            int w = canvas.getWidth();
            int h = canvas.getHeight();

            ball = new Ball(new Vector2(w / 2, h / 2), w * 0.02f);
            boundry = new Boundry(w, h);
            canvas.addGameObject(ball);
            canvas.addGameObject(boundry);
        });
    }

    /**
     * Sets window fullscreen and remove title bar, and forces landscape orientation.
     */
    private void initWindow() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
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

                if (ball != null) { //Because we dont know when the graphics will be initialized
                    ball.physicsUpdate(sensorEvent.values, deltaTime, boundry.getRectangle());
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
}
