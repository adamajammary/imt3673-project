package com.imt3673.project.Objects;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;

import com.imt3673.project.utils.Vector2;

/**
 * A rectangular block
 * The position is "baked" into the rectangle.
 */
public class Block extends GameObject{
    //Types of blocks, clear means no block
    //Made types color, so that its compatibly with the bitmaps
    public static final int TYPE_CLEAR = Color.WHITE;
    public static final int TYPE_OBSTACLE = Color.BLACK;
    public static final int TYPE_GOAL = Color.GREEN;


    private int type = 0;
    private RectF rectangle;
    private Vector2[] corners;

    /**
     * Creates a boundry box
     * @param width of box
     * @param height of box
     */
    public Block(Vector2 position, float width, float height, int type){
        this.position = position;
        this.type = type;

        corners = new Vector2[]{
            position,
            new Vector2(position.x + width, position.y),
            new Vector2(position.x, position.y + height),
            new Vector2(position.x + width, position.y + height)
        };

        rectangle = new RectF(position.x, position.y, position.x + width, position.y + height);

        paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(type);

        // Update shader matrix
        Matrix mtx = new Matrix();
        //mtx.setTranslate(position.x, position.y);
        //mtx.postScale(0.1f, 0.1f);
        //shader.setLocalMatrix(mtx);
    }

    /**
     * Gets the rectangle
     * @return RectF rectangle
     */
    public RectF getRectangle(){
        return rectangle;
    }

    /**
     * Gets the corners of the rectangle
     * @return Vector2[4] corners
     */
    public Vector2[] getCorners(){
        return corners;
    }

    /**
     * Returns the type of the block
     * @return int type
     */
    public int getType(){
        return type;
    }

    @Override
    public void draw(Canvas canvas, Vector2 cameraPosition){
        Vector2 viewPos = Vector2.subtract(position, cameraPosition);
        /*canvas.save();
        canvas.translate(rectangle.left, rectangle.right);

        Matrix mtx = new Matrix();
        mtx.setScale(rectangle.width(), rectangle.height());
        shader.setLocalMatrix(mtx);

        canvas.drawRect(Vector2.subtract(cameraPosition, rectangle), paint);

        canvas.restore();*/

        canvas.drawRect(Vector2.subtract(cameraPosition, rectangle), paint);
    }
}
