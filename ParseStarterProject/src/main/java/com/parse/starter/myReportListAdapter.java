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

import java.util.List;

/**
 * Created by Zuhair on 15-11-30.
 */
public class myReportListAdapter extends ArrayAdapter<ParseObject> {

    public myReportListAdapter(Context context, int resource, List<ParseObject> items) {
        super(context, resource, items);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = convertView;

        if (v == null) {
            LayoutInflater vi;
            vi = LayoutInflater.from(getContext());
            v = vi.inflate(R.layout.my_report_item_layout, null);
        }

        ParseObject p = getItem(position);

        if (p != null) {
            TextView title = (TextView) v.findViewById(R.id.reportName);
            TextView content = (TextView) v.findViewById(R.id.reportContent);
            TextView status = (TextView)  v.findViewById(R.id.reportStatus);
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

            if (p.get("Status").equals("new")) {
                status.setText("New");
            }

            else {
                status.setText(p.get("Status").toString());
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
