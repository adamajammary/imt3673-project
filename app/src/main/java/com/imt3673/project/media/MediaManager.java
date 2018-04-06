package com.imt3673.project.media;

import android.content.Context;
import android.media.MediaPlayer;
import android.media.SoundPool;

/**
 *
 */
public class MediaManager {

    private final Context   context;
    private MediaPlayer     mediaPlayer;
    private final SoundPool soundPool;

    /**
     *
     * @param context
     */
    public MediaManager(final Context context) {
        this.context   = context;
        this.soundPool = new SoundPool.Builder().setMaxStreams(2).build();
    }

    /**
     *
     * @param resourceID
     * @param mediaType
     */
    public void loadResource(final int resourceID, final int mediaType) {
        switch (mediaType) {
            case Constants.MEDIA_TYPE_MUSIC:
                if (this.mediaPlayer != null) {
                    this.mediaPlayer.release();
                    this.mediaPlayer = null;
                }

                this.mediaPlayer = MediaPlayer.create(this.context, resourceID);
                break;
            case Constants.MEDIA_TYPE_SOUND:
                this.soundPool.load(this.context, resourceID, 1);
                break;
        }
    }

    /**
     *
     * @param resourceID
     * @param mediaType
     */
    public void pause(final int resourceID, final int mediaType) {
        switch (mediaType) {
            case Constants.MEDIA_TYPE_MUSIC:
                this.pauseMusic();
                break;
            case Constants.MEDIA_TYPE_SOUND:
                this.pauseSound(resourceID);
                break;
        }
    }

    /**
     *
     * @param resourceID
     * @param mediaType
     */
    public void play(final int resourceID, final int mediaType) {
        switch (mediaType) {
            case Constants.MEDIA_TYPE_MUSIC:
                this.playMusic();
                break;
            case Constants.MEDIA_TYPE_SOUND:
                this.playSound(resourceID);
                break;
        }
    }

    /**
     *
     * @param resourceID
     * @param mediaType
     */
    public void stop(final int resourceID, final int mediaType) {
        switch (mediaType) {
            case Constants.MEDIA_TYPE_MUSIC:
                this.stopMusic();
                break;
            case Constants.MEDIA_TYPE_SOUND:
                this.stopSound(resourceID);
                break;
        }
    }

    /**
     * Pauses the current music file.
     */
    private void pauseMusic() {
        this.mediaPlayer.pause();
    }

    /**
     * Plays the current music file.
     */
    private void playMusic() {
        if (this.mediaPlayer.isPlaying())
            this.mediaPlayer.stop();

        this.mediaPlayer.start();
    }

    /**
     * Stops the current music file.
     */
    private void stopMusic() {
        this.mediaPlayer.stop();

        this.mediaPlayer.release();
        this.mediaPlayer = null;
    }

    /**
     * Pauses the selected sound effect.
     * @param resourceID Resource ID
     */
    private void pauseSound(final int resourceID) {
        this.soundPool.stop(resourceID);
    }

    /**
     * Plays the selected sound effect.
     * @param resourceID Resource ID
     */
    private void playSound(final int resourceID) {
        this.soundPool.stop(resourceID);
        this.soundPool.play(resourceID, 1.0f, 1.0f, 1, 0, 1.0f);
    }

    /**
     * Stops the selected sound effect.
     * @param resourceID Resource ID
     */
    private void stopSound(final int resourceID) {
        this.soundPool.stop(resourceID);
    }

}