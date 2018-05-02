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

    private Vector2 spawnPoint;

    //Ball physics variables
    private float accelDelta; //Used for acceleration calculations
    private final float drag = 0.75f; //Used for slowing down ball when hitting wall
    //private boolean reversePhysics = false; // Used to flip acceleration data while physics are "flipped"

    /**
     * Constructs the ball
     * @param position position of ball
     * @param phoneHeight height of phone
     */
    public Ball(Vector2 position, int phoneHeight){
        this.position = position;
        this.spawnPoint = new Vector2(position);
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
     * @param collisionGroups collision groups
     * @return what the ball collided with
     */
    public BallCollision physicsUpdate(final float[] accelData, float deltaTime, ArrayList<Pair<RectF, ArrayList<Block>>> collisionGroups){
        /*if(reversePhysics){
            accelData[0] = -accelData[0];
            accelData[1] = -accelData[1];
            accelData[2] = -accelData[2];
        }*/
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


                        if(collision.blockType == Block.TYPE_HOLE){
                            holeTrigger(block);
                            // No return if block type is TYPE_HOLE because holes act as triggers instead of colliders
                        }
                        else {
                            position.setAxis(axis, oldPos.getAxis(axis));
                            velocity.setAxis(axis, -velocity.getAxis(axis) * drag);
                            return collision;
                        }

                    }
                }
            }
        }
        return new BallCollision();
    }


    /**
     * Sets the position of the ball and sets velocity to 0
     * @param pos position to set
     */
    private void setPosition(Vector2 pos){
        position = pos;
        velocity = Vector2.zero;
    }

    /**
     * Function to run when inside the collider block of a hole
     *
     * Resets the player position if close enough to hole center
     * "Pulls" the ball towards the center if not close enough
     */
    private void holeTrigger(Block block){
        if(Vector2.distance(block.getCenter(), getPosition()) < Level.getPixelSize() * 0.35f){
            setPosition(new Vector2(spawnPoint));
        }
        else {
            Vector2 fall = Vector2.subtract(block.getCenter(), getPosition());
            float force = 1 - fall.magnitude() / Level.getPixelSize();
            fall = fall.normalized();
            fall.x *= force * 5;
            fall.y *= force * 5;
            velocity.x += fall.x;
            velocity.y += fall.y;
        }
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
