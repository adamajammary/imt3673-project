package com.imt3673.project.database;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

/**
 * Database table
 * Entity: Represents a table within the database.
 */

@Entity(tableName = "high_score")
public class HighScore {
    @PrimaryKey(autoGenerate = true)
    private int uid;

    @ColumnInfo(name = "level_name")
    private String levelName;

    @ColumnInfo(name = "level_time")
    private String levelTime;

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public String getLevelName() {
        return levelName;
    }

    public void setLevelName(String levelName) {
        this.levelName = levelName;
    }

    public String getLevelTime() {
        return levelTime;
    }

    public void setLevelTime(String levelTime) {
        this.levelTime = levelTime;
    }
}
