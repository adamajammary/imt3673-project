package com.imt3673.project.main;

import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.WindowManager;

import com.imt3673.project.Objects.Ball;
import com.imt3673.project.Objects.BallCollision;
import com.imt3673.project.Objects.Block;
import com.imt3673.project.Objects.Level;
import com.imt3673.project.Objects.Timer;
import com.imt3673.project.database.AppDatabase;
import com.imt3673.project.database.HighScore;
import com.imt3673.project.graphics.CanvasView;
import com.imt3673.project.media.Constants;
import com.imt3673.project.media.MediaManager;
import com.imt3673.project.media.TextureManager;
import com.imt3673.project.media.TextureSet;
import com.imt3673.project.sensors.HapticFeedbackManager;
import com.imt3673.project.sensors.SensorListenerManager;
import com.imt3673.project.services.GooglePlayService;
import com.imt3673.project.utils.Vector2;

public class MainActivity extends AppCompatActivity {
    private AcceleratorListener   acceleratorListener;
    private Sensor                acceleratorSensor;
    private HapticFeedbackManager hapticManager;
    private MediaManager          mediaManager;
    private SensorListenerManager sensorManager;
    private AppDatabase           database;
    private String currentLevelName;

    private CanvasView canvas;
    private static int canvasWidth;
    private static int canvasHeight;
    private Boolean ready = false;
    private long lastUpdateTime = 0;

    //GameObjects
    private Ball ball;
    private Level level;

    private Timer levelTimer;
    private Handler timeHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.timeHandler = new Handler();
        this.database = AppDatabase.getAppDatabase(this);
        this.currentLevelName = getIntent().getStringExtra("level");

        // Set window fullscreen and remove title bar, and force landscape orientation
       // this.requestWindowFeature(Window.FEATURE_NO_TITLE);
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
                canvasWidth = canvas.getWidth();
                canvasHeight = canvas.getHeight();
                if (canvasHeight != 0 && canvasWidth != 0){
                    new LoadLevel().execute(getIntent().getStringExtra("level"));
                }
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
     */
    private void loadResources() {
        this.mediaManager.loadResource(R.raw.ping_001, Constants.MEDIA_TYPE_SOUND);
    }


    public static int getCanvasHeight() {
        return canvasHeight;
    }

    /**
     * This gets called when the ball hits the goal
     */
    private void goalReached(){
        this.levelTimer.stop();
        this.saveTimeToDb();
        this.saveTimeToGooglePlay();
        this.levelTimer.reset();

        onBackPressed();
    }

    /**
     * Saves time to database
     */
    private void saveTimeToDb() {
        HighScore score = new HighScore();
        score.setLevelName(this.currentLevelName);
        score.setLevelTime(this.levelTimer.getTime());

        this.database.highScoreDao().insertAll(score);
    }

    /**
     * Saves the time to the online leaderboard on Google Play if the user is signed in.
     */
    private void saveTimeToGooglePlay() {
        GooglePlayService googlePlayService = new GooglePlayService(this);

        if (googlePlayService.isSignedIn())
            googlePlayService.updateLeaderboard(this.currentLevelName, this.levelTimer.getTimeMilliseconds());
    }

    /**
     * Called to give feedback when colliding
     */
    private void collisionFeedBack(){
        hapticManager.vibrate(250);
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
                    BallCollision hit = ball.physicsUpdate(sensorEvent.values, deltaTime, level.getBlocks());
                    canvas.draw();

                    if (hit.blockType != Block.TYPE_CLEAR && hit.magnitude > 250){
                       collisionFeedBack();
                    }

                    if (hit.blockType == Block.TYPE_GOAL){
                        goalReached();
                    }
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

    private class LoadLevel extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... strings) {
            TextureSet textureSet = TextureManager.getTextureSet("default");

            level = new Level();
            level.setTextureSet(textureSet);
            Bitmap levelBitMap = mediaManager.loadLevelPNG(strings[0]);
            level.buildFromPNG(levelBitMap, canvasHeight, MainActivity.this);

            ball = new Ball(new Vector2(level.getSpawnPoint()), canvasHeight);
            ball.setTexture(MainActivity.this, textureSet, TextureSet.BALL_TEX);

            levelTimer = new Timer(new Vector2(canvasWidth,canvasHeight), timeHandler);


            return null;
        }

        @Override
        protected void onProgressUpdate(Void... voids) {

        }

        @Override
        protected void onPostExecute(Void voids) {
            canvas.setLevel(level);
            canvas.setBall(ball);
            canvas.setTimer(levelTimer);
            levelTimer.start();
            ready = true;
        }
    }
}
