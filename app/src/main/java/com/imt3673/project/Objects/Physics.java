package com.imt3673.project.Objects;

import android.graphics.LinearGradient;
import android.graphics.RectF;

import com.imt3673.project.utils.LineSegment;
import com.imt3673.project.utils.Vector2;

import java.util.ArrayList;

/**
 * Created by Muffinz on 12/04/2018.
 */

public class Physics {

    /**
     * Circle/Rectangle collision.
     * Based on answer by e.James in thread:
     * https://stackoverflow.com/questions/401847/circle-rectangle-collision-detection-intersection
     * @param ball
     * @param block
     * @return true if collision
     */
    public static boolean BallBlockCollision(Ball ball, Block block){
        Vector2 circle = ball.getPosition();
        RectF rect = block.getRectangle();
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

    /**
     * Calculates collision between a LineSegment and a block
     * Based on answer by user37968 in thread:
     * https://stackoverflow.com/questions/99353/how-to-test-if-a-line-segment-intersects-an-axis-aligned-rectange-in-2d
     * @param line
     * @param block
     * @return true if collision
     */
    public static boolean LineSegmentBlockCollision(LineSegment line, Block block) {
        Vector2[] corners = block.getCorners();
        int total = 0;
        int maxTotal = corners.length;
        for (Vector2 corner : corners){
            if (line.inBound(corner.x, corner.y)){
                total += Math.signum(line.implicitEquation(corner.x, corner.y));
            } else {
                maxTotal--;
            }
        }
        return total != maxTotal;
    }
}