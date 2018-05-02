package com.imt3673.project.graphics;

import android.opengl.GLES20;

/**
 *
 */
class Shader {

    private final int shader;

    /**
     *
     * @param type type
     * @param shaderSource source
     */
    Shader(final int type, final String shaderSource){
        this.shader = GLES20.glCreateShader(type);

        GLES20.glShaderSource(this.shader, shaderSource);
        GLES20.glCompileShader(this.shader);
    }

    /**
     *
     * @return shader
     */
    public int getShader() {
        return this.shader;
    }

}
