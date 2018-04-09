package com.imt3673.project.Objects;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;

// TODO - Finish this class
/**
 * A box
 */
public class Boundry extends GameObject{

    private RectF rectangle;
    private Paint paint;

    public Boundry(float width, float height){

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

    }
}
