package com.imt3673.project.main;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.annotation.SuppressLint;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import android.view.View;

import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

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
    private String goldTime;
    private String silverTime;
    private String bronzeTime;

    private CanvasView canvas;
    private static int canvasWidth;
    private static int canvasHeight;

    private Boolean ready = false;
    private long lastUpdateTime = 0;

    // Medals
    private final int GOLD   = 1;
    private final int SILVER = 2;
    private final int BRONZE = 3;


    //GameObjects
    private Ball ball;
    private Level level;

    private Timer levelTimer;

    private Handler timeHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.initMain();
        this.initGraphics();
        this.initManagers();
        this.loadResources();
        this.initCanvas();
    }

    /**
     * Initializes the canvas and loads the selected level.
     */
    private void initCanvas() {
        lastUpdateTime = System.currentTimeMillis();
        canvas.post(() -> { //So that we wait until the UI system is ready

            canvasWidth = canvas.getWidth();
            canvasHeight = canvas.getHeight();
            if (canvasHeight != 0 && canvasWidth != 0){
                new LoadLevel().execute(getIntent().getStringExtra("level"));
            }
        });
    }

    /**
     * Sets window fullscreen and removes title bar, and forces landscape orientation.
     */
    private void initGraphics() {
        // this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        this.canvas = new CanvasView(this);
        setContentView(this.canvas);
    }

    /**
     * Initializes the main resources used by the main activity.
     */
    private void initMain() {
        this.timeHandler      = new Handler();
        this.database         = AppDatabase.getAppDatabase(this);
        this.currentLevelName = getIntent().getStringExtra("level");
        this.goldTime         = getIntent().getStringExtra("gold_time");
        this.silverTime       = getIntent().getStringExtra("silver_time");
        this.bronzeTime       = getIntent().getStringExtra("bronze_time");
    }

    /**
     * Initializes the various resource managers the main activity will use.
     */
    private void initManagers() {
        this.sensorManager       = new SensorListenerManager(this);
        this.acceleratorListener = new AcceleratorListener();
        this.acceleratorSensor   = this.sensorManager.getSensor(Sensor.TYPE_ACCELEROMETER);
        this.hapticManager       = new HapticFeedbackManager(this);
        this.mediaManager        = new MediaManager(this);
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
        this.sensorManager.removeListener(this.acceleratorListener);

        this.displayWinScreen();
        this.saveTimeToDb();
        this.saveTimeToGooglePlay();
        this.displayLevelChooserButton();
        this.levelTimer.reset();
    }

    /**
     * Display level chooser button in win screen
     */
    private void displayLevelChooserButton() {
        Button doneBtn = findViewById(R.id.win_screen_button);

        doneBtn.setOnClickListener(v -> onBackPressed());

        doneBtn.setVisibility(View.VISIBLE);
    }

    /**
     * Displays win screen with time and stars earned.
     */
    private void displayWinScreen() {

        // Switch to the Win Screen layout
        setContentView(R.layout.win_screen);

        // Display and animate the big star
        this.animateBigStar();

        // Get the int value to calculate number of stars displayed
        int newTime    = Integer.parseInt(this.levelTimer.getTime().replace(":",""));
        int goldTime   = Integer.parseInt(this.goldTime.replace(":",""));
        int silverTime = Integer.parseInt(this.silverTime.replace(":",""));
        int bronzeTime = Integer.parseInt(this.bronzeTime.replace(":",""));

        // Check times and display level-time and stars.
        if(newTime < goldTime){

            this.animateSmallStars(GOLD);
            this.displayLevelTimes(GOLD);
        }
        else if(newTime < silverTime){

            this.animateSmallStars(SILVER);
            this.displayLevelTimes(SILVER);
        }
        else if(newTime < bronzeTime){

            this.animateSmallStars(BRONZE);
            this.displayLevelTimes(BRONZE);
        }
    }

    /**
     * Will display the the level times to beat in the WinScreen
     * with a green color based on what times are beaten.
     * Gold will display green time for Gold, Silver and Bronze
     * Silver will display green time for Silver and Bronze
     * Bronze will display green time for Bronze
     * @param medal Medals earned
     */
    private void displayLevelTimes(final int medal){

        // Get text views
        TextView goldTimeView   = findViewById(R.id.times_to_beat_gold);
        TextView silverTimeView = findViewById(R.id.times_to_beat_silver);
        TextView bronzeTimeView = findViewById(R.id.times_to_beat_bronze);

        // Set the level times to beat
        goldTimeView.setText(this.goldTime);
        silverTimeView.setText(this.silverTime);
        bronzeTimeView.setText(this.bronzeTime);

        // Display green text color based on medals
        switch (medal){
            case GOLD:
                goldTimeView.setTextColor(Color.GREEN);
                silverTimeView.setTextColor(Color.GREEN);
                bronzeTimeView.setTextColor(Color.GREEN);
                break;

            case SILVER:
                silverTimeView.setTextColor(Color.GREEN);
                bronzeTimeView.setTextColor(Color.GREEN);
                break;

            case BRONZE:
                bronzeTimeView.setTextColor(Color.GREEN);
                break;
        }
    }

    /**
     * Will display and animate number of stars based on medals in the WinScreen
     * Gold will display and animate stars for Gold, Silver and Bronze
     * Silver will display and animate stars for Silver and Bronze
     * Bronze will display and animate star for Bronze
     * @param medal Medals to be displayed GOLD, SILVER OR BRONZE
     */
    private void animateSmallStars(final int medal){

       final float rotation = 360f;
       final int   duration = 2000;

        // Get image views
        ImageView goldStar   = findViewById(R.id.win_gold_star);
        ImageView silverStar = findViewById(R.id.win_silver_star);
        ImageView bronzeStar = findViewById(R.id.win_bronze_star);

        switch (medal){
            case GOLD:
                // Set star visibility
                goldStar.setVisibility(View.VISIBLE);
                silverStar.setVisibility(View.VISIBLE);
                bronzeStar.setVisibility(View.VISIBLE);

                // Animate
                goldStar.animate().rotationBy(rotation).setDuration(duration);
                silverStar.animate().rotationBy(rotation).setDuration(duration);
                bronzeStar.animate().rotationBy(rotation).setDuration(duration);
                break;

            case SILVER:
                silverStar.setVisibility(View.VISIBLE);
                bronzeStar.setVisibility(View.VISIBLE);

                silverStar.animate().rotationBy(rotation).setDuration(duration);
                bronzeStar.animate().rotationBy(rotation).setDuration(duration);
                break;

            case BRONZE:
                bronzeStar.setVisibility(View.VISIBLE);
                bronzeStar.animate().rotationBy(rotation).setDuration(duration);
                break;
        }
    }

    /**
     * Animate the big star containing the time in the win screen
     */
    private void animateBigStar() {

        ((TextView)findViewById(R.id.win_screen_time_view)).setText(this.levelTimer.getTime());

        ImageView bigStar = findViewById(R.id.win_screen_big_star);

        Animator scaleDown = AnimatorInflater.loadAnimator(this, R.animator.scale_down_animation);
        scaleDown.setTarget(bigStar);

        Animator scaleUp = AnimatorInflater.loadAnimator(this, R.animator.scale_up_animation);
        scaleUp.setTarget(bigStar);

        AnimatorSet setScaleDownAndUp = new AnimatorSet();
        setScaleDownAndUp.playSequentially(scaleDown, scaleUp);
        setScaleDownAndUp.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                setScaleDownAndUp.start();
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        setScaleDownAndUp.start();
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
                    BallCollision hit = ball.physicsUpdate(sensorEvent.values, deltaTime, level.getCollisionGroups());
                    level.update(deltaTime);
                    canvas.draw();

                    if (hit.isImpactful() && hit.blockType != Block.TYPE_CLEAR && hit.blockType != Block.TYPE_HOLE){
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

    @SuppressLint("StaticFieldLeak")
    private class LoadLevel extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... strings) {
            TextureSet textureSet = TextureManager.getTextureSet("default");

            level = new Level();
            level.setTextureSet(textureSet);
            Bitmap levelBitMap = mediaManager.loadLevelPNG(strings[0]);
            level.buildFromPNG(levelBitMap, canvasHeight);

            ball = new Ball(new Vector2(level.getSpawnPoint()), canvasHeight);
            ball.setTexture(textureSet, TextureSet.BALL_TEX);

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
