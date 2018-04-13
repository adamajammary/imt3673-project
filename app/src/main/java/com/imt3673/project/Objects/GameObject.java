package com.imt3673.project.Objects;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.Shader;

import com.imt3673.project.main.R;
import com.imt3673.project.utils.Vector2;

/**
 * Base class for gameobjects
 */
public abstract class GameObject {
    protected Vector2 position;
    protected Paint paint;
    protected Shader shader;
    protected Bitmap bitmap;

    protected final int texture256scale = 335; // Used for scaling textures(256x256). I have NO CLUE why I have to scale by 335, and not 256....

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
     * @param context context
     * @param textureid textureID
     */
    public void setTexture(Context context, int textureid){
        bitmap = BitmapFactory.decodeResource(context.getResources(), textureid);
        shader = new BitmapShader(bitmap, Shader.TileMode.REPEAT, Shader.TileMode.REPEAT);
        paint.setShader(shader);
    }

}
