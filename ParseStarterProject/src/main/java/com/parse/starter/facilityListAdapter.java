package com.parse.starter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;

import java.util.List;

/**
 * Created by Zuhair on 15-11-20.
 */
public class facilityListAdapter extends ArrayAdapter<ParseObject> {

    public facilityListAdapter(Context context, int resource, List<ParseObject> items) {
        super(context, resource, items);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = convertView;

        if (v == null) {
            LayoutInflater vi;
            vi = LayoutInflater.from(getContext());
            v = vi.inflate(R.layout.facility_item_layout, null);
        }

        ParseObject p = getItem(position);

        if (p != null) {
            TextView title = (TextView) v.findViewById(R.id.facilityName);
            TextView content = (TextView) v.findViewById(R.id.reportContent);
            TextView userName = (TextView)  v.findViewById(R.id.userName);
            ImageView reportImage = (ImageView) v.findViewById(R.id.reportPicture);

            if (p.has("Title")) {
                title.setText(p.get("Title").toString());
            }

            if (p.has("reportImage")) {
                loadImages(p.getParseFile("reportImage"), reportImage);
            }

            if (p.has("Content")) {

                content.setText(p.get("Content").toString());
            }

            ParseUser user = p.getParseUser("createdBy");

            if (user != null) {
                userName.setText(user.get("Name").toString());
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
