package com.parse.starter;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseRole;
import com.parse.ParseUser;
import com.parse.SaveCallback;

public class createAdmin extends AppCompatActivity {

    private EditText password;
    protected String pw;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_admin);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



        final ParseQuery<ParseRole> query = ParseRole.getQuery();
        query.whereEqualTo("name", "Admin");
        query.getInBackground("zueMT0hsH2", new GetCallback<ParseRole>() {
            @Override
            public void done(final ParseRole role, ParseException e) {
                if (e == null) {
                    ParseQuery<ParseUser> query1 = ParseQuery.getQuery("_User");
                    query1.getInBackground("zwFuONriFQ", new GetCallback<ParseUser>() {
                        @Override
                        public void done(ParseUser newAdmin, ParseException e) {
                            if (e == null){
                                role.getUsers().add(newAdmin);
                                role.saveInBackground(new SaveCallback() {
                                    @Override
                                    public void done(ParseException e) {
                                        if (e ==null){
                                            showToast(getApplicationContext(), "Successfully added user to admin role");
                                        }

                                        else{
                                            showToast(getApplicationContext(), "Failure to add user to role.");
                                        }
                                    }
                                });

                            }
                            else{
                                //didnt get user
                                showToast(getApplicationContext(), "User does not exist");
                            }
                        }
                    });

                } else {
                    //failure to retrieve role
                    showToast(getApplicationContext(), "Role Does Not Exist");
                }

            }
        });

         }

    protected static void showToast(Context con, CharSequence text) {
        Toast.makeText(con, text, Toast.LENGTH_SHORT).show();
    }

    }
