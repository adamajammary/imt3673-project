package com.imt3673.project.database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.ArrayList;
import java.util.List;

/**
 * DAO: Contains the methods used for accessing the database.
 * Data Access Object
 */

@Dao
public interface HighScoreDao {

    @Query("SELECT * FROM high_score")
    List<HighScore> getAll();

    @Query("SELECT * FROM high_score WHERE level_name LIKE :levelName ORDER BY level_time ASC")
    List<HighScore> getAllScoresFromLevelSorted(String levelName);

    @Insert
    void insertAll(HighScore... highScores);

    @Delete
    void delete(HighScore highScores);

    @Query("DELETE FROM high_score")
    void deleteAll();
}
