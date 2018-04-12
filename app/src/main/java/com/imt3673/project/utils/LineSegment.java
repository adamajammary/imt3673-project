package com.imt3673.project.utils;

/**
 * LineSegment made of two Vector2
 */
public class LineSegment {
    public Vector2 a;
    public Vector2 b;

    /**
     * Constructs a line using provided points
     * @param b
     * @param a
     */
    public LineSegment(Vector2 b, Vector2 a){
        this.a = a;
        this.b = b;
    }
}
