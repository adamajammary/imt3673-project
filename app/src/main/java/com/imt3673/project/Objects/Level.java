package com.imt3673.project.Objects;


import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.util.Log;

import com.imt3673.project.utils.Vector2;

import java.util.ArrayList;

/**
 * A level in the game, consisting of blocks
 */
public class Level {
    private static final String TAG = Level.class.getName();

    private ArrayList<Block> blocks = new ArrayList<>();
    private float pixelSize;

    /**
     * Draws all blocks in level
     * @param canvas
     * @param cameraPosition
     */
    public void draw(Canvas canvas, Vector2 cameraPosition){
        for(Block block : blocks){
            block.draw(canvas, cameraPosition);
        }
    }

    /**
     * Gets all blocks in level
     * @return ArrayList<GameObject> blocks
     */
    public ArrayList<Block> getBlocks(){
        return blocks;
    }

    /**
     * Size of one pixel in bitmap in world
     */
    public float getPixelSize(){
        return pixelSize;
    }

    /**
     * Builds level from a bitmap
     * @param level bitmap to use
     * @param phoneWidth width of canvas
     * @param phoneHeight height of canvas
     */
    public void buildFromPNG(Bitmap level, int phoneWidth, int phoneHeight){
        Log.d(TAG, "BUILD LEVEL! Width: " + level.getWidth() + " Height: " + level.getHeight());
        float scaling = phoneHeight / level.getHeight();
        pixelSize = scaling;

        for (int x = 0; x < level.getWidth(); x++){
            for (int y = 0; y < level.getHeight(); y++){
                int clr = level.getPixel(x, y);
                if (clr == Block.TYPE_OBSTACLE){
                    createRect(level, x, y, Block.TYPE_OBSTACLE, scaling);
                } else if (clr == Block.TYPE_GOAL){
                    createRect(level, x, y, Block.TYPE_GOAL, scaling);
                }
            }
        }

        //blocks.add(new Block(new Vector2(), 100, 100, Color.BLACK));
    }

    /**
     * Creates the biggest possible rect starting at x,y of type type
     * @param level bitmap to use
     * @param x x coordinate of rect start
     * @param y y coordinate of rect start
     * @param type type of rect
     * @param scaling scaling to apply to rect
     */
    private void createRect(Bitmap level, int x, int y, int type, float scaling){
        int startX = x;
        int startY = y;

        int w = 0;
        int h = 0;
        int clr = type;
        while (clr == type && startX + w < level.getWidth()){ //Scan width of rect
            w++;
            if (startX + w >= level.getWidth()){
                break;
            }
            clr = level.getPixel(startX + w, startY);
        }

        boolean fullRow = true;
        while (fullRow) { //Scan height of rect
            h++;

            if (startY + h >= level.getHeight()){
                break;
            }

            for (x = startX; x < startX + w; x++){
                if (level.getPixel(x, startY + h) != type){
                    fullRow = false;
                }
            }
        }

        for (x = startX; x < startX + w; x++){ //Clear pixels of rectangle from bitmap
            for (y = startY; y < startY + h; y++){
                level.setPixel(x, y, Block.TYPE_CLEAR);
            }
        }

        Log.d(TAG, "Made block, width: " + w + " height: " + h + " pos: " + startX + "," + startY);

        Vector2 pos = new Vector2(startX * scaling, startY * scaling);
        Block block = new Block(pos, w * scaling, h * scaling, type);
        Log.d(TAG, "Scaled block: " + block.getRectangle().toShortString());
        blocks.add(block);
    }
}
