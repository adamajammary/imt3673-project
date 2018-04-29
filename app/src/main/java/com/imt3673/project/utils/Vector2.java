package com.imt3673.project.utils;

import android.graphics.RectF;

import java.util.Locale;

/**
 * Class for a 2d Vector
 */
public class Vector2 {
    public static Vector2 zero = new Vector2(0,0);
    public float x;
    public float y;

    /**
     * Constructor taking x and y value
     */
    public Vector2(){
        x = 0;
        y = 0;
    }

    /**
     * Constructor taking x and y value
     * @param x value for vector
     * @param y value for vector
     */
    public Vector2(float x, float y){
        this.x = x;
        this.y = y;
    }

    /**
     * Makes a copy of a vector
     * @param copy vector to copy
     */
    public Vector2(Vector2 copy){
        x = copy.x;
        y = copy.y;
    }

    /**
     * Gets the value for the axis
     * @param axis axis to get
     * @return x for axis == 0, y for axis == 1;
     */
    public float getAxis(int axis){
        switch (axis){
            case 0:
                return x;
            case 1:
                return y;
            default:
                return 999999;
        }
    }

    /**
     * sets the value of an axis
     * @param axis axis to set
     * @param value value to set
     */
    public void setAxis(int axis, float value){
        switch (axis){
            case 0:
                x = value;
                break;
            case 1:
                y = value;
                break;
        }
    }

    /**
     * Calculates the length of the vector
     * @return float length
     */
    public float magnitude(){
        return (float)Math.sqrt(x*x + y*y);
    }

    /**
     * Calculates the squared length of the vector (cheaper then normal length)
     * @return float squared length
     */
    public float magnitudeSquared(){
        return x*x + y*y;
    }

    /**
     * Calculates the normalized vector
     * @return Vector2 normalized vector
     */
    public Vector2 normalized(){
        return Vector2.mult(this, 1 / magnitude());
    }


    public static float distance(Vector2 v1, Vector2 v2) {
        return Vector2.subtract(v1, v2).magnitude();
    }

    /**
     * Adds two vectors together
     * @param first The first Vector2
     * @param second The second Vector2
     * @return Vector2 result
     */
    public static Vector2 add(Vector2 first, Vector2 second){
        return new Vector2(first.x + second.x, first.y + second.y);
    }

    /**
     * Subtracts in this manne: first - second
     * @param first The first Vector2
     * @param second The second Vector2
     * @return Vector2 result
     */
    public static Vector2 subtract(Vector2 first, Vector2 second){
        return new Vector2(first.x - second.x, first.y - second.y);
    }

    /**
     * Multiplies the vector with a float
     * @param first Vector2
     * @param number float
     * @return Vector2 * float
     */
    public static Vector2 mult(Vector2 first, float number){
        return new Vector2(first.x * number, first.y * number);
    }

    /**
     * Adds a vector and a rect
     * @param vector Vector to add
     * @param rectf Rect to add
     * @return RectF result
     */
    public static RectF add(Vector2 vector, RectF rectf){
        return new RectF(
                rectf.left +  vector.x,
                rectf.top +  vector.y,
                rectf.right +  vector.x,
                rectf.bottom +  vector.y
        );
    }


    /**
     * Subtracts a vector and a rect
     * @param vector Vector to add
     * @param rectf Rect to add
     * @return RectF result
     */
    public static RectF subtract(Vector2 vector, RectF rectf){
        return new RectF(
                rectf.left -  vector.x,
                rectf.top -  vector.y,
                rectf.right -  vector.x,
                rectf.bottom -  vector.y
        );
    }


    @Override
    public String toString(){
        return String.format(new Locale("NO"),"(%f, %f)", x, y);
    }
}
