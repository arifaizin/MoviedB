package com.example.wakhyudi.moviedb.view;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.wakhyudi.moviedb.R;
import com.example.wakhyudi.moviedb.adapter.MovieFavAdapter;
import com.example.wakhyudi.moviedb.database.DatabaseHelper;

import static com.example.wakhyudi.moviedb.provider.MovFavProvider.CONTENT_URI;

public class FavoriteActivity extends AppCompatActivity {
    RecyclerView rvFav;
    DatabaseHelper database;
    Cursor cursor;
    //ArrayList<Movie>listMovie = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite);
        rvFav = (RecyclerView)findViewById(R.id.rv_fav);
        database = new DatabaseHelper(this);
        rvFav.setLayoutManager(new LinearLayoutManager(this));

        cursor = getContentResolver().query(CONTENT_URI,null,null,null,null);

        //listMovie = database.getDataFavorite();

        MovieFavAdapter adapter = new MovieFavAdapter(this,cursor);

        rvFav.setAdapter(adapter);

    }
}
