package com.example.a7med_11.finalmovieapp;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.example.a7med_11.finalmovieapp.Adapters.SelectionChangeListener;

public class MainActivity extends AppCompatActivity implements SelectionChangeListener {
    private boolean mTwoPane;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        main_activity_fragment fragment = new main_activity_fragment();
//        FragmentManager fragmentManager = getFragmentManager();
//        FragmentTransaction transaction = fragmentManager.beginTransaction();
//        transaction.add(R.id.my_main, fragment, "MainFragment");
//        transaction.commit();


        if (findViewById(R.id.movie_detail_container) != null) {
            mTwoPane = true;
            if (savedInstanceState == null) {
                        Detail_Fragment fragment = new Detail_Fragment();
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.movie_detail_container, fragment, Detail_Fragment.TAG);
        transaction.commit();
            }
        } else {
            mTwoPane = false;
        }
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void OnSelectionChanger(String[] data) {
        String[] x = data;
        if (mTwoPane) {
            Bundle arguments = new Bundle();
            // arguments.putParcelable(Detail_Fragment.DETAIL_MOVIE, x);

            arguments.putStringArray("movie_data",x);

            Detail_Fragment fragment = new Detail_Fragment();
            fragment.setArguments(arguments);
            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.replace(R.id.movie_detail_container, fragment, Detail_Fragment.TAG);
            transaction.commit();



        } else {
            Intent intent = new Intent(this,Detail_Activity.class)
                    .putExtra("movie_data", x);
            startActivity(intent);
        }
    }
}
