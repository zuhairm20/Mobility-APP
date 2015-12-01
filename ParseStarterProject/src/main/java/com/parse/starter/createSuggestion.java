package com.parse.starter;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.ParseACL;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;

public class createSuggestion extends AppCompatActivity {

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_suggestion);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final Animation shake = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.shake);
        final EditText name = (EditText) findViewById(R.id.facilityName);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setBackgroundTintList(ColorStateList.valueOf(Color.rgb(205, 92, 92)));
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ParseObject suggestion = new ParseObject("Suggestion");
                if (name.getText().toString().isEmpty()){
                    showToast(getApplicationContext(),  "There must be a name");
                    name.startAnimation(shake);
                    return;
                }

                else{
                    suggestion.put("Name", name.getText().toString());
                    ParseACL acl = new ParseACL();
                    acl.setPublicReadAccess(true);
                    acl.setRoleWriteAccess("Admin", true);
                    suggestion.setACL(acl);
                    suggestion.put("createdBy", ParseUser.getCurrentUser());
                    suggestion.put("Status", "New");
                    suggestion.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(ParseException e) {
                            if (e == null){
                                showToast(getApplicationContext(), "You have successfully submitted a suggestion. Thank you. ");
                                Intent intent = new Intent(createSuggestion.this, viewFacilities.class);
                                startActivity(intent);
                            }
                        }
                    });
                }
                }

        });
    }


    public void onLoadingStart(boolean showSpinner, String message) {
        if (showSpinner) {
            if (!isFinishing()) {
                progressDialog = ProgressDialog.show(this, "Please wait",
                        message, true, true);
            }

        }
    }

    public void onLoadingFinish() {

        if (progressDialog != null) {

            if (!isFinishing()) {

                progressDialog.dismiss();
                progressDialog = null;
            }

        }
    }

    protected static void showToast(Context con, CharSequence text) {
        Toast.makeText(con, text, Toast.LENGTH_SHORT).show();
    }

}
