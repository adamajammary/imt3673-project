package com.imt3673.project.graphics;

import android.opengl.GLES20;

/**
 *
 */
class ShaderManager {

    private final int program;

    /**
     *
     */
    ShaderManager() {
        String vertexShaderSource = (
            "uniform   mat4 mvpMatrix;" +
            "attribute vec4 position;" +
            "void main() {" +
            "   gl_Position = (mvpMatrix * position);" +
            "}"
        );

        String fragmentShaderSource = (
            "precision mediump float;" +
            "uniform vec4 color;" +
            "void main() {" +
            "   gl_FragColor = color;" +
            "}"
        );

        Shader vertexShader   = new Shader(GLES20.GL_VERTEX_SHADER,   vertexShaderSource);
        Shader fragmentShader = new Shader(GLES20.GL_FRAGMENT_SHADER, fragmentShaderSource);

        this.program = GLES20.glCreateProgram();

        GLES20.glAttachShader(this.program, vertexShader.getShader());
        GLES20.glAttachShader(this.program, fragmentShader.getShader());

        GLES20.glLinkProgram(this.program);
    }

    /**
     *
     * @return
     */
    public int getProgram() {
        return this.program;
    }

}
