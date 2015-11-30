package com.parse.starter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

public class SignInActivity extends AppCompatActivity {

    private EditText userName;
    private EditText password;
    private Button login;
    private Button reg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ParseUser currentUser = ParseUser.getCurrentUser();
        if (currentUser != null) {
            Intent intent = new Intent(SignInActivity.this, myActivities.class);
            startActivity(intent);
            // do stuff with the user
        }
        else {
            setContentView(R.layout.activity_sign_in);
            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
            userName = (EditText) findViewById(R.id.user_email);
             password = (EditText) findViewById(R.id.password);
            login = (Button) findViewById(R.id.loginButton);
             reg = (Button) findViewById(R.id.registerButton);

            login.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String username = userName.getText().toString();
                    String pw = password.getText().toString();
                    userLogin(username, pw);
                }
            });


            reg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(SignInActivity.this, RegisterActivity.class);
                    startActivity(intent);
                }
            });
        }
    }

    protected void userLogin(String username, String pw){
        ParseUser.logInInBackground(username, pw, new LogInCallback() {
            @Override
            public void done(ParseUser user, ParseException e) {
                if (user != null) {
                    showToast(getApplicationContext(), "YAY LOGGED IN!");
                    Intent intent = new Intent(SignInActivity.this, fileReport.class);
                    startActivity(intent);
                    // Hooray! The user is logged in.
                } else {
                    Log.d("Zuhair", "" + e);
                    // Sign in failed. Look at the ParseException to see what happened.
                }
            }
        });
    }


    protected static void showToast(Context con, CharSequence text) {
        Toast.makeText(con, text, Toast.LENGTH_SHORT).show();
    }




}
