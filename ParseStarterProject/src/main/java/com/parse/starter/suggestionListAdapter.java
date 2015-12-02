package com.parse.starter;

import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.SaveCallback;

import java.util.List;

/**
 * Created by Zuhair on 15-12-01.
 */
public class suggestionListAdapter extends ArrayAdapter<ParseObject> {

    ParseObject p;
    View v;
    TextView name;

    public suggestionListAdapter(Context context, int resource, List<ParseObject> items) {
        super(context, resource, items);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        v = convertView;

        if (v == null) {
            LayoutInflater vi;
            vi = LayoutInflater.from(getContext());
            v = vi.inflate(R.layout.suggestion_list_item_layout, null);
            CheckBox cb = (CheckBox) v.findViewById(R.id.checkbox_suggestion);
            cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    onCheckboxClicked(buttonView);
                }
            });
        }




         p = getItem(position);

        if (p != null) {
            name = (TextView) v.findViewById(R.id.facilityName);

            if (p.has("Name")) {
                name.setText(p.get("Name").toString());
            }




        }
        return v;
    }

    public void onCheckboxClicked(View view) {
        // Is the view now checked?
        boolean checked = ((CheckBox) view).isChecked();
        if (checked) {
            name.setPaintFlags(name.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            p.put("Status", "Reviewed");
            p.saveInBackground(new SaveCallback() {
                 @Override
                 public void done(ParseException e) {
                     if (e == null){

                     }
                 }
             });

        }

    }


}
