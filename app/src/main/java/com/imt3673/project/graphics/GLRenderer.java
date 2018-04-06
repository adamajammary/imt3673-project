package com.imt3673.project.graphics;

import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 *
 */
class GLRenderer implements GLSurfaceView.Renderer {

    private volatile float angle;
    private final float[]  mvpMatrix        = new float[16];
    private final float[]  projectionMatrix = new float[16];
    private final float[]  viewMatrix       = new float[16];
    private Square         square;
    private Triangle       triangle;
    private ShaderManager  shaderManager;

    /**
     * Called for each redraw of the view.
     */
    @Override
    public void onDrawFrame(final GL10 gl) {
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);

        this.setMVP();
        this.rotateMVP();

        if (this.shaderManager != null) {
            this.square.draw(this.shaderManager.getProgram(),   this.mvpMatrix);
            this.triangle.draw(this.shaderManager.getProgram(), this.mvpMatrix);
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

        final float ratio = ((float)width / (float)height);
        Matrix.frustumM(this.projectionMatrix, 0, -ratio, ratio, -1, 1, 3, 7);
    }

    /**
     *
     * @return
     */
    public float getAngle() {
        return this.angle;
    }

    /**
     *
     * @param angle
     */
    public void setAngle(final float angle) {
        this.angle = angle;
    }

    /**
     *
     */
    private void rotateMVP() {
        // Auto-rotate over time
        //long  time  = (SystemClock.uptimeMillis() % 4000L);
        //float angle = (0.090f * (int)time);

        float[] rotMatrix = new float[16];

        Matrix.setRotateM(rotMatrix, 0, this.angle, 0, 0, -1.0f);
        Matrix.multiplyMM(this.mvpMatrix, 0, this.mvpMatrix, 0, rotMatrix, 0);
    }

    /**
     *
     */
    private void setMVP() {
        Matrix.setLookAtM(this.viewMatrix, 0, 0, 0, -3, 0f, 0f, 0f, 0f, 1.0f, 0.0f);
        Matrix.multiplyMM(this.mvpMatrix, 0, this.projectionMatrix, 0, this.viewMatrix, 0);
    }

}
