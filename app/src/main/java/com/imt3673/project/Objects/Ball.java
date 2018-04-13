package com.imt3673.project.Objects;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;

import com.imt3673.project.utils.LineSegment;
import com.imt3673.project.utils.Vector2;

import java.util.ArrayList;

/**
 * The ball that the player controls
 */
public class Ball extends GameObject {
    private Vector2 velocity;
    private float radius;

    //Ball physics variables
    final float accelDelta = 4f; //Used for acceleration calculations
    final float drag = 0.75f; //Used for slowing down ball when hitting wall

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
     * Gets the radius
     * @return float radius
     */
    public float getRadius(){
        return radius;
    }

    /**
     * Does the physics update for ball.
     * @param accelData xyz acceleration data
     * @param blocks all blocks the ball can collide with
     */
    public void physicsUpdate(final float[] accelData, float deltaTime, ArrayList<Block> blocks){
        //zFactor helps reduce acceleration when the phone is put flat on a table
        float zFactor = 1 - (Math.abs(accelData[2]) / (Math.abs(accelData[0]) + Math.abs(accelData[1]) + Math.abs(accelData[2])));
        velocity = Vector2.add(velocity, new Vector2(accelData[1] * accelDelta * zFactor, accelData[0] * accelDelta * zFactor));
        Vector2 oldPos = position;
        position = Vector2.add(position, Vector2.mult(velocity, deltaTime));

        LineSegment movement = new LineSegment(oldPos, position);

        for(Block block : blocks) {
            //The first function checks if we passed through a block, the second if we are intersecting it
            if (Physics.LineSegmentBlockCollision(movement, block) || Physics.BallBlockCollision(this, block)) {
                RectF rect = block.getRectangle();

                //Now we need to work out what side of the block we hit
                float xd1 = Math.abs(position.x - rect.left);
                float xd2 = Math.abs(position.x - rect.right);
                float xDist = (xd1 < xd2) ? xd1 : xd2; //The distance between ball and block in x dir
                float yd1 = Math.abs(position.y - rect.bottom);
                float yd2 = Math.abs(position.y - rect.top);
                float yDist = (yd1 < yd2) ? yd1 : yd2; //The distance between ball and block in y dir

                if (xDist < yDist){ //Collision in X dir
                    velocity.x = -velocity.x * drag;
                    position.x = oldPos.x;
                } else { //Collision in y dir
                    velocity.y = -velocity.y * drag;
                    position.y = oldPos.y;
                }

                //TODO - Trigger user feedback (sound/vibration(If we want that ofc))
                break;
            }
        }
    }

    /**
     * Draws the ball to the canvas
     * @param canvas draw target canvas
     */
    @Override
    public void draw(Canvas canvas, Vector2 cameraPosition){
        Vector2 viewPos = Vector2.subtract(position, cameraPosition);
        canvas.save();

        canvas.translate(viewPos.x, viewPos.y);
        Matrix m = new Matrix();
        m.setTranslate(texture256scale, texture256scale);
        m.postScale(radius/ texture256scale, radius/ texture256scale);
        //m.postRotate(42); // Use this for rotating the ball
        shader.setLocalMatrix(m);
        canvas.drawCircle(0, 0, radius, paint);
        canvas.restore();
    }
}
