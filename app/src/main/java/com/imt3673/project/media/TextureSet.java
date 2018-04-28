package com.imt3673.project.media;

import android.graphics.Bitmap;

/**
 * Texture sets contain the textures neccessary for one map
 */
public class TextureSet {
    // array containing all the textures
    private Bitmap[] textures;

    // index constants for all texture types:
    public final static int BALL_TEX = 0;
    public final static int FLOOR_TEX = 1;
    public final static int WALL_TEX = 2;
    public final static int GOAL_TEX = 3;
    public final static int BREAKABLE_WALL_TEX = 4;
    public final static int WALL_FRAGMENT_1_TEX = 5;
    public final static int WALL_FRAGMENT_2_TEX = 6;
    public final static int WALL_FRAGMENT_3_TEX = 7;
    public final static int WALL_FRAGMENT_4_TEX = 8;

    public TextureSet(Bitmap ballTex, Bitmap floorTex, Bitmap wallTex, Bitmap goalTex){
        textures = new Bitmap[]{
                ballTex,
                floorTex,
                wallTex,
                goalTex
        };
    }


    public Bitmap getTexture(int textureType) {
        return textures[textureType];
    }
}
