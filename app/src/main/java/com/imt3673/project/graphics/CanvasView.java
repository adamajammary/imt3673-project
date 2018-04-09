package com.imt3673.project.graphics;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;

import com.imt3673.project.Objects.GameObject;

import java.util.ArrayList;

/**
 * Canvas View - Custom canvas view used for drawing 2D graphics.
 */
public class CanvasView extends View {

    private final Paint drawPen      = new Paint();
    private long        lastDrawTime = 0;

    private ArrayList<GameObject> gameObjects = new ArrayList<>();

    /**
     * @param context Context
     */
    public CanvasView(final Context context) {
        super(context);
        setFocusable(true);
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
        canvas.drawColor(Color.BLUE);

        for (GameObject obj : gameObjects){
            obj.draw(canvas);
        }
    }

}
