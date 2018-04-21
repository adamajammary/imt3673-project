package com.imt3673.project.menu;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.imt3673.project.main.MainActivity;
import com.imt3673.project.main.R;

import java.util.ArrayList;

public class LevelChooser extends AppCompatActivity {

    private LevelChooserListAdapter listAdapter;
    private ListView levelListView;
    private ArrayList<LevelInfo> levels;
    

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_level_chooser);

        this.levels = new ArrayList<>();

        this.levels.add(new LevelInfo("Level 1","level1","00:20:00","00:40:00","01:00:00"));
        this.levels.add(new LevelInfo("Level 2","level2","00:20:00","00:40:00","01:00:00"));

        levelListView = findViewById(R.id.lv_levels);
        this.listAdapter = new LevelChooserListAdapter(this,this.levels);
        levelListView.setAdapter(this.listAdapter);
    }
}
