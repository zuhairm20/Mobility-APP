package com.parse.starter;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.ParseACL;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;

public class fileReport extends AppCompatActivity {


    private EditText titleEdit;
    private String titleText;
    private EditText descEdit;
    private String description;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_report);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


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

        report.put("createdBy", user);

        report.put("Content", description);
        report.put("Title", title);
       // report.put("Status", "New");


       // if (saveImageFile != null){
         //   task.put("taskImage", saveImageFile);
        //}

        final ParseACL acl = new ParseACL();
        acl.setPublicReadAccess(true);
        acl.setWriteAccess(ParseUser.getCurrentUser(), true);

        report.setACL(acl);

        report.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e==null){
                //success
                    onLoadingFinish();
                    showToast(getApplicationContext(), "You have successfully filed this report. It will now be reviewed by an admin.");
                    Intent intent = new Intent(fileReport.this, fileReport.class);
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

        if (id == R.id.action_editProfile){
            Intent intent = new Intent(fileReport.this, CreateProfileActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

}
