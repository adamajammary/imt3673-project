package com.imt3673.project.Objects;

import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Shader;

import com.imt3673.project.media.TextureSet;
import com.imt3673.project.utils.Vector2;

/**
 * Base class for gameobjects
 */
public abstract class GameObject {
    protected Vector2 position;
    protected Paint paint;
    protected Shader shader;
    protected Bitmap bitmap;

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

    /**
     * Sets the texture for the gameobject
     * @param textureSet textureSet
     * @param textureType textureType
     */
    public void setTexture(TextureSet textureSet, int textureType){
        bitmap = textureSet.getTexture(textureType);
        shader = new BitmapShader(bitmap, Shader.TileMode.REPEAT, Shader.TileMode.REPEAT);
        paint.setShader(shader);
    }
}
