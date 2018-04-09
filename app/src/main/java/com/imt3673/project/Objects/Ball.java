package com.imt3673.project.Objects;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;

import com.imt3673.project.utils.Vector2;

/**
 * Created by Muffinz on 09/04/2018.
 */
public class Ball extends GameObject {
    private Vector2 velocity;
    private float radius;

    //Ball physics variables
    final float accelDelta = 0.1f; //Used for acceleration calculations
    final float drag = 0.95f; //Used for slowing down ball when hitting wall

    private Paint paint;

    /**
     * Constructs the ball
     * @param position position of ball
     * @param radius radius of ball
     */
    public Ball(Vector2 position, float radius){
        this.position = position;
        this.radius = radius;
        velocity = new Vector2(); //Defaults to zero

        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(Color.BLUE);
        paint.setStyle(Paint.Style.FILL);
    }

    /**
     * Does the physics update for ball.
     * @param accelData xyz acceleration data
     * @param boundry bounding box
     */
    public void physicsUpdate(final float[] accelData, RectF boundry){
        //zFactor helps reduce acceleration when the phone is put flat on a table
        float zFactor = 1 - (Math.abs(accelData[2]) / (Math.abs(accelData[0]) + Math.abs(accelData[1]) + Math.abs(accelData[2])));
        velocity = Vector2.add(velocity, new Vector2(accelData[1] * accelDelta * zFactor, accelData[0] * accelDelta * zFactor));
        Vector2 oldPos = position;
        position = Vector2.add(position, velocity);

        boolean collision = false;
        if (position.x - radius < boundry.left || position.x + radius > boundry.right) {
            velocity.x = -velocity.x * drag;
            position.x = oldPos.x;
            collision = true;
        }
        if (position.y - radius < boundry.top || position.y + radius > boundry.bottom) {
            velocity.y = -velocity.y * drag;
            position.y = oldPos.y;
            collision = true;
        }

        if (collision){
            //TODO - Trigger user feedback (sound/vibration(If we want that ofc))
        }
    }

    /**
     * Draws the ball to the canvas
     * @param canvas draw target canvas
     */
    @Override
    public void draw(Canvas canvas){
        canvas.drawCircle(position.x, position.y, radius, paint);
    }
}
