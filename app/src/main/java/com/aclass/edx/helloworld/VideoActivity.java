package com.aclass.edx.helloworld;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class VideoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);

        Intent intent = getIntent();
        String selectedFilename = intent.getStringExtra(MainActivity.SELECTED_FILENAME);

        TextView filename = (TextView) findViewById(R.id.filename);
        filename.setText(selectedFilename);
    }
}
