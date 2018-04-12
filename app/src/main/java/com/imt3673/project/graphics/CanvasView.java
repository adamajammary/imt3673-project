package com.imt3673.project.graphics;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.view.View;

import com.imt3673.project.Objects.GameObject;
import com.imt3673.project.Objects.Level;

import java.util.ArrayList;

/**
 * Canvas View - Custom canvas view used for drawing 2D graphics.
 */
public class CanvasView extends View {
    private static final String TAG = CanvasView.class.getName();

    private long        lastDrawTime = 0;

    private ArrayList<GameObject> gameObjects = new ArrayList<>();
    private Level level;

    /**
     * @param context Context
     */
    public CanvasView(final Context context) {
        super(context);
        setFocusable(true);
    }

    /**
     * Adds the GameObject to the canvas
     * @param obj
     */
    public void addGameObject(GameObject obj){
        gameObjects.add(obj);
    }

    /**
     * Sets the level to draw
     * @param level
     */
    public void setLevel(Level level) {
        this.level = level;
        Log.d(TAG, "Adding level");
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

        // TODO: Update objects (Think object updates should be event based, such as per sensor event)

        // Clear the background
        canvas.drawColor(Color.WHITE);

        if (level != null){
            level.draw(canvas);
        }
        for (GameObject obj : gameObjects){
            obj.draw(canvas);
        }
    }


}
