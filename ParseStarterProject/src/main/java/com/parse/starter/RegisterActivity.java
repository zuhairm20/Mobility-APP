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

import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

public class RegisterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



        Button reg = (Button) findViewById(R.id.registerButton);
        reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ParseUser user = new ParseUser();

                EditText userName = (EditText) findViewById(R.id.facilityAddress);
                String name = userName.getText().toString();
                user.put("Name", name);

                EditText userEmail = (EditText) findViewById(R.id.user_email);
                String email = userEmail.getText().toString();
                user.setUsername(email);

                EditText userPW = (EditText) findViewById(R.id.password);
                String password = userPW.getText().toString();
                user.setPassword(password);

                user.signUpInBackground(new SignUpCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (e == null) {
                            showToast(getApplicationContext(), "Please confirm your email through the confirmation email in order to access all features of the application");
                            // Hooray! Let them use the app now.
                            Intent intent = new Intent(RegisterActivity.this, CreateProfileActivity.class);
                            startActivity(intent);
                        }
                        else {
                            Log.d("Zuhair", "" + e);
                            // Sign up didn't succeed. Look at the ParseException
                            // to figure out what went wrong
                        }
                    }
                });
            }
        });


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    protected static void showToast(Context con, CharSequence text) {
        Toast.makeText(con, text, Toast.LENGTH_SHORT).show();
    }

}
