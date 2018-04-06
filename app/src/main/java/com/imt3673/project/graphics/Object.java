package com.imt3673.project.graphics;

import android.opengl.GLES20;

import com.imt3673.project.constants.Constants;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

/**
 *
 */
class Object {

    private final float[] color;
    private final short[] indices;
    private final float[] vertices;
    private ShortBuffer   indexBuffer;
    private FloatBuffer   vertexBuffer;

    /**
     *
     * @param vertices
     * @param indices
     */
    Object(final float[] vertices, final short[] indices, float[] color) {
        this.color    = color;
        this.indices  = indices;
        this.vertices = vertices;

        this.init();
    }

    /**
     *
     */
    public void draw(final int shaderProgram, final float[] mvpMatrix) {
        GLES20.glUseProgram(shaderProgram);

        // MVP - Model View Projection Matrix
        int mvpUniform = GLES20.glGetUniformLocation(shaderProgram,"mvpMatrix");
        GLES20.glUniformMatrix4fv(mvpUniform, 1, false, mvpMatrix, 0);

        // Set the object vertex attrib array (position)
        int positionAttrib = GLES20.glGetAttribLocation(shaderProgram,"position");
        int vertexStride   = (Constants.COORDS_PER_VERTEX * Constants.BYTES_PER_FLOAT);

        GLES20.glEnableVertexAttribArray(positionAttrib);
        GLES20.glVertexAttribPointer(positionAttrib, Constants.COORDS_PER_VERTEX, GLES20.GL_FLOAT,false, vertexStride, this.vertexBuffer);

        // Set the object color
        int colorUniform = GLES20.glGetUniformLocation(shaderProgram,"color");
        GLES20.glUniform4fv(colorUniform, 1, this.color, 0);

        // Draw the object
        if (this.indexBuffer == null)
            GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, (this.vertices.length / Constants.COORDS_PER_VERTEX));
        else
            GLES20.glDrawElements(GLES20.GL_TRIANGLE_STRIP, this.indices.length, GLES20.GL_UNSIGNED_SHORT, this.indexBuffer);

        // Disable the object vertex attrib array (position)
        GLES20.glDisableVertexAttribArray(positionAttrib);
    }

    /**
     *
     */
    private void init() {
        if ((this.indices != null) && (this.indices.length > 0))
            this.indexBuffer = this.getShortBuffer(this.getByteOrder(ByteOrder.nativeOrder(), this.indices.length), this.indices);

        this.vertexBuffer = this.getFloatBuffer(this.getByteOrder(ByteOrder.nativeOrder(), this.vertices.length), this.vertices);
    }

    /**
     *
     * @param byteOrder
     * @param size
     * @return
     */
    private ByteBuffer getByteOrder(final ByteOrder byteOrder, final int size) {
        return ByteBuffer.allocateDirect(size * Constants.BYTES_PER_FLOAT).order(byteOrder);
    }

    /**
     *
     * @param byteBuffer
     * @param data
     * @return
     */
    private ShortBuffer getShortBuffer(final ByteBuffer byteBuffer, final short[] data) {
        if ((byteBuffer == null) || (data == null))
            return null;

        ShortBuffer buffer = byteBuffer.asShortBuffer();

        buffer.put(data);
        buffer.position(0);

        return buffer;
    }

    /**
     *
     * @param byteBuffer
     * @param data
     * @return
     */
    private FloatBuffer getFloatBuffer(final ByteBuffer byteBuffer, final float[] data) {
        if ((byteBuffer == null) || (data == null))
            return null;

        FloatBuffer buffer = byteBuffer.asFloatBuffer();

        buffer.put(data);
        buffer.position(0);

        return buffer;
    }

}
