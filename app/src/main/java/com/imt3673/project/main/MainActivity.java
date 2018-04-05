package com.imt3673.project.main;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.imt3673.project.graphics.GLView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(new GLView(this));
    }

}
