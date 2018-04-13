package com.imt3673.project.Objects;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
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

    Vector2 dims;

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
        dims = new Vector2(width, height);

        paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(type);

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

        canvas.save();
        canvas.translate(-cameraPosition.x, -cameraPosition.y);

        Matrix m = new Matrix();
        m.postScale(1f/bitmap.getScaledWidth(canvas) * Level.getPixelSize(), 1f/bitmap.getScaledWidth(canvas) * Level.getPixelSize());
        shader.setLocalMatrix(m);
        canvas.drawRect(Vector2.subtract(Vector2.zero, rectangle), paint);

        canvas.restore();


     //   Matrix m = new Matrix();
     //   m.postScale(1f/bitmap.getScaledWidth(canvas) * Level.getPixelSize(), 1f/bitmap.getScaledWidth(canvas) * Level.getPixelSize());
     //   shader.setLocalMatrix(m);
     //   canvas.drawRect(Vector2.subtract(cameraPosition, rectangle), paint);

    }
}
