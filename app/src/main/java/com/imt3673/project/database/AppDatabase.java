package com.imt3673.project.database;


import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.util.Log;

@Database(entities = {HighScore.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase{
    String TAG = getClass().getName();

    private static  AppDatabase INSTANCE;

    public abstract HighScoreDao highScoreDao();

    public static AppDatabase getAppDatabase(Context context){
        if (INSTANCE == null){
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(),AppDatabase.class,"high_score_database")
                    .allowMainThreadQueries() // TODO fix
                    .build();
            Log.i("Appdatabase","creating inctance");
        }
        return INSTANCE;
    }

    public  static  void destroyInstance(){
        INSTANCE = null;
    }
}
