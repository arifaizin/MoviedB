package com.example.wakhyudi.moviedb.view;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.wakhyudi.moviedb.BuildConfig;
import com.example.wakhyudi.moviedb.R;
import com.example.wakhyudi.moviedb.adapter.MovieNowAdapter;
import com.example.wakhyudi.moviedb.model.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

@SuppressWarnings("deprecation")
public class NavDrawerActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    RecyclerView recyclerView;
    String BASE_URL = "https://api.themoviedb.org/3/movie/now_playing";
    final static String API_KEY_PARAMS = "api_key";
    final static String LANGUAGE_PARAMS = "language";
    public ArrayList<Movie>listMovie = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nav_drawer);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
//        getSupportActionBar().setTitle("Now Playing");
//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });

        recyclerView = (RecyclerView)findViewById(R.id.rv_now_playing);
        GridLayoutManager glm = new GridLayoutManager(this,2);

        recyclerView.setLayoutManager(glm);

        OkHttpClient network = new OkHttpClient();



        Uri uri = Uri.parse(BASE_URL).buildUpon()
                    .appendQueryParameter(API_KEY_PARAMS, BuildConfig.APi_KEY_MOVIE)
                    .appendQueryParameter(LANGUAGE_PARAMS,"en-US")
                    .build();

        String url = uri.toString();

        Request request = new Request.Builder()
                            .url(url)
                            .build();

        network.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),"Conection Failed",Toast.LENGTH_LONG).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String jsonData = response.body().string();

                try {
                    JSONObject jsonObject = new JSONObject(jsonData);
                    JSONArray jsonArray = jsonObject.getJSONArray("results");
                    for (int i = 0; i<jsonArray.length();i++){
                        JSONObject data = jsonArray.getJSONObject(i);
                        String title = data.getString("title");
                        String tanggal = data.getString("release_date");
                        String linkGambar = data.getString("poster_path");
                        String overview =  data.getString("overview");

                        //masukan ke class model agar menjadi satu objek
                        Movie movie = new Movie(linkGambar, title, tanggal, overview);

                        listMovie.add(movie);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        MovieNowAdapter adapter = new MovieNowAdapter(NavDrawerActivity.this,listMovie);
                        recyclerView.setAdapter(adapter);
                    }
                });

            }



        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.nav_drawer, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_now_playing) {
            Intent i = new Intent(this,NavDrawerActivity.class);
            startActivity(i);

        } else if (id == R.id.nav_upcoming) {

        } else if (id == R.id.nav_search) {
            Intent i = new Intent(this, MainActivity.class);
            startActivity(i);

        } else if (id == R.id.nav_notification) {
            Intent i = new Intent(this, NotificationSettingActivity.class);
            startActivity(i);

        } else if (id == R.id.nav_language) {
            Intent i = new Intent(Settings.ACTION_LOCALE_SETTINGS);
            startActivity(i);


        }else if (id == R.id.nav_favorite) {
            Intent i = new Intent(this,FavoriteActivity.class);
            startActivity(i);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
