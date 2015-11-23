package com.parse.starter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ArrayAdapter;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;

import java.util.List;

/**
 * Created by Zuhair on 15-11-24.
 */
public class eventListAdapter extends ArrayAdapter<ParseObject>{

    public eventListAdapter(Context context, int resource, List<ParseObject> items) {
        super(context, resource, items);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = convertView;

        if (v == null) {
            LayoutInflater vi;
            vi = LayoutInflater.from(getContext());
            v = vi.inflate(R.layout.event_item_layout, null);
        }

        ParseObject p = getItem(position);

        if (p != null) {
            TextView eventName = (TextView) v.findViewById(R.id.eventName);
            TextView eventDescription = (TextView) v.findViewById(R.id.eventDescription);
            TextView eventDate = (TextView)  v.findViewById(R.id.eventDate);
            ImageView eventImage = (ImageView) v.findViewById(R.id.eventPicture);

            if (p.has("eventName")) {
                eventName.setText(p.get("eventName").toString());
            }

            if (p.has("eventImage")) {
                loadImages(p.getParseFile("eventImage"), eventImage);
            }

            if (p.has("eventDescription")) {
                eventDescription.setText(p.get("eventDescription").toString());
            }

            if (p.has("eventDate")){
                eventDate.setText(p.get("eventDate").toString());
            }
        }
        return v;
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
}
