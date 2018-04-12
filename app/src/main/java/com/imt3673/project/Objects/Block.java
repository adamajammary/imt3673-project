package com.imt3673.project.Objects;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;

import com.imt3673.project.utils.Vector2;

/**
 * A rectangular block
 */
public class Block extends GameObject{
    public static final int TYPE_OBSTACLE = 0;
    public static final int TYPE_GOAL = 1;

    private int type = 0;
    private RectF rectangle;


    /**
     * Creates a boundry box
     * @param width of box
     * @param height of box
     */
    public Block(Vector2 position, float width, float height, int type, int color){
        this.position = position;
        this.type = type;

        rectangle = new RectF(position.x, position.y + height, position.x + width, position.y);

        paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(color);
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
