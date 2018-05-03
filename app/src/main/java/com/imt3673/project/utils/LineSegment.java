package com.imt3673.project.utils;

/**
 * LineSegment made of two Vector2
 */
public class LineSegment {
    public Vector2 a;
    public Vector2 b;

    private float minX;
    private float minY;
    private float maxX;
    private float maxY;

    /**
     * Constructs a line using provided points
     * @param a start of line
     * @param b end of line
     */
    public LineSegment(Vector2 a, Vector2 b){
        this.a = a;
        this.b = b;

        minX = (a.x < b.x)  ? a.x : b.x;
        maxX = (a.x >= b.x) ? a.x : b.x;
        minY = (a.y < b.y)  ? a.y : b.y;
        maxY = (a.y >= b.y) ? a.y : b.y;
    }

    /**
     * Implicit equation for this line.
     * @param x x
     * @param y y
     * @return F(x, y)
     */
    public float implicitEquation(float x, float y){
        return (b.y-a.y)*x + (a.x-b.x)*y + (b.x*a.y-a.x*b.y);
    }

    /**
     * Checks if the point is within the bounds of the line
     * @param x x
     * @param y y
     * @return true if inbound
     */
    public boolean inBound(float x, float y){
        return (x > minX && x < maxX && y > minY && y < maxY);
    }
}
