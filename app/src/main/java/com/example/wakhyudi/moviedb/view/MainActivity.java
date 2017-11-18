package com.example.wakhyudi.moviedb.view;

import android.app.ProgressDialog;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.wakhyudi.moviedb.R;
import com.example.wakhyudi.moviedb.adapter.MovieAdapter;
import com.example.wakhyudi.moviedb.model.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {
    final String BASE_MOVIE_URL = "https://api.themoviedb.org/3/search/movie?api_key=a27648284b6aa518fd4ceeff26cdc383";
    EditText edtSearchMovie;
    RecyclerView recyclerViewListMovie;
    ArrayList<Movie>listMovie = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ButterKnife.bind(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerViewListMovie = (RecyclerView)findViewById(R.id.rv_movie_list);
        edtSearchMovie = (EditText)findViewById(R.id.edt_search_movie);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(MainActivity.this);
        recyclerViewListMovie.setLayoutManager(linearLayoutManager);
    }

    //saat tombol cari diklik dia akan memanggil method ini
    public void searchMovie(View view) {
        ButterKnife.bind(this);
        final ProgressDialog progressDialog = new ProgressDialog(MainActivity.this);
        progressDialog.setMessage("Please wait ... Loading");
        progressDialog.show();
        //mengambil teks yang dimasukan ke edittext
        String cariMovie = edtSearchMovie.getText().toString();

        if(!listMovie.isEmpty()){
            listMovie.clear();
        }

        //membuat Uri
        Uri uri = Uri.parse(BASE_MOVIE_URL).buildUpon()
                                           .appendQueryParameter("query",cariMovie)
                                           .build();

        //merubah Uri ke url berbentuk string
        String url = uri.toString();

        //buat objek okhttp
        OkHttpClient network = new OkHttpClient();
        //buat request
        Request request = new Request.Builder().url(url).build();

        //mengirim request
        network.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressDialog.dismiss();
                        Toast.makeText(MainActivity.this,"Gagal",Toast.LENGTH_LONG).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String jsonData = response.body().string();

                try {
                    JSONObject jsonObject = new JSONObject(jsonData);
                    JSONArray jsonArray = jsonObject.getJSONArray("results");
                    for(int i = 0;i<jsonArray.length();i++){
                    JSONObject data = jsonArray.getJSONObject(i);
                        String title = data.getString("title");
                        String tanggal = data.getString("release_date");
                        String linkGambar = data.getString("poster_path");
                        String overview =  data.getString("overview");
                        //masukan ke class model agar menjadi satu objek
                        Movie movie = new Movie(linkGambar, title, tanggal, overview);
                        //masukan ke Arraylist
                        listMovie.add(movie);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressDialog.dismiss();
                        MovieAdapter adapter = new MovieAdapter(MainActivity.this,listMovie);
                        recyclerViewListMovie.setAdapter(adapter);

                    }
                });
            }
        });
    }

}
