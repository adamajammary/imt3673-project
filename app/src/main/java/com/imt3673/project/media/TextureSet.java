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
    public final static int COIN_TEX = 4;
    public final static int CRATE_TEX = 5;
    public final static int CRATEDAMAGED_TEX = 6;
    public final static int PORTAL_TEX = 7;
    public final static int HOLE_TEX = 8;

    public TextureSet(Bitmap ballTex, Bitmap floorTex, Bitmap wallTex, Bitmap goalTex,
                      Bitmap coin, Bitmap crate, Bitmap damagedCrate, Bitmap portal,
                      Bitmap hole){
        textures = new Bitmap[]{
                ballTex,
                floorTex,
                wallTex,
                goalTex,
                coin,
                crate,
                damagedCrate,
                portal,
                hole
        };
    }


    public Bitmap getTexture(int textureType) {
        return textures[textureType];
    }
}
