package com.imt3673.project.Objects;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PointF;
import android.util.Log;

import com.imt3673.project.main.R;
import com.imt3673.project.media.TextureSet;
import com.imt3673.project.utils.Vector2;

import java.util.ArrayList;

/**
 * A level in the game, consisting of blocks
 */
public class Level {
    private static final String TAG = Level.class.getName();

    private Block background;
    private ArrayList<Block> blocks = new ArrayList<>();
    private static float pixelSize;
    private Vector2 spawnPoint;
    private TextureSet textureSet;

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
     * Gets the spawn point for the level
     * @return Vector2 spawnPoint
     */
    public Vector2 getSpawnPoint(){
        return spawnPoint;
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
    public static float getPixelSize(){
        return pixelSize;
    }

    /**
     * Builds level from a bitmap
     * @param level bitmap to use
     * @param phoneHeight height of canvas
     */
    public void buildFromPNG(Bitmap level, int phoneHeight, Context context){
        Log.d(TAG, "BUILD LEVEL! Width: " + level.getWidth() + " Height: " + level.getHeight());
        float scaling = phoneHeight / level.getHeight();
        pixelSize = scaling;

        addBackground(context, new PointF(level.getWidth() * scaling, level.getHeight() * scaling));

        for (int x = 0; x < level.getWidth(); x++){
            for (int y = 0; y < level.getHeight(); y++){
                int clr = level.getPixel(x, y);
                if (clr == Block.TYPE_OBSTACLE) {
                    createRect(level, x, y, Block.TYPE_OBSTACLE, scaling, context);
                } else if(clr == Block.TYPE_GOAL){
                    createRect(level, x, y, Block.TYPE_GOAL, scaling, context);
                } else if (clr == Block.TYPE_SPAWN){
                    addSpawnPoint(level, x, y, scaling);
                }
            }
        }
    }

    /**
     * Creates the biggest possible rect starting at x,y of type type
     * @param level bitmap to use
     * @param x x coordinate of rect start
     * @param y y coordinate of rect start
     * @param type type of the block
     * @param scaling scaling to apply to rect
     */
    private void createRect(Bitmap level, int x, int y, int type, float scaling, Context context){
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
        addBlockTexture(block, type, context);
        Log.d(TAG, "Scaled block: " + block.getRectangle().toShortString());
        blocks.add(block);
    }

    /**
     * Adds texture to block
     * @param block block to texture
     * @param type type of the block
     * @param context context
     */
    private void addBlockTexture(Block block, int type, Context context) {
        if(type == Block.TYPE_OBSTACLE){
            block.setTexture(context, textureSet, TextureSet.WALL_TEX);
        }
        else if(type == Block.TYPE_GOAL){
            block.setTexture(context, textureSet, TextureSet.GOAL_TEX);
        }
    }

    /**
     * Adds a spawn point
     * @param level Bitmap of level
     * @param x coordinate
     * @param y coordinate
     * @param scaling scaling for level
     */
    private void addSpawnPoint(Bitmap level, int x, int y, float scaling){
        spawnPoint = new Vector2(x * scaling, y * scaling);
        level.setPixel(x, y, Block.TYPE_CLEAR);
    }

    /**
     * Set up the background block
     */
    private void addBackground(Context context, PointF levelDims){
        background = new Block(Vector2.zero, levelDims.x, levelDims.y, Block.TYPE_CLEAR);
        background.setTexture(context, textureSet, TextureSet.FLOOR_TEX);
    }

    /**
     * Set the texture set
     * @param textureSet texture set
     */
    public void setTextureSet(TextureSet textureSet) {
        this.textureSet = textureSet;
    }

    public Block getBackground() {
        return background;
    }
}
