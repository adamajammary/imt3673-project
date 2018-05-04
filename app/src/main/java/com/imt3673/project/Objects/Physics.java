package com.imt3673.project.Objects;

import android.graphics.RectF;

import com.imt3673.project.utils.Vector2;

/**
 * Created by Muffinz on 12/04/2018.
 */
public class Physics {

    /**
     * Circle/Rectangle collision.
     * Based on answer by e.James in thread:
     * https://stackoverflow.com/questions/401847/circle-rectangle-collision-detection-intersection
     * @param ball ball
     * @param rect rect
     * @return true if collision
     */
    public static boolean BallBlockCollision(Ball ball, RectF rect){
        Vector2 circle = ball.getPosition();
        Vector2 circleDistance = new Vector2();

        circleDistance.x = Math.abs(circle.x - rect.centerX());
        circleDistance.y = Math.abs(circle.y - rect.centerY());

        if (circleDistance.x > (rect.width()/2 + ball.getRadius())) { return false; }
        if (circleDistance.y > (rect.height()/2 + ball.getRadius())) { return false; }

        if (circleDistance.x <= (rect.width()/2)) { return true; }
        if (circleDistance.y <= (rect.height()/2)) { return true; }

        float cornerDistance_sq = (float)Math.pow(circleDistance.x - rect.width()/2, 2) +
                (float)Math.pow(circleDistance.y - rect.height()/2,2 );

        return (cornerDistance_sq <= (Math.pow(ball.getRadius(), 2)));
    }
}
