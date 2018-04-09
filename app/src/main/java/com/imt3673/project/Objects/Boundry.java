package com.imt3673.project.Objects;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;

// TODO - Finish this class
/**
 * A box
 */
public class Boundry extends GameObject{
    private RectF rectangle;

    /**
     * Creates a boundry box
     * @param width of box
     * @param height of box
     */
    public Boundry(float width, float height){
        final float gap = 10;
        rectangle = new RectF(gap, gap, width - gap, height - gap);

        paint = new Paint();
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(10);
        paint.setColor(Color.BLACK);
    }

    /**
     * Gets the rectangle
     * @return RectF rectangle
     */
    public RectF getRectangle(){
        return rectangle;
    }

    @Override
    public void draw(Canvas canvas){
        canvas.drawRect(rectangle, paint);
    }
}
