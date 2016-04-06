package com.example.a7med_11.finalmovieapp;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

public class Detail_Activity extends AppCompatActivity {
    private final String LOG_TAG = Detail_Activity.class.getSimpleName();

    public String[] arr1;
    ImageView poster;
    TextView title;
    TextView overview;
    TextView date;
    TextView vote_average;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_);


        if (savedInstanceState == null) {
            Bundle arguments = new Bundle();
            arguments.putStringArray("movie_data",
                    getIntent().getStringArrayExtra("movie_data"));

            Detail_Fragment fragment = new Detail_Fragment();
            fragment.setArguments(arguments);

            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.add(R.id.movie_detail_container, fragment);
            transaction.commit();
        }




//        if(savedInstanceState == null) {
//            Detail_Fragment fragment = new Detail_Fragment();
//            FragmentManager fragmentManager = getFragmentManager();
//            FragmentTransaction transaction = fragmentManager.beginTransaction();
//            transaction.add(R.id.movie_detail_container, fragment, "DetailFragment");
//            transaction.commit();
//        }
//        Intent intent = getIntent();
//
//        arr1 = intent.getExtras().getStringArray("details");
//        Log.v(LOG_TAG, "The dataaaaaaaaa  " + main_activity_fragment.detail_arr[6]);
//

    }
}
