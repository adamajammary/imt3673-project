package com.imt3673.project.graphics;

import android.content.Context;
import android.opengl.GLSurfaceView;

/**
 *
 */
public class GLView extends GLSurfaceView {

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
     */
    private void setRenderer() {
        setEGLContextClientVersion(2);                      // OpenGL ES 2.0 context
        setRenderer(new GLRenderer());                      // OpenGLES Renderer
        setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY); // Only render when data has changed
    }

}
