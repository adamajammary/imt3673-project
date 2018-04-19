package com.imt3673.project.Objects;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.os.SystemClock;


import com.imt3673.project.utils.Vector2;

import java.util.Locale;

/**
 * Timer class
 * Used to draw a timer in the right corner when the user starts a level.
 * Keeps track of how long time spent on this level.
 * Inspired by https://www.android-examples.com/android-create-stopwatch-example-tutorial-in-android-studio/
 */
public class Timer extends GameObject{

    private final float TEXT_SIZE = 50.0f;

    private int minutes;
    private int seconds;
    private int milliSeconds;

    private long millisecondsTime;
    private long startTime;
    private long updateTime;
    private long timeAtPause;

    private final Handler handler;

    private String time;


    /**
     * Initializes the timer
     * @param canvasSize size of the canvas on which the timer will be drawn
     * @param timeHandler handler used to run the timerRunnable
     */
   public Timer(Vector2 canvasSize, Handler timeHandler){
        this.reset();

        position = new Vector2((canvasSize.x / 7) * 6 , TEXT_SIZE);

        paint = new Paint();
        paint.setColor(Color.BLACK);
        paint.setStyle(Paint.Style.FILL);
        paint.setTextSize(TEXT_SIZE);

        this.handler = timeHandler;
    }

    /**
     * Starts the timer.
     */
    public void start(){
        this.startTime = SystemClock.uptimeMillis();
        this.handler.postDelayed(runnableTimer,0);
    }

    /**
     * Stops the timer.
     * if timer is started again with start()
     * the timer will resume its time.
     * call reset() to reset the timer to 00:00:00
     */
    public void stop(){
        this.timeAtPause += millisecondsTime;
        this.handler.removeCallbacks(runnableTimer);
    }

    /**
     * Reset the timer to 00:00:00
     */
    public void reset(){
        milliSeconds = 0;
        seconds = 0;
        minutes = 0;
        millisecondsTime = 0L;
        startTime = 0L;
        updateTime = 0L;
        timeAtPause = 0L;

        time = "00:00:00";
    }

    /**
     * Get the time
     * @return time in 00:00:00 format
     */
    public String getTime(){
        return time;
    }

    /**
     * Create a runnable timer
     * updates the time string with new time
     */
    private final Runnable runnableTimer = new Runnable() {
        @Override
        public void run() {

            millisecondsTime = SystemClock.uptimeMillis() - startTime;

            updateTime = timeAtPause + millisecondsTime;

            seconds = (int)(updateTime / 1000);

            minutes = seconds / 60;

            seconds = seconds % 60;

            milliSeconds = (int) (updateTime % 1000);

            time =  String.format(Locale.ENGLISH,"%02d",minutes)  + ":" + String.format(Locale.ENGLISH,"%02d",seconds) + ":" + String.format(Locale.ENGLISH,"%02d",milliSeconds).substring(0,2);

            handler.postDelayed(this,0); // loop
        }
    };

    /**
     * Draw timer
     * @param canvas canvas draw-target
     * @param cameraPosition position of camera
     */
    @Override
    public void draw(Canvas canvas, Vector2 cameraPosition) {
        canvas.drawText(time, position.x, position.y,paint);
    }
}