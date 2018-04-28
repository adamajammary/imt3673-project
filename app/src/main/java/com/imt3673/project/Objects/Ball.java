package com.imt3673.project.Objects;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.Pair;

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
    private float accelDelta; //Used for acceleration calculations
    private final float drag = 0.75f; //Used for slowing down ball when hitting wall

    /**
     * Constructs the ball
     * @param position position of ball
     * @param phoneHeight
     */
    public Ball(Vector2 position, int phoneHeight){
        this.position = position;
        this.radius = phoneHeight * 0.025f; //Make radius 2.5% of phone height

        accelDelta = phoneHeight * 0.005f; //Make acceleration scale with phone size too
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
     * @return what the ball collided with
     */
    public BallCollision physicsUpdate(final float[] accelData, float deltaTime, ArrayList<Pair<RectF, ArrayList<Block>>> collisionGroups){
        //zFactor helps reduce acceleration when the phone is put flat on a table
        float zFactor = 1 - (Math.abs(accelData[2]) / (Math.abs(accelData[0]) + Math.abs(accelData[1]) + Math.abs(accelData[2])));
        velocity = Vector2.add(velocity, new Vector2(accelData[1] * accelDelta * zFactor, accelData[0] * accelDelta * zFactor));
        BallCollision hit1 = physicsUpdateAxis(0, deltaTime, collisionGroups);
        BallCollision hit2 = physicsUpdateAxis(1, deltaTime, collisionGroups);

        return (hit1.greater(hit2)) ? hit1 : hit2;
    }

    /**
     * Does velocity and position calculations for an axis
     * @param axis axis to update for
     * @return what the ball collided with
     */
    private BallCollision physicsUpdateAxis(int axis, float deltaTime,  ArrayList<Pair<RectF, ArrayList<Block>>> collisionGroups){
        Vector2 oldPos = new Vector2(position);
        position.setAxis(axis, position.getAxis(axis) + velocity.getAxis(axis) * deltaTime);

        LineSegment movement = new LineSegment(oldPos, position);

        for (Pair<RectF, ArrayList<Block>> collisionGroup : collisionGroups) {
            if (Physics.BallBlockCollision(this, collisionGroup.first)) { //Figure out if ball is inside collision group
                for (int i = 0; i < collisionGroup.second.size(); i++) {
                    Block block = collisionGroup.second.get(i);
                    //The first function checks if we passed through a block, the second if we are intersecting it
                    if (Physics.BallBlockCollision(this, block.getRectangle()) || Physics.LineSegmentBlockCollision(movement, block)) {

                        BallCollision collision = new BallCollision();
                        collision.blockType = block.getType();
                        collision.magnitude = Math.abs(velocity.getAxis(axis));

                        if (block.getType() == Block.TYPE_BREAKABLE){
                            if (collision.magnitude > 250) {
                                breakBlock(axis, ((BreakableBlock) block), collisionGroups);
                            }
                        }

                        position.setAxis(axis, oldPos.getAxis(axis));
                        velocity.setAxis(axis, -velocity.getAxis(axis) * drag);

                        return collision;
                    }
                }
            }
        }
        return new BallCollision();
    }

    /**
     * Breaks a breakable block
     * @param axis axis that broke it
     * @param block block in question
     * @param collisionGroups all collision groups (for removing the block)
     */
    private void breakBlock(int axis, BreakableBlock block, ArrayList<Pair<RectF, ArrayList<Block>>> collisionGroups){
        Vector2 breakVel = new Vector2();
        breakVel.setAxis(axis, velocity.getAxis(axis) * 0.25f);
        block.breakBlock(breakVel);
        for (Pair<RectF, ArrayList<Block>> collisionGroup : collisionGroups) {
            collisionGroup.second.remove(block);
        }
    }

    /**
     * Draws the ball to the canvas
     * @param canvas draw target canvas
     */
    @Override
    public void draw(Canvas canvas, Vector2 cameraPosition){
        Vector2 viewPos = Vector2.subtract(position, cameraPosition);


        Matrix m = new Matrix();
        m.setScale((radius * 2)/bitmap.getScaledWidth(canvas), (radius * 2)/bitmap.getScaledWidth(canvas));
        m.postTranslate(radius, radius);
        m.postRotate(311); // Use this to rotate the ball
        m.postTranslate(viewPos.x, viewPos.y);
        shader.setLocalMatrix(m);


        canvas.drawCircle(viewPos.x, viewPos.y, radius, paint);
    }
}
