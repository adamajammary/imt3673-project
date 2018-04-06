package com.imt3673.project.graphics;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.view.MotionEvent;

import com.imt3673.project.constants.Constants;

/**
 *
 */
public class GLView extends GLSurfaceView {

    private GLRenderer glRenderer;
    private float      previousX;
    private float      previousY;

    /**
     *
     * @param context
     */
    public GLView(final Context context){
        super(context);
        this.setRenderer();
    }

    /**
     *
     * @param event
     * @return
     */
    @Override
    public boolean onTouchEvent(final MotionEvent event) {
        super.onTouchEvent(event);

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
     *
     */
    private void setRenderer() {
        setEGLContextClientVersion(2);

        this.glRenderer = new GLRenderer();
        setRenderer(this.glRenderer);

        // Only render when render data has changed
        setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
    }

}
