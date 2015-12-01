package com.parse.starter;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

public class listSuggestions extends ListActivity {

    private ProgressDialog progressDialog;
    private SwipeRefreshLayout swipeLayout;
    private List<ParseObject> suggestions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_suggestions);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);

        suggestions = new ArrayList<>();
        suggestionListAdapter adapter = new suggestionListAdapter(this, R.layout.suggestion_list_item_layout, suggestions);
        setListAdapter(adapter);

        refreshSuggestions();

        final SwipeRefreshLayout swipeLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_container);
        swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeLayout.setRefreshing(false);
                refreshSuggestions();
            }
        });


    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        ParseObject t = suggestions.get(position);
       // Intent intent = new Intent(this, viewEvent.class);
        //intent.putExtra("eventID", t.getObjectId());
       // startActivity(intent);
    }

    public void onLoadingFinish() {

        if (progressDialog != null) {

            if (!isFinishing()) {

                progressDialog.dismiss();
                progressDialog = null;
            }

        }
    }

    private void refreshSuggestions() {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Suggestion");
        query.orderByDescending("createdAt");
        onLoadingStart(true, "Loading Suggestions...");
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> suggestionList, ParseException e) {
                onLoadingFinish();
                if (e == null) {
                    // If there are results, update the list of tasks
                    // and notify the adapter
                    suggestions.clear();
                    for (ParseObject suggestion : suggestionList) {
                        suggestions.add(suggestion);
                    }

                    if (suggestions.isEmpty()) {
                        showToast(getApplicationContext(), "There are no suggestions to display.");

                    }
                    ((suggestionListAdapter) getListAdapter()).notifyDataSetChanged();
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


