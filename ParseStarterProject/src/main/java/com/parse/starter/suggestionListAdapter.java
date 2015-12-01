package com.parse.starter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckedTextView;

import com.parse.ParseObject;

import java.util.List;

/**
 * Created by Zuhair on 15-12-01.
 */
public class suggestionListAdapter extends ArrayAdapter<ParseObject> {

    public suggestionListAdapter(Context context, int resource, List<ParseObject> items) {
        super(context, resource, items);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = convertView;

        if (v == null) {
            LayoutInflater vi;
            vi = LayoutInflater.from(getContext());
            v = vi.inflate(R.layout.suggestion_list_item_layout, null);
        }

        ParseObject p = getItem(position);

        if (p != null) {
            CheckedTextView name = (CheckedTextView) v.findViewById(R.id.facilityName);

            if (p.has("Name")) {
                name.setText(p.get("Name").toString());
            }




        }
        return v;
    }
}
