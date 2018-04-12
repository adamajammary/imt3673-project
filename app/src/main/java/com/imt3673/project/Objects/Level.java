package com.imt3673.project.Objects;


import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.Log;

import com.imt3673.project.utils.Vector2;

import java.util.ArrayList;

public class Level {
    private static final String TAG = Level.class.getName();

    //Color meanings:
    private static final int CLEAR = Color.WHITE;
    private static final int WALL = Color.BLACK;
    private static final int FINISH = Color.GREEN;

    private ArrayList<GameObject> blocks = new ArrayList<>();

    public void draw(Canvas canvas){
        for(GameObject block : blocks){
            block.draw(canvas);
        }
    }

    public void buildFromPNG(Bitmap level, int phoneWidth, int phoneHeight){
        Log.d(TAG, "BUILD LEVEL! Width: " + level.getWidth() + " Height: " + level.getHeight());
        float scaling = phoneHeight / level.getHeight();

        for (int x = 0; x < level.getWidth(); x++){
            for (int y = 0; y < level.getHeight(); y++){
                int clr = level.getPixel(x, y);
                if (clr == WALL){
                    createRect(level, x, y, WALL, scaling);
                } else if (clr == FINISH){
                    createRect(level, x, y, FINISH, scaling);
                }
            }
        }
    }

    private void createRect(Bitmap level, int x, int y, int type, float scaling){
        int startX = x;
        int startY = y;

        int w = 0;
        int h = 0;
        int clr = type;
        while (clr == type){ //Scan width of rect
            level.setPixel(startX + w, startY, CLEAR);
            w++;
            if (startX + w < level.getWidth()) {
                clr = level.getPixel(startX + w, startY);
            } else {
                break;
            }
        }

        boolean fullRow = true;
        while (fullRow) { //Scan height of rect
            for (x = startX; x < w; x++){
                if (level.getPixel(x, startY + h) != type){
                    fullRow = false;
                }
            }
            h++;
            if (startY + h < level.getHeight()) {
                if (fullRow) {
                    for (x = startX; x < w; x++) { //Clear row
                        level.setPixel(x, startY + h, CLEAR);
                    }
                }
            } else {
                break;
            }
        }

        int blockType;
        if (type == FINISH){
            blockType = Block.TYPE_GOAL;
        }else {
            blockType = Block.TYPE_OBSTACLE;
        }
        Vector2 pos = new Vector2(startX * scaling, startY * scaling);
        Block block = new Block(pos, w * scaling, h * scaling, blockType, type);
        blocks.add(block);
    }
}
