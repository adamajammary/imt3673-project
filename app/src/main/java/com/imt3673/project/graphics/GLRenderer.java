package com.imt3673.project.graphics;

import android.opengl.GLES20;
import android.opengl.GLSurfaceView;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 *
 */
class GLRenderer implements GLSurfaceView.Renderer {

    private Square        square;
    private Triangle      triangle;
    private ShaderManager shaderManager;

    /**
     * Called for each redraw of the view.
     */
    @Override
    public void onDrawFrame(final GL10 gl) {
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);

        if ( this.shaderManager != null) {
            this.square.draw( this.shaderManager.getProgram());
            this.triangle.draw( this.shaderManager.getProgram());
        }
    }

    /**
     * Called once to set up the view's OpenGL ES environment.
     */
    @Override
    public void onSurfaceCreated(final GL10 gl, final EGLConfig config) {
        GLES20.glClearColor(0.0f, 0.0f, 1.0f, 1.0f);

        final float[] colorRed      = { 1.0f, 0.0f, 0.0f, 1.0f };
        final float[] colorGreen    = { 0.0f, 1.0f, 0.0f, 1.0f };
        final short[] squareIndices = { 0, 1, 2, 0, 2, 3 };

        final float[] squareVertices = {
            -0.5f,  0.5f, 0.0f, // top left
            -0.5f, -0.5f, 0.0f, // bottom left
             0.5f, -0.5f, 0.0f, // bottom right
             0.5f,  0.5f, 0.0f  // top right
        };

        final float[] triangleVertices = {
             0.0f,  0.622008459f, 0.0f, // top
            -0.5f, -0.311004243f, 0.0f, // bottom left
             0.5f, -0.311004243f, 0.0f  // bottom right
        };

        this.square        = new Square(squareVertices, squareIndices, colorRed);
        this.triangle      = new Triangle(triangleVertices, colorGreen);
        this.shaderManager = new ShaderManager();
    }

    /**
     * Called if the geometry of the view changes,
     * for example when the device's screen orientation changes.
     */
    @Override
    public void onSurfaceChanged(final GL10 gl, final int width, final int height) {
        GLES20.glViewport(0, 0, width, height);
    }

}
