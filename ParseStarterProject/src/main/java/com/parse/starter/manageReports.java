package com.parse.starter;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.instabug.wrapper.support.activity.InstabugListActivity;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

public class manageReports extends InstabugListActivity {

    private ProgressDialog progressDialog;
    private SwipeRefreshLayout swipeLayout;
    private List<ParseObject> reports;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_reports);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
       // setSupportActionBar(toolbar);

        reports = new ArrayList<>();
        reportListAdapter adapter = new reportListAdapter(this, R.layout.list_item_layout, reports);
        setListAdapter(adapter);




        refreshReports();



        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });


        final SwipeRefreshLayout swipeLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_container);
        swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeLayout.setRefreshing(false);
                refreshReports();
            }
        });


    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        ParseObject t = reports.get(position);
        Intent intent = new Intent(this, viewReport.class);
        intent.putExtra("reportId", t.getObjectId());
        startActivity(intent);
    }

    public void onLoadingFinish() {

        if (progressDialog != null) {

            if (!isFinishing()) {

                progressDialog.dismiss();
                progressDialog = null;
            }

        }
    }

    public void refreshReports(){
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Report");
        query.orderByDescending("createdAt");
        query.include("createdBy");
        onLoadingStart(true, "Loading Reports...");
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> reportList, ParseException e) {
                onLoadingFinish();
                if (e == null) {
                    // If there are results, update the list of tasks
                    // and notify the adapter
                    reports.clear();
                    for (ParseObject rep : reportList) {

                        if (rep.getString("Status").equals("new")) {
                            reports.add(rep);
                        }
                    }

                    if (reports.isEmpty()) {
                        showToast(getApplicationContext(), "There are no pending reports at the moment.");

                    }
                    ((reportListAdapter) getListAdapter()).notifyDataSetChanged();
                }
                else {
                    Log.d(getClass().getSimpleName(), "Error: " + e.getMessage());
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

    protected static void showToast(Context con, CharSequence text) {
        Toast.makeText(con, text, Toast.LENGTH_SHORT).show();
    }


}
