package com.example.a7med_11.finalmovieapp.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.example.a7med_11.finalmovieapp.R;
import com.squareup.picasso.Picasso;

/**
 * Created by a7med_11 on 28/01/16.
 */
public class Movie_Adapter extends ArrayAdapter<String> {




    //ArrayList<String[]> strings = new ArrayList<>();
    String[] data = {};
    public Movie_Adapter(Context context) {
        super(context, 0);

    }
    public void add(String[] data){
        this.data = data;
    }

//    public void add(ArrayList<String[]> strings) {
//
//        this.strings = strings;
//    }

    @Override
    public int getCount() {
        return this.data.length;
    }

    @Override
    public String getItem(int position) {
        return this.data[position];
    }

    static class DataHandler{
        ImageView Poster_Image;
    }



    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        DataHandler handler;
        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.grid_item_layout, parent, false);

            handler = new DataHandler();
            handler.Poster_Image = (ImageView) convertView.findViewById(R.id.grid_item_image);


            convertView.setTag(handler);
        }
        else
        {
            handler = (DataHandler) convertView.getTag();
        }


        Picasso.with(getContext())
                .load("http://image.tmdb.org/t/p/w185"+data[position])
                .into(handler.Poster_Image);
        //handler.Poster_Image.setScaleType(ImageView.ScaleType.MATRIX);

        return convertView;
    }
}
