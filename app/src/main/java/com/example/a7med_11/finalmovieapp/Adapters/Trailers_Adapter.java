package com.example.a7med_11.finalmovieapp.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.a7med_11.finalmovieapp.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by a7med_11 on 29/01/16.
 */
public class Trailers_Adapter extends ArrayAdapter<String[]> {


    ArrayList<String[]> strings = new ArrayList<>();
    private final String LOG_TAG2 = Trailers_Adapter.class.getSimpleName();
    public Trailers_Adapter(Context context) {
        super(context, 0);

    }

    public void add(ArrayList<String[]> strings) {

        this.strings = strings;
    }

    @Override
    public int getCount() {
        int size = this.strings.size();
        return size;
    }

    @Override
    public String[] getItem(int position) {
        return this.strings.get(position);
    }

    static class DataHandler{
        ImageView trailer_Image;
        TextView trailer_title;
    }



    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        DataHandler handler;
        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.trailors_layout, parent, false);

            handler = new DataHandler();
            handler.trailer_Image = (ImageView) convertView.findViewById(R.id.trailer_image);
            handler.trailer_title = (TextView) convertView.findViewById(R.id.trailer_name);

            convertView.setTag(handler);
        }
        else
        {
            handler = (DataHandler) convertView.getTag();
        }

        String[] mydata = getItem(position);

        String trailer_URL = "http://img.youtube.com/vi/" + mydata[0] + "/0.jpg";
        Picasso.with(getContext())
                .load(trailer_URL)
                .into(handler.trailer_Image);
        handler.trailer_title.setText(mydata[1]);

        return convertView;
    }
}
