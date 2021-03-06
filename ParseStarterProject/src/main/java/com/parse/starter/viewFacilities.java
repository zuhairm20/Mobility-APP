package com.parse.starter;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
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

public class viewFacilities extends InstabugListActivity {


    private ProgressDialog progressDialog;
    private SwipeRefreshLayout swipeLayout;
    private List<ParseObject> facilities;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_facilities);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
      //  setSupportActionBar(toolbar);

        facilities = new ArrayList<>();
        facilityListAdapter adapter = new facilityListAdapter(this, R.layout.facility_item_layout, facilities);
        setListAdapter(adapter);

        refreshFacilities();
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setBackgroundTintList(ColorStateList.valueOf(Color.rgb(205, 92, 92)));
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(viewFacilities.this, CreateFacility.class);
                startActivity(intent);
            }
        });


        final SwipeRefreshLayout swipeLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_container);
        swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeLayout.setRefreshing(false);
                refreshFacilities();
            }
        });

    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        ParseObject t = facilities.get(position);
        Intent intent = new Intent(this, viewFacility.class);
        intent.putExtra("facilityId", t.getObjectId());
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

    public void refreshFacilities(){
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Facility");
        query.orderByDescending("facilityName");
        onLoadingStart(true, "Loading Facilities...");
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> facilityList, ParseException e) {
                onLoadingFinish();
                if (e == null) {
                    // If there are results, update the list of tasks
                    // and notify the adapter
                    facilities.clear();
                    for (ParseObject facility : facilityList) {
                            facilities.add(facility);

                    }

                    if (facilities.isEmpty()) {
                        showToast(getApplicationContext(), "There are no facilities to display.");

                    }
                    ((facilityListAdapter) getListAdapter()).notifyDataSetChanged();
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
