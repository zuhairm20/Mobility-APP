package com.parse.starter;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

public class myActivities extends AppCompatActivity {

    private Button viewReports;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_activities);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final Button viewReports = (Button) findViewById(R.id.viewReports);

        viewReports.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(myActivities.this, ownReports.class);
                startActivity(intent);
            }
        });

        final Button viewEvents = (Button) findViewById(R.id.viewEvents);
        viewEvents.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(myActivities.this, ownEvents.class);
                startActivity(intent);
            }
        });


    }

}
