package com.example.a7med_11.finalmovieapp;

import android.app.Fragment;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.example.a7med_11.finalmovieapp.Adapters.Movie_Adapter;
import com.example.a7med_11.finalmovieapp.Adapters.SelectionChangeListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by a7med_11 on 28/01/16.
 */
public class main_activity_fragment extends Fragment {


    DBConnection database2;
    public JSONArray MoviesArray;
    private GridView mGridView;
    private static final String SORT_SETTING = "sort_setting";
    private static final String POPULARITY_DESC = "popularity.desc";
    private static final String RATING_DESC = "vote_average.desc";
    private String mSortBy = POPULARITY_DESC;
    Movie_Adapter movieadapter_obj;
    ArrayList<String[]> movies_data = new ArrayList<>();


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        boolean isEnabled = Settings.System.getInt(this.getActivity().getContentResolver(),Settings.System.AIRPLANE_MODE_ON, 0) == 1;

        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_fragment, menu);
        MenuItem action_sort_by_popularity = menu.findItem(R.id.action_sort_by_popularity);
        MenuItem action_sort_by_rating = menu.findItem(R.id.action_sort_by_rating);
        if (mSortBy.contentEquals(POPULARITY_DESC)) {
            if (!action_sort_by_popularity.isChecked())
                action_sort_by_popularity.setChecked(true);
        }
        else {
            if (!action_sort_by_rating.isChecked())
                action_sort_by_rating.setChecked(true);
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_sort_by_popularity:
                if (item.isChecked())
                    item.setChecked(false);
                else
                    item.setChecked(true);
                mSortBy = POPULARITY_DESC;
                updateMovies(mSortBy);
                return true;
            case R.id.action_sort_by_rating:
                if (item.isChecked())
                    item.setChecked(false);
                else
                    item.setChecked(true);
                mSortBy = RATING_DESC;
                updateMovies(mSortBy);
                return true;
            case R.id.Favourite_item:
                if (item.isChecked())
                    item.setChecked(false);
                else
                    item.setChecked(true);
                getfavourite();

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void getfavourite(){

        //--------Retriev/roote data//

        int i=0;
        ArrayList<String[]> datasql1 = new ArrayList<>();

        datasql1 = database2.viewData();
        String[] arr_posters =new String[datasql1.size()];

        for (String[] x:datasql1) {
            arr_posters[i] = x[0];
            i++;
        }



        //------set new Adapter--//
        movieadapter_obj.clear();

        movieadapter_obj.add(arr_posters);





        //------intialize movies data----///
        movies_data = datasql1;

    }

    private void updateMovies(String sort_by) {
        FetchMoviesTask moviesTask = new FetchMoviesTask();
        moviesTask.execute(sort_by);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.main_fragment, container, false);
        mGridView = (GridView) view.findViewById(R.id.gridview_movies);
        movieadapter_obj = new Movie_Adapter(getActivity());
        mGridView.setAdapter(movieadapter_obj);



        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Movie movie = mMovieGridAdapter.getItem(position);

                String[] arr = movies_data.get(position);

                SelectionChangeListener listener = (SelectionChangeListener) getActivity();
                listener.OnSelectionChanger(arr);
//                detail_arr = arr;
//
//                Log.v(LOG_TAG,"ARRayFinal   " + arr[0] + arr[1] + arr[2] + arr[3]);
//
//                Intent i=new Intent(getActivity(), Detail_Activity.class);
//                i.putExtra("details",arr);
//                startActivity(i);
            }
        });

        //mGridView.setAdapter(adapter_obj);
        FetchMoviesTask MoviesTask = new FetchMoviesTask();
        MoviesTask.execute(mSortBy);

        database2 = new DBConnection(getActivity());
        return view;
    }



    public class FetchMoviesTask extends AsyncTask<String, Void, String[]> {

        private final String LOG_TAG = FetchMoviesTask.class.getSimpleName();

        //------------------ Array of movies------------------
        private String[] getMovieDataFromJson(String Moviejsonstr)
                throws JSONException {

            // These are the names of the JSON objects that need to be extracted.
            final String MOVIE_LIST = "results";
            final String Poster_Path = "poster_path";
            final String OverView = "overview";
            final String RELEASE_DATE = "release_date";
            final String TITLE = "original_title";
            final String ID = "id";
            final String backdrop_Path = "backdrop_path";
            final String Vote_Average = "vote_average";

            JSONObject MovieJson = new JSONObject(Moviejsonstr);
            MoviesArray = MovieJson.getJSONArray(MOVIE_LIST);
            Log.v(LOG_TAG,"JsonArray : " + MoviesArray);

            int nummovies = MoviesArray.length();

            String[] posters = new String[nummovies];
            String[] data = new String[7];
            movies_data = new ArrayList<>();
            for(int i = 0; i < nummovies; i++) {
                String posterpath ;
                JSONObject t_movie = MoviesArray.getJSONObject(i);
                posterpath = t_movie.getString(Poster_Path);
                posters[i] = posterpath;

                data[0] = posterpath;
                data[1] = t_movie.getString(backdrop_Path);
                data[2] = t_movie.getString(TITLE);
                data[3] = t_movie.getString(OverView);
                data[4] = t_movie.getString(RELEASE_DATE);
                data[5] = t_movie.getString(Vote_Average);
                data[6] = t_movie.getString(ID);
                movies_data.add(data);
                data = new String[7];
            }

            return posters;
        }
        //--------------------- --------------------------------//





        @Override
        protected String[] doInBackground(String... params) {
            Log.v(LOG_TAG,"Start of doInBackground");

            if (params.length == 0) {
                return null;
            }



            //--------------------Connection---------------------//

            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            String jsonStr = null;

            try {


                final String BASE_URL = "http://api.themoviedb.org/3/discover/movie?";
                final String SORT_BY_PARAM = "sort_by";
                final String API_KEY_PARAM = "api_key";
                //final String Favourite_Key_Param = "favourite";


                Uri builtUri = Uri.parse(BASE_URL).buildUpon()
                        .appendQueryParameter(SORT_BY_PARAM, params[0])
                        .appendQueryParameter(API_KEY_PARAM, "a8881a4f49366e9189566d112ee91f26")
                        .build();

                URL url = new URL(builtUri.toString());

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
            } catch (IOException e) {
                Log.e(LOG_TAG, "Error ", e);
                return null;
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e(LOG_TAG, "Error closing stream", e);
                    }
                }
            }



            try {
                return getMovieDataFromJson(jsonStr);
            } catch (JSONException e) {
                Log.e(LOG_TAG, e.getMessage(), e);
                e.printStackTrace();
            }

            // This will only happen if there was an error getting or parsing the forecast.
            Log.v(LOG_TAG,"End Of OnBackground");
            Log.v(LOG_TAG,"Movie Json String : " +  jsonStr);
            return null;
        }

        @Override
        protected void onPostExecute(String[] movies) {
            movieadapter_obj.clear();
            movieadapter_obj.add(movies);
        }
    }
}