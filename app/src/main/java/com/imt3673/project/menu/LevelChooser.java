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

    private ListView levelListView;
    private ArrayList<String> levelsList = new ArrayList<>();
    private ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_level_chooser);

        levelsList.add("level1");
        levelsList.add("level2");

        levelListView = findViewById(R.id.lv_levels);
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, levelsList);
        levelListView.setAdapter(adapter);

        levelListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String levelName = levelsList.get(position);
                Intent intent = new Intent(view.getContext(), MainActivity.class);
                intent.putExtra("level", levelName);
                startActivity(intent);
            }
        });
    }
}
