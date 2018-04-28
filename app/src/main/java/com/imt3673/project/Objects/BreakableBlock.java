package com.imt3673.project.Objects;

import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.RectF;

import com.imt3673.project.media.TextureManager;
import com.imt3673.project.media.TextureSet;
import com.imt3673.project.utils.Vector2;

/**
 * A block that breaks on impact
 */
public class BreakableBlock extends Block {
    private boolean broken = false;
    private Vector2 velocity;

    private TextureSet textureSet;

    private float timer;
    private final float brokenTTL = 1f;

    private float width;
    private float height;
    private float widthMod;
    private float heightMod;

    /**
     * Creates a boundry box
     * @param width of box
     * @param height of box
     */
    public BreakableBlock(Vector2 position, float width, float height, int type, TextureSet textureSet){
        super(position, width, height, type);
        this.width = width;
        this.height = height;
        this.textureSet = textureSet;
    }

    /**
     * Call this to break the block
     * @param breakVel Velocity for block after breaking
     */
    public void breakBlock(Vector2 breakVel){
        if (!broken){
            broken = true;
            velocity = breakVel;
            setTexture(textureSet, TextureSet.CRATEDAMAGED_TEX);
        }
    }

    /**
     * Updates block, returns true when the block is gone
     * @param deltaTime
     * @return
     */
    public boolean update(float deltaTime){
        if (broken){
            position = Vector2.add(position, Vector2.mult(velocity, deltaTime));
            timer += deltaTime;

            float shrinkDelta = deltaTime * brokenTTL / 2f;
            widthMod += shrinkDelta;
            heightMod += shrinkDelta;
            rectangle = new RectF(
                    position.x + widthMod,
                    position.y + heightMod,
                    position.x + width - widthMod,
                    position.y + height - heightMod);
            return timer >= brokenTTL;
        } else {
            return false;
        }
    }

    /**
     * Get broken state of block
     * @return
     */
    public boolean getBroken(){
        return broken;
    }


    /**
     * Draws the ball to the canvas
     * @param canvas draw target canvas
     */
    /**
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

        Matrix m = new Matrix();
        m.postScale(Level.getPixelSize()/bitmap.getScaledWidth(canvas), Level.getPixelSize()/bitmap.getScaledWidth(canvas));
        m.postTranslate(-cameraPosition.x, -cameraPosition.y);
        shader.setLocalMatrix(m);

        canvas.drawRect(Vector2.subtract(cameraPosition, rectangle), paint);
    }
    */
}
