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
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class ownEvents extends InstabugListActivity {

    private ProgressDialog progressDialog;
    private SwipeRefreshLayout swipeLayout;
    private List<ParseObject> events;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_own_events);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);

        events = new ArrayList<>();
        myEventsListAdapter adapter = new myEventsListAdapter(this, R.layout.my_event_item_layout, events);
        setListAdapter(adapter);

        refreshEvents();
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setBackgroundTintList(ColorStateList.valueOf(Color.rgb(205, 92, 92)));
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ownEvents.this, CreateEvent.class);
                startActivity(intent);
            }
        });

        final SwipeRefreshLayout swipeLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_container);
        swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeLayout.setRefreshing(false);
                refreshEvents();
            }
        });

    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        ParseObject t = events.get(position);
        Intent intent = new Intent(ownEvents.this, viewEvent.class);
        intent.putExtra("eventID", t.getObjectId());
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

    public void refreshEvents() {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Event");
        query.orderByDescending("eventName");
        onLoadingStart(true, "Loading Events...");
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> eventList, ParseException e) {
                onLoadingFinish();
                if (e == null) {
                    ParseUser curr = ParseUser.getCurrentUser();
                    // If there are results, update the list of tasks
                    // and notify the adapter
                    events.clear();
                    for (ParseObject event : eventList) {
                        if (curr.equals(event.getParseUser("createdBy"))) {
                            events.add(event);
                        }

                    }

                    if (events.isEmpty()) {
                        showToast(getApplicationContext(), "There are no events to display.");

                    }
                    ((myEventsListAdapter) getListAdapter()).notifyDataSetChanged();
                } else {
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