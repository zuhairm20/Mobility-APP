package com.parse.starter;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.TextView;

import com.parse.GetCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseQuery;
import com.parse.ParseUser;

public class viewUserProfile extends AppCompatActivity {


    private ProgressDialog progressDialog;
    private ImageView profilePic;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_user_profile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final TextView userName = (TextView) findViewById(R.id.userName);
        final TextView aboutMe = (TextView) findViewById(R.id.aboutMe);

        final TextView userGender = (TextView) findViewById(R.id.userGender);
        final ImageView profilePic = (ImageView) findViewById(R.id.profilePicture);


        Intent intent = this.getIntent();
        if (intent.getExtras() != null) {
            final ParseQuery<ParseUser> query = ParseQuery.getQuery("_User");
            query.getInBackground(intent.getStringExtra("userId"), new GetCallback<ParseUser>() {
                @Override
                public void done(ParseUser user, ParseException e) {
                    if (e == null) {
                        //handle retrieval and set values
                        if (user.has("Name")) {
                            userName.setText(user.get("Name").toString());

                        }

                        if (user.has("Gender")) {
                            userGender.setText(user.get("Gender").toString());
                        }

                        if (user.has("profilePicture")) {
                            loadImages(user.getParseFile("profilePicture"), profilePic);
                        }
                        if (user.has("aboutMe")) {
                            aboutMe.setText(user.get("aboutMe").toString());
                        }
                    } else {
                        ///handle error
                    }
                }
            });


        }
    }

    private void loadImages(ParseFile thumbnail, final ImageView img) {

        if (thumbnail != null) {
            thumbnail.getDataInBackground(new GetDataCallback() {
                @Override
                public void done(byte[] data, ParseException e) {
                    if (e == null) {
                        Bitmap bmp = BitmapFactory.decodeByteArray(data, 0, data.length);
                        img.setImageBitmap(bmp);
                    }
                    else {
                    }
                }
            });
        }

        else {
            //img.setImageResource(R.drawable.menu);
        }
    }


}
