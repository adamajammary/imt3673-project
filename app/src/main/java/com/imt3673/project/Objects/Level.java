package com.imt3673.project.Objects;


import android.graphics.Bitmap;
import android.graphics.Color;
import android.util.Log;

import com.imt3673.project.Objects.GameObject;

import java.util.ArrayList;

public class Level {
    private static final String TAG = Level.class.getName();
    private ArrayList<GameObject> obstacles = new ArrayList<>();
    private int width;
    private int height;
    private Bitmap levelMap;

    public void buildFromPNG(Bitmap level, int phoneWidth, int phoneHeight){
        Log.d(TAG, "BUILD LEVEL! Width: " + level.getWidth() + " Height: " + level.getHeight());
        levelMap = level;
        for (int x = 0; x < level.getWidth(); x++){
            for (int y = 0; y < level.getHeight(); y++){
                int clr = level.getPixel(x, y);
                Log.d(TAG, "" + clr);
            }
        }
    }
}
