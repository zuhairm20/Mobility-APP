package com.parse.starter;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;

import com.parse.ParseUser;

public class CreateProfileActivity extends AppCompatActivity {

    EditText phoneNumEdit;
    EditText ageEdit;
    EditText disabilityEdit;
    EditText nameEdit;

    ParseUser user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_profile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

         user = ParseUser.getCurrentUser();

         phoneNumEdit = (EditText) findViewById(R.id.phoneNumEdit);
         ageEdit = (EditText) findViewById(R.id.ageEdit);
         disabilityEdit = (EditText) findViewById(R.id.disabilityEdit);
         nameEdit = (EditText) findViewById(R.id.nameEdit);


        FloatingActionButton saveButton = (FloatingActionButton) findViewById(R.id.fab);

        saveButton.setBackgroundTintList(ColorStateList.valueOf(Color.rgb(229, 204, 255)));
        saveButton.setRippleColor(Color.rgb(220, 189, 250));
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String phoneNumber = phoneNumEdit.getText().toString();
                String age = ageEdit.getText().toString();
                String disability = disabilityEdit.getText().toString();
                String name = nameEdit.getText().toString();

            }
        });




    }

}
