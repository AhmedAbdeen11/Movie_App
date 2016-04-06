package com.example.a7med_11.finalmovieapp;

import android.app.Fragment;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.a7med_11.finalmovieapp.Adapters.Trailers_Adapter;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by a7med_11 on 28/01/16.
 */
public class Detail_Fragment extends Fragment {


    public static final String TAG = Detail_Fragment.class.getSimpleName();
    static final String DETAIL_MOVIE = "DETAIL_MOVIE";

    String[] mMovie;

    ImageButton b1;
    public String movie_id;

    Trailers_Adapter trailer_obj;
    GridView gridview;
    Button save;
    public static ArrayList x;

    public ListView listview;
    DBConnection database;
    ImageView poster;
    TextView title;
    TextView overview;
    TextView date;
    TextView vote_average;
    boolean fav_status=false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        Bundle arguments = getArguments();
        if (arguments != null) {
            mMovie = arguments.getStringArray("movie_data");
        }

       View view = inflater.inflate(R.layout.detail_fragment_layout, container, false);

        if (mMovie != null) {
            view.setVisibility(View.VISIBLE);


            poster = (ImageView) view.findViewById(R.id.detail_image);
            title = (TextView) view.findViewById(R.id.detail_title);
            overview = (TextView) view.findViewById(R.id.detail_overview);
            date = (TextView) view.findViewById(R.id.detail_date);
            vote_average = (TextView) view.findViewById(R.id.detail_vote_average);

            Picasso.with(getActivity())
                    .load("http://image.tmdb.org/t/p/w185" + mMovie[1])
                    .into(poster);
            title.setText(mMovie[2]);
            overview.setText(mMovie[3]);
            date.setText(mMovie[4]);
            vote_average.setText(mMovie[5] + "/10");

            //-------------Favourite Status----------//
            database = new DBConnection(getActivity());

            save = (Button) view.findViewById(R.id.favorite);
            fav_status = database.search_movie(mMovie[6]);
            if (fav_status == true) {
                save.setText("Remove From Favourite");
            } else {
                save.setText("Add to Favourite");
            }


            gridview = (GridView) view.findViewById(R.id.trailers_gridview);
            trailer_obj = new Trailers_Adapter(getActivity());
            gridview.setAdapter(trailer_obj);
            FetchTrailor f1 = new FetchTrailor();
            f1.execute(mMovie[6]);
            FetchReviews f2 = new FetchReviews();
            f2.execute(mMovie[6]);

            gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    StringBuilder x = new StringBuilder("https://www.youtube.com/watch?v=");
                    String[] r = trailer_obj.getItem(position);
                    x.append(r[0]);
                    Intent intent;
                    intent = new Intent(Intent.ACTION_VIEW, Uri.parse(x.toString()));
                    startActivity(intent);
                    //   Log.v(LOG_TAG,"You clicked the button1 and the key is " + key + "     " + movie_id);
                }
            });


            save.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (fav_status == false) {
                        long id = database.dataInsert(
                                mMovie[0],
                                mMovie[1],
                                mMovie[2],
                                mMovie[3],
                                mMovie[4],
                                mMovie[5],
                                mMovie[6]
                        );

                        save.setText("Remove From Favourite");
                        fav_status = true;


                    } else {

                        int count = database.delete(mMovie[6]);
                        save.setText("Add to Favourite");
                        fav_status = false;

                    }
                }
            });
        }
        else {
            view.setVisibility(View.INVISIBLE);
        }
        return view;
    }







    //------------------------Async Task for Trailors--------------------------//

    public class FetchTrailor extends AsyncTask<String, Void, ArrayList<String[]>> {
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        String jsonStr = null;

        @Override
        protected ArrayList<String[]> doInBackground(String... params) {


            try {


                //---------------- Compare if i want trailor or reviews link -----------


                String mo_id = params[0];
                final String BASE_URL = "http://api.themoviedb.org/3/movie?";
                final String API_KEY_PARAM = "api_key";
                final String Videos = "videos";
                final String Reviews = "reviews";
                JSONArray MoviesArray;
                ArrayList<String[]> trailors_data = new ArrayList<>();


                Uri builtUri = Uri.parse(BASE_URL).buildUpon().
                        appendPath(mo_id).appendPath(Videos)
                        .appendQueryParameter(API_KEY_PARAM, "a8881a4f49366e9189566d112ee91f26")
                        .build();

                URL url = new URL(builtUri.toString());


                //----------connect to themoviedb to get trailers-----------//

                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));
                String line;
                while ((line = reader.readLine()) != null) {
                    // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                    // But it does make debugging a *lot* easier if you print out the completed
                    // buffer for debugging.
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    return null;
                }
                jsonStr = buffer.toString();


                //------get key for youtube from the jsonstr--------//

                String key = null;
                String title;
                String[] myarr = new String[2];
                JSONObject MovieJson = new JSONObject(jsonStr);
                MoviesArray = MovieJson.getJSONArray("results");
                int counter;
                for (counter = 0; counter < MoviesArray.length(); counter++) {
                    MovieJson = MoviesArray.getJSONObject(counter);

                    myarr[0] = MovieJson.getString("key");
                    myarr[1] = MovieJson.getString("name");

                    trailors_data.add(myarr);

                    myarr = new String[2];
                }

                return trailors_data;


            } catch (MalformedURLException e) {


                e.printStackTrace();
            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(ArrayList<String[]> strings) {

            trailer_obj.clear();
            trailer_obj.add(strings);
        }
    }

    //----------------------------Async Task for Reviews-----------------//

    public class FetchReviews extends AsyncTask<String, Void, ArrayList<String[]>> {
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        String jsonStr = null;


        @Override
        protected ArrayList<String[]> doInBackground(String... params) {
            String mo_id = params[0];
            final String BASE_URL = "http://api.themoviedb.org/3/movie?";
            final String API_KEY_PARAM = "api_key";
            final String Videos = "videos";
            final String Reviews = "reviews";
            JSONArray MoviesArray;
            ArrayList<String[]> Reviews_data = new ArrayList<>();


            try{

                Uri builtUri = Uri.parse(BASE_URL).buildUpon().
                        appendPath(mo_id).appendPath(Reviews)
                        .appendQueryParameter(API_KEY_PARAM, "a8881a4f49366e9189566d112ee91f26")
                        .build();

                URL url = new URL(builtUri.toString());


                //----------connect to themoviedb to get reviews-------//


                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));
                String line;
                while ((line = reader.readLine()) != null) {
                    // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                    // But it does make debugging a *lot* easier if you print out the completed
                    // buffer for debugging.
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    return null;
                }
                jsonStr = buffer.toString();




                String author = null;
                String content;
                String[] myarr = new String[2];
                JSONObject MovieJson = new JSONObject(jsonStr);
                MoviesArray = MovieJson.getJSONArray("results");
                int counter;
                for (counter = 0; counter < MoviesArray.length(); counter++) {
                    MovieJson = MoviesArray.getJSONObject(counter);

                    myarr[0] = MovieJson.getString("author");
                    myarr[1] = MovieJson.getString("content");

                    Reviews_data.add(myarr);

                    myarr = new String[2];
                }

                return Reviews_data;


            } catch (MalformedURLException e) {


                e.printStackTrace();
            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }




        @Override
        protected void onPostExecute(ArrayList<String[]> strings) {
            super.onPostExecute(strings);

            String[] data = new String[2];
            LinearLayout list = (LinearLayout)getView().findViewById(R.id.reviews_linearlayout);
            for (int i=0; i<strings.size(); i++) {
                // Product product = products.get(i);
                //View vi = inflater.inflate(R.layout.product_item, null);
                data = strings.get(i);

                View v ;
                v = LayoutInflater.from(getActivity()).inflate(R.layout.reviews_layout,null);
                TextView textview = (TextView) v.findViewById(R.id.author);
                textview.setText(data[0]);
                TextView textview2 = (TextView) v.findViewById(R.id.content);
                textview2.setText(data[1]);

                list.addView(v);
            }
        }
    }

}