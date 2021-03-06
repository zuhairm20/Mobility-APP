package com.parse.starter;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.GetCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.SaveCallback;

public class viewReport extends AppCompatActivity {

    private ProgressDialog progressDialog;
    ParseObject report;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_report);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final TextView reportTitle = (TextView) findViewById(R.id.facilityName);

        final TextView reportContent = (TextView) findViewById(R.id.facilityDescription);

        final ImageView reportPic = (ImageView) findViewById(R.id.reportPic);

        Intent intent = this.getIntent();

        if (intent.getExtras() != null) {
            //get Report to view
            final ParseQuery<ParseObject> query = ParseQuery.getQuery("Report");
            query.include("createdBy");
            onLoadingStart(true, "Loading Report...");
            query.getInBackground(intent.getStringExtra("reportId"), new GetCallback<ParseObject>() {
                @Override
                public void done(ParseObject rep, ParseException e) {
                    if (e == null){
                        report = rep;
                        reportTitle.setText(rep.get("Title").toString());
                        reportContent.setText(rep.get("Content").toString());
                        ParseFile repImage = rep.getParseFile("reportImage");
                        loadImages(repImage, reportPic);
                        onLoadingFinish();
                    }

                    else{

                        //error handling
                    }
                }
            });
        }

        Button approve = (Button) findViewById(R.id.approve);
        approve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onLoadingStart(true, "Approving Report...");
                report.put("Status", "Approved");
                report.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                       if (e == null) {
                           onLoadingFinish();
                           showToast(getApplicationContext(), "You have successfully approved report.");
                           Intent intent = new Intent(viewReport.this, manageReports.class);
                           startActivity(intent);
                       }
                    }
                });
            }
        });

        Button deny = (Button) findViewById(R.id.deny);
        deny.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onLoadingStart(true, "Denying Report...");
                report.put("Status", "Rejected");
                report.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (e == null){
                            onLoadingFinish();
                            showToast(getApplicationContext(), "You have successfully denied report.");
                            Intent intent = new Intent(viewReport.this, manageReports.class);
                            startActivity(intent);
                        }
                    }
                });
            }
        });





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

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)  {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            // do something on back.
            Intent intent = new Intent(viewReport.this, manageReports.class);
            startActivity(intent);
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }


}
