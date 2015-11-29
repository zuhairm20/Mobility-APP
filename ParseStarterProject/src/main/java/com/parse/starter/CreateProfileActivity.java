package com.parse.starter;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class CreateProfileActivity extends AppCompatActivity {



    CheckBox female;
    CheckBox male;

    private ProgressDialog progressDialog;
    ParseUser user;

    private EditText phoneNumEdit;
    private EditText ageEdit;
    private EditText disabilityEdit;
    private EditText nameEdit;
    private EditText aboutEdit;

    protected ImageView mImageView;
    protected ParseFile saveImageFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_profile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mImageView = (ImageView) findViewById(R.id.userImage);
        mImageView.setImageResource(R.drawable.defaultuser);

        female = (CheckBox) findViewById(R.id.checkbox_female);
        male = (CheckBox) findViewById(R.id.checkbox_male);

        phoneNumEdit = (EditText) findViewById(R.id.phoneNumEdit);

        ageEdit = (EditText) findViewById(R.id.ageEdit);

        disabilityEdit = (EditText) findViewById(R.id.disabilityEdit);

        nameEdit = (EditText) findViewById(R.id.nameEdit);

        aboutEdit = (EditText) findViewById(R.id.aboutEdit);
        user = ParseUser.getCurrentUser();

        final ParseQuery<ParseUser> query = ParseQuery.getQuery("_User");
        onLoadingStart(true, "Retrieving User Details...");
        query.getInBackground(user.getObjectId(), new GetCallback<ParseUser>() {
            @Override
            public void done(ParseUser curr, ParseException e) {
                if (e == null) {
                    onLoadingFinish();
                    user = curr;
                    if (!curr.get("Name").toString().isEmpty()) {
                        nameEdit.setText(curr.get("Name").toString());
                    }


                    if (curr.has("Age"))
                    {
                        ageEdit.setText(curr.get("Age").toString());
                    }

                    if (curr.has("aboutMe")){
                        aboutEdit.setText(curr.get("aboutMe").toString());
                    }

                    if (curr.has("phoneNumber"))
                    {
                        phoneNumEdit.setText(curr.get("phoneNumber").toString());
                    }

                    if (curr.has("Disability"))
                    {
                        disabilityEdit.setText(curr.get("Disability").toString());
                    }

                    if (curr.has("Gender")) {
                        if (curr.getString("Gender").equals("F")) {
                            female.setChecked(true);
                        } else {
                            male.setChecked(true);
                        }
                    }
                }

                else{
                    //error code
                    onLoadingFinish();
                    showToast(getApplicationContext(), "This user does not exist");
                }
            }
        });

        Button mPhoto = (Button) findViewById(R.id.choosePic);

        mPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });


        FloatingActionButton saveButton = (FloatingActionButton) findViewById(R.id.fab);

        saveButton.setBackgroundTintList(ColorStateList.valueOf(Color.rgb(229, 204, 255)));
        saveButton.setRippleColor(Color.rgb(220, 189, 250));

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    saveUser();
            }
        });
    }




    private void selectImage() {

        final CharSequence[] options = { "Take Photo", "Choose from Gallery","Cancel" };

        AlertDialog.Builder builder = new AlertDialog.Builder(CreateProfileActivity.this);
        builder.setTitle("Add Photo!");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals("Take Photo"))
                {

                    Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                        startActivityForResult(takePictureIntent, 1);
                    }
                }
                else if (options[item].equals("Choose from Gallery"))
                {
                    Intent intent = new   Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
                    startActivityForResult(intent, 2);

                }
                else if (options[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == 1) {

                Bundle extras = data.getExtras();
                Bitmap bitmap = (Bitmap) extras.get("data");
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                mImageView.setImageBitmap(bitmap);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                byte[] b = stream.toByteArray();
                saveImageFile = new ParseFile("profilePicture.jpg", b);
                //selectPhoto.setText("taskPicture.jpg");
                //taskPic.setParseFile(saveImageFile);
                //taskPic.loadInBackground(new GetDataCallback() {
                //                  @Override
                //                public void done(byte[] data, ParseException e) {
                //                  if (e == null) {
                //                    onLoadingFinish();
//
                //                          Log.d("Zuhair", "Got image");
                //                    }
                //                  else{
                //                    Log.d("Zuhair", "" + e.getMessage());
                //              }
                //        }
                //  });

                if (bitmap !=null) {
                    Log.d("Zuhair", "not null");
                }


            }

            else if (requestCode == 2) {

                Uri selectedImage = data.getData();

                try{
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                    byte[] b = stream.toByteArray();
                    mImageView.setImageBitmap(bitmap);
                    saveImageFile = new ParseFile("profilePicture.jpg", b);
                }
                catch (FileNotFoundException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

            }
        }
    }


    protected void saveUser(){

        String phoneNumber = phoneNumEdit.getText().toString();
        String age = ageEdit.getText().toString();
        String disability = disabilityEdit.getText().toString();
        String name = nameEdit.getText().toString();
        String aboutMe = aboutEdit.getText().toString();
        boolean save = true;

        Animation shake = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.shake);

        if (name.isEmpty()) {
            save = false;
            showToast(getApplicationContext(),  "There must be a name");
            nameEdit.startAnimation(shake);
            return;
        }

        if (age.isEmpty()){
                save = false;
                showToast(getApplicationContext(),  "There must be an age");
                ageEdit.startAnimation(shake);
                return;
        }

        if (phoneNumber.isEmpty()) {
            save = false;
            showToast(getApplicationContext(),  "There must be a Phone Number");
            phoneNumEdit.startAnimation(shake);
            return;
        }

        if (disability.isEmpty()) {
            save = false;
            showToast(getApplicationContext(),  "There must be a disability entered. If you do not have one, please let us know.");
            disabilityEdit.startAnimation(shake);
            return;
        }


        if ((!male.isChecked()) && (!female.isChecked())) {
            save = false;
            male.startAnimation(shake);
            female.startAnimation(shake);
            showToast(getApplicationContext(), "You must select a gender");

        }

        if ((male.isChecked()) && (female.isChecked())) {
            save = false;
            male.startAnimation(shake);
            female.startAnimation(shake);
            showToast(getApplicationContext(), "You must select only one gender");

        }

        else if (save){
            onLoadingStart(true, "");
            user.put("Name", name);
            user.put("Age", age);
            user.put("phoneNumber", phoneNumber);
            user.put("Disability", disability);

            if (!aboutMe.isEmpty()){
                user.put("aboutMe", aboutMe);
            }

            if (male.isChecked()){
                user.put("Gender", "M");

            }

            if (female.isChecked()){
                user.put("Gender", "F");

            }

            user.saveInBackground(new SaveCallback() {
                @Override
                public void done(ParseException e) {
                    if (e == null){
                        //success
                        onLoadingFinish();
                        showToast(getApplicationContext(), "You have successfully edited your profile details!");
                      //  Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        //        .setAction("Action", null).show();
                    }

                    else
                    {
                        onLoadingFinish();
                        //failure scenario

                    }
                }
            });
        }


    }

    protected static void showToast(Context con, CharSequence text) {
        Toast.makeText(con, text, Toast.LENGTH_SHORT).show();
    }

    public void onCheckboxClicked(View view) {
        // Is the view now checked?
        boolean checked = ((CheckBox) view).isChecked();
        if (checked) {

        }

    }


    public void onLoadingStart(boolean showSpinner, String message) {
        if (showSpinner) {
            if (!isFinishing()) {
                progressDialog = ProgressDialog.show(this, "Please wait",
                        message, true, true);
            }

        }
    }


    public void hideKeyboard(View view) {
        InputMethodManager inputManager = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }

    public void onLoadingFinish() {

        if (progressDialog != null) {

            if (!isFinishing()) {

                progressDialog.dismiss();
                progressDialog = null;
            }

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.editprofile, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        if (id == R.id.action_fileReport){
            Intent intent = new Intent(CreateProfileActivity.this, fileReport.class);
            startActivity(intent);
        }

        if (id == R.id.action_signout){
            ParseUser currentUser = ParseUser.getCurrentUser();
            currentUser.logOut();
            Intent intent = new Intent(CreateProfileActivity.this, SignInActivity.class);
            startActivity(intent);
        }



        return super.onOptionsItemSelected(item);
    }

}



