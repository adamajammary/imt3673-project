package com.imt3673.project.Objects;

import android.graphics.Canvas;
import android.graphics.Paint;

import com.imt3673.project.utils.Vector2;

/**
 * Base class for gameobjects
 */
public abstract class GameObject {
    protected Vector2 position;
    protected Paint paint;

    /**
     * gets the position
     * @return Vector2 position
     */
    public Vector2 getPosition(){
        return position;
    }

    /**
     * Draws the gameobject to the canvas
     * @param canvas canvas drawtarget
     * @param cameraPosition position of camera
     */
    public abstract void draw(Canvas canvas, Vector2 cameraPosition);
}
