package com.imt3673.project.graphics;

import android.annotation.SuppressLint;
import android.content.Context;
import android.opengl.GLSurfaceView;
import android.view.MotionEvent;

/**
 *
 */
public class GLView extends GLSurfaceView {

    private GLRenderer glRenderer;
    private float      previousX;
    private float      previousY;

    /**
     * @param context activity context
     */
    public GLView(final Context context){
        super(context);
        this.setRenderer();
    }

    /**
     * @param event the touch event
     * @return true
     */
    @SuppressLint("ClickableViewAccessibility") // Suppressing because the performClick() func would be left empty as we don't know what it should do, and Stack Overflow just says to suppress it ¯\_(ツ)_/¯
    @Override
    public boolean onTouchEvent(final MotionEvent event) {
        super.onTouchEvent(event);
        performClick();

        float x = event.getX();
        float y = event.getY();

        if (event.getAction() == MotionEvent.ACTION_MOVE) {
            float deltaX = (x - this.previousX);
            float deltaY = (y - this.previousY);

            // Reverse direction of rotation above the mid-line
            if (y > (getHeight() / 2))
                deltaX = (deltaX * -1.0f);

            // Reverse direction of rotation to left of the mid-line
            if (x < (getWidth() / 2))
                deltaY = (deltaY * -1.0f);

            this.glRenderer.setAngle(this.glRenderer.getAngle() + ((deltaX + deltaY) * Constants.TOUCH_SCALE_FACTOR));
            requestRender();
        }

        this.previousX = x;
        this.previousY = y;

        return true;
    }

    /**
     * Sets the renderer
     */
    private void setRenderer() {
        setEGLContextClientVersion(2);

        this.glRenderer = new GLRenderer();
        setRenderer(this.glRenderer);

        // Only render when render data has changed
        setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
    }

}
