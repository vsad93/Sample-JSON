import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends Activity {

    private static String json_url = "http://api.themoviedb.org/3/movie/now_playing?api_key=b1e885f7a1a0602d435d8c52dc0de5f0";
    private ArrayAdapter<Movie> adapter;
    MovieAdapter movieAdapter;
    private List<Movie> movie_titles;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ListView listView = (ListView) findViewById(R.id.listView);
        movie_titles = new ArrayList<>();
        //adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, movie_titles);
        movieAdapter = new MovieAdapter(this,R.layout.custom_row,movie_titles);
        listView.setAdapter(movieAdapter);

    }


    public void buttonClicked(View view) {
        movie_titles.clear();
        movieAdapter.notifyDataSetChanged();
        if (view.getId() == R.id.buttonURL) new GetData().execute(json_url);
        if (view.getId() == R.id.buttonVolley) sendVolleyRequest();
    }


    public class GetData extends AsyncTask<String, String, String> {

        HttpURLConnection urlConnection;

        @Override
        protected String doInBackground(String... args) {

            StringBuilder result = new StringBuilder();

            try {
                URL url = new URL(args[0]);
                urlConnection = (HttpURLConnection) url.openConnection();
                InputStream in = new BufferedInputStream(urlConnection.getInputStream());

                BufferedReader reader = new BufferedReader(new InputStreamReader(in));

                String line;
                while ((line = reader.readLine()) != null) {
                    result.append(line);
                }

            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                urlConnection.disconnect();
            }


            return result.toString();
        }

        @Override
        protected void onPostExecute(String result) {

            //Do something with the JSON string
            populateList(result);
        }

    }


    private void sendVolleyRequest() {
//		Response.Listener responseLis = new Response.Listener<String>() {
//			@Override
//			public void onResponse(String response) {
//
//			}
//		};
//
//
//		Response.ErrorListener errLis = new Response.ErrorListener() {
//			@Override
//			public void onErrorResponse(VolleyError error) {
//
//			}
//		};
//
//
//
//		StringRequest s = new StringRequest("snjfdkdsjk",responseLis ,errLis );

        StringRequest stringRequest = new StringRequest(json_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        populateList(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(MainActivity.this, error.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void populateList(String result) {

        try {
            JSONObject json = new JSONObject(result);
            JSONArray movies = json.getJSONArray("results");
            for (int i = 0; i < movies.length(); i++) {
                JSONObject movie = movies.getJSONObject(i);
                String title = movie.getString("title");
                String overview = movie.getString("overview");
                String vote_average = movie.getString("vote_average");
                String posterPath = movie.getString("poster_path");
                Movie movie1 = new Movie(title,overview,posterPath);
                movie_titles.add(movie1);
            }
            movieAdapter.notifyDataSetChanged();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private class MovieAdapter extends ArrayAdapter<Movie> {
        final String BASE_IMAGE_URL = "http://image.tmdb.org/t/p/w185/ )";
        private int resource;
        private ArrayList<Movie> movies;


        MovieAdapter(Context context, int resource, List<Movie> objects) {
            super(context, resource, objects);
            movies = (ArrayList<Movie>) objects;


        }

        @Override
        public int getCount() {
            return movies.size();
        }

        @NonNull
        @Override
        public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            //return super.getView(position, convertView, parent);

            View v = convertView;
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inflater.inflate(R.layout.custom_row, null);
            //ConstraintLayout layout = (ConstraintLayout)findViewById(R.id.mycutomrow);
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // Go  to Next screen
                    //Toast.makeText(getContext(),"Clicked",Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(MainActivity.this,DetailActivity.class);
                    intent.putExtra("title",movies.get(position).mtitle);
                    intent.putExtra("url",movies.get(position).imageURL);
                    startActivity(intent);
                }
            });

            TextView textView = (TextView) v.findViewById(R.id.textView2);
            textView.setText(movies.get(position).mtitle + movies.get(position).description);
            return v;
        }




    }

}
