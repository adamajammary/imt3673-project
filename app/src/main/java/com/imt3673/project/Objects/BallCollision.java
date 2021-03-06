package com.imt3673.project.Objects;


/**
 * Contains information about a collision between the ball and a block
 */
public class BallCollision {
    public int blockType = Block.TYPE_CLEAR;
    public float magnitude; //A measure of how "hard" the collision was

    /**
     * Compres two collisions
     * @param other the collision to compare against
     * @return if this collision is greater then the other
     */
    public boolean greater(BallCollision other){
        boolean typeComp = Block.TYPE_VALUES.get(blockType) > Block.TYPE_VALUES.get(other.blockType);
        boolean typeEqual = blockType == other.blockType;
        boolean magnitudeComp = magnitude > other.magnitude;

        if (!typeEqual) {
            return  typeComp;
        } else {
            return magnitudeComp;
        }
    }

    public boolean isImpactful(){
        return magnitude > 250;
    }
}
