package com.imt3673.project.graphics;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;

/**
 * Canvas View - Custom canvas view used for drawing 2D graphics.
 */
public class CanvasView extends View {

    private final Paint drawPen      = new Paint();
    private long        lastDrawTime = 0;

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

        // TODO: Update objects

        // Clear the background
        canvas.drawColor(Color.BLUE);

        // TODO: Draw objects
        this.drawPen.setAntiAlias(true);
        this.drawPen.setColor(Color.YELLOW);
        this.drawPen.setStyle(Paint.Style.FILL);
        canvas.drawCircle(canvas.getWidth() / 2, canvas.getHeight() / 2, 200, this.drawPen);

    }

}
