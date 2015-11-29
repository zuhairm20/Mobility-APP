package com.parse.starter;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.parse.ParseACL;
import com.parse.ParseException;
import com.parse.ParseFile;
//import com.parse.ParseImageView;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;



public class fileReport extends AppCompatActivity {


    private EditText titleEdit;
    private String titleText;
    private EditText descEdit;
    private String description;
    private ProgressDialog progressDialog;

    protected Button mPhoto;
    protected ImageView mImageView;
    protected ParseFile saveImageFile;

   // private ParseImageView taskPic;


    private static final int CAMERA_REQUEST = 1888;
    private static final int SELECT_PHOTO = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_report);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



        //Initialize ImageView

        mImageView = (ImageView) findViewById(R.id.imagePreview);

        //Initialize Camera
        mPhoto = (Button) findViewById(R.id.choosePic);

        mPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });

        titleEdit = (EditText) findViewById(R.id.titleEdit);

        descEdit = (EditText) findViewById(R.id.describeEdit);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                       // .setAction("Action", null).show();


                titleText = titleEdit.getText().toString();
                description = descEdit.getText().toString();

                Boolean boo = true;

                if (titleText.isEmpty())
                {
                    boo = false;
                    showToast(getApplicationContext(),  "Please enter a title for your Report.");
                    Animation shake = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.shake);
                    findViewById(R.id.titleEdit).startAnimation(shake);
                    return;

                }

                if (description.isEmpty())
                {
                    Animation shake = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.shake);
                    findViewById(R.id.describeEdit).startAnimation(shake);
                    showToast(getApplicationContext(),  "Please enter a description for your report.");
                    boo = false;
                    return;
                }



                if (boo) {
                    createReport(titleText, description);
                }
            }
            });


        }

    private void selectImage() {

        final CharSequence[] options = { "Take Photo", "Choose from Gallery","Cancel" };

        AlertDialog.Builder builder = new AlertDialog.Builder(fileReport.this);
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



    protected static void showToast(Context con, CharSequence text) {
        Toast.makeText(con, text, Toast.LENGTH_SHORT).show();
    }

    public void onLoadingStart(boolean showSpinner, String message) {
        if (showSpinner) {
            if (!isFinishing()) {
                progressDialog = ProgressDialog.show(this, "Please wait",
                        message, true, true);
            }

        }
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
                saveImageFile = new ParseFile("reportPicture.jpg", b);
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
                    saveImageFile = new ParseFile("reportPicture.jpg", b);
                    //selectPhoto.setText("taskPicture.jpg");
                    //taskPic.setParseFile(saveImageFile);
//                    taskPic.loadInBackground(new GetDataCallback() {
  //                      @Override
    //                    public void done(byte[] data, ParseException e) {
       //                     if (e == null) {
     //                           onLoadingFinish();

//                                Log.d("Zuhair", "Got image");
  //                          }
    //                        else{
      //                          Log.d("Zuhair", "" + e.getMessage());
        //                    }
          //              }
      //              });
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

    protected void createReport(String title, String description){
        onLoadingStart(true, "Creating Report");
        final ParseUser user = ParseUser.getCurrentUser();
        final ParseObject report = new ParseObject("Report");
        //String id = (String) ParseUser.getCurrentUser().get("objectId");

        if (saveImageFile != null){
            report.put("reportImage", saveImageFile);
        }

        if (saveImageFile == null){
            //do nothing
        }
        report.put("createdBy", user);

        report.put("Content", description);
        report.put("Title", title);
        report.put("Status", "New");


       // if (saveImageFile != null){
         //   task.put("taskImage", saveImageFile);
        //}

        final ParseACL acl = new ParseACL();
        acl.setPublicReadAccess(true);
        acl.setWriteAccess(ParseUser.getCurrentUser(), true);
        ///create roles
        report.setACL(acl);

        report.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e==null){
                //success
                    onLoadingFinish();
                    showToast(getApplicationContext(), "You have successfully filed this report. It will now be reviewed by an admin.");
                    Intent intent = new Intent(fileReport.this, viewReport.class);
                    intent.putExtra("reportId", report.getObjectId());
                    startActivity(intent);
                }
                else{
                    onLoadingFinish();
                    //failure
                    showToast(getApplicationContext(), "Failed to create Report");
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
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

        if (id == R.id.action_signout){
            ParseUser currentUser = ParseUser.getCurrentUser();
            currentUser.logOut();
            Intent intent = new Intent(fileReport.this, SignInActivity.class);
            startActivity(intent);
                }


        if (id == R.id.action_editProfile){
            Intent intent = new Intent(fileReport.this, CreateProfileActivity.class);
            startActivity(intent);
        }

        if (id == R.id.action_manageReports){
            Intent intent = new Intent(fileReport.this, manageReports.class);
            startActivity(intent);
        }


        return super.onOptionsItemSelected(item);
    }

}
