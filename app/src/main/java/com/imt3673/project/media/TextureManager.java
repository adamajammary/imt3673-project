package com.imt3673.project.media;

import android.content.Context;
import android.graphics.BitmapFactory;

import com.imt3673.project.main.R;

import java.util.HashMap;

public class TextureManager {
    private static HashMap<String, TextureSet> textureSets;


    /**
     * Initializes the texture manager and adds in the default texture set
     * @param context context
     */
    public static void init(Context context){
        if(textureSets != null) // To make sure we don't try to re-init
            return;

        textureSets = new HashMap<>();
        textureSets.put("default", new TextureSet(
                BitmapFactory.decodeResource(context.getResources(), R.drawable.ball2),
                BitmapFactory.decodeResource(context.getResources(), R.drawable.ground_04_light),
                BitmapFactory.decodeResource(context.getResources(), R.drawable.wall),
                BitmapFactory.decodeResource(context.getResources(), R.drawable.goal),
                BitmapFactory.decodeResource(context.getResources(), R.drawable.coin),
                BitmapFactory.decodeResource(context.getResources(), R.drawable.crate),
                BitmapFactory.decodeResource(context.getResources(), R.drawable.crate_damaged),
                BitmapFactory.decodeResource(context.getResources(), R.drawable.portal),
                BitmapFactory.decodeResource(context.getResources(), R.drawable.hole)
                ));
    }

    /**
     * Tries to get a texture set
     * @param setID key of texture set
     * @return the texture set
     */
    public static TextureSet getTextureSet(String setID){
        if(textureSets.containsKey(setID)){
            return textureSets.get(setID);
        } else {
            throw new IllegalArgumentException("TextureManager.textureSets does not contain an item with key \"" + setID + "\"");
        }
    }

    /**
     * Adds a texture set
     * @param set the set to add
     * @param key the key to use
     * @return whether the set was added
     */
    public static boolean addTextureSet(TextureSet set, String key){
        if(textureSets.containsKey(key))
            return false;

        textureSets.put(key, set);
        return true;
    }

}
