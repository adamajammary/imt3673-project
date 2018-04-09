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

    //We havent decided if we'll go with OpenGL or Android thingie yet,
    //changhing the implementation of draw should be enough though,
    //the rest of the game logic should be independent from the rendering.
    /**
     * Draws the gameobject to the canvas
     * @param canvas canvas drawtarget
     */
    public abstract void draw(Canvas canvas);
}
