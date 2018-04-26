package com.imt3673.project.database;


import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.util.Log;

/**
 * The application database.
 * https://medium.com/@ajaysaini.official/building-database-with-room-persistence-library-ecf7d0b8f3e9
 */
@Database(entities = {HighScore.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase{

    private static  AppDatabase INSTANCE;

    public abstract HighScoreDao highScoreDao();

    public static AppDatabase getAppDatabase(Context context){
        if (INSTANCE == null){
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(),AppDatabase.class,"high_score_database")
                    .allowMainThreadQueries() // TODO fix
                    .build();
            Log.i("AppDatabase","creating inctance");
        }
        return INSTANCE;
    }

    public  static  void destroyInstance(){
        INSTANCE = null;
    }
}
