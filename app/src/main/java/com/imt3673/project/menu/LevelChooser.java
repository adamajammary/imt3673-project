package com.imt3673.project.menu;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.imt3673.project.database.AppDatabase;
import com.imt3673.project.database.HighScore;
import com.imt3673.project.main.MainActivity;
import com.imt3673.project.main.R;

import java.util.ArrayList;
import java.util.List;

public class LevelChooser extends AppCompatActivity {

    private LevelChooserListAdapter listAdapter;
    private ListView levelListView;
    private ArrayList<LevelInfo> levels;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_level_chooser);

     //  AppDatabase database = AppDatabase.getAppDatabase(this);

       //database.highScoreDao().deleteAll();


   //  HighScore scoren = new HighScore();
   //  scoren.setLevelName("level2");
   //  scoren.setLevelTime("000100");

   //   database.highScoreDao().insertAll(scoren);

    //   HighScore score_two = new HighScore();
    //   score.setLevelName("level1");
    //   score.setLevelTime(001000);
    //   database.highScoreDao().insertAll(score_two);

     //  List<HighScore> list = database.highScoreDao().getAllScoresFromLevelSorted("level1");

     //  for(HighScore score : list){

     //      Log.i("LEVELCHOOSER","name: " + score.getLevelName() + ". time: " + score.getLevelTime());
     //  }




        this.levels = new ArrayList<>();

        this.levels.add(new LevelInfo("Level 1","level1","00:20:00","00:40:00","01:00:00"));
        this.levels.add(new LevelInfo("Level 2","level2","00:20:00","00:40:00","01:00:00"));

        levelListView = findViewById(R.id.lv_levels);
        this.listAdapter = new LevelChooserListAdapter(this,this.levels, AppDatabase.getAppDatabase(this));
        levelListView.setAdapter(this.listAdapter);
    }
}
