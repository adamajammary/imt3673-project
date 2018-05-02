package com.imt3673.project.graphics;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.imt3673.project.Objects.Ball;
import com.imt3673.project.Objects.Block;
import com.imt3673.project.Objects.GameObject;
import com.imt3673.project.Objects.Level;
import com.imt3673.project.Objects.Timer;
import com.imt3673.project.utils.Vector2;

import java.util.ArrayList;

/**
 * Canvas View - Custom canvas view used for drawing 2D graphics.
 */
public class CanvasView extends View {
    private static final String TAG = CanvasView.class.getName();

    private long        lastDrawTime = 0;

    private ArrayList<GameObject> gameObjects = new ArrayList<>();
    private Level level;
    private Ball ball;
    private Timer levelTimer;
    private Vector2 cameraPos = new Vector2();

    /**
     * @param context Context
     */
    public CanvasView(final Context context) {
        super(context);
        setFocusable(true);
    }

    /**
     * Sets timer to draw
     * @param timer Timer object to be drawn
     */
    public void setTimer(Timer timer){
        this.levelTimer = timer;
    }

    /**
     * Adds the GameObject to the canvas
     * @param obj a gameobject to add
     */
    public void addGameObject(GameObject obj){
        gameObjects.add(obj);
    }

    /**
     * Sets the level to draw
     * @param level the level
     */
    public void setLevel(Level level) {
        this.level = level;
        Log.d(TAG, "Adding level");
    }

    /**
     * Sets the ball
     * @param ball the ball
     */
    public void setBall(Ball ball){
        this.ball = ball;
    }

    /**
     * Triggers a draw call
     */
    public void draw(){
        invalidate();
    }

    /**
     * @param canvas Canvas
     */
    protected void onDraw(final Canvas canvas) {
        long  currentTime = System.currentTimeMillis();
        float deltaTime   = (1.0f / (float)(currentTime - this.lastDrawTime));

        this.lastDrawTime = currentTime;

        // Clear the background
        canvas.drawColor(Color.WHITE);

        cameraPos.x = 0;
        cameraPos.y = 0;

        if (ball != null) { // Update camera position
            cameraPos.x = ball.getPosition().x - canvas.getWidth() / 2;
        }

        if(level != null){ // Draw background
            level.getBackground().draw(canvas, cameraPos);
        }

        if (level != null){ // Draw level objects
            level.draw(canvas, cameraPos);
        }

        for (GameObject obj : gameObjects){
            obj.draw(canvas, cameraPos);
        }

        if (ball != null) { // Draw ball
            ball.draw(canvas, cameraPos);
        }

        if(levelTimer != null){
            levelTimer.draw(canvas,cameraPos);
        }
    }


}
