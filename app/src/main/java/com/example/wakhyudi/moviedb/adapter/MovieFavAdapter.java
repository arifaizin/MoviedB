package com.example.wakhyudi.moviedb.adapter;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.wakhyudi.moviedb.R;
import com.example.wakhyudi.moviedb.model.Movie;

/**
 * Created by wakhyudi on 15/11/17.
 */

public class MovieFavAdapter extends RecyclerView.Adapter<MovieFavAdapter.MovieViewHolder> {
    private Cursor listMovie;
    private Context context;
    //private ArrayList<Movie> movies;
    int positionMovie;
    public MovieFavAdapter(Context context, Cursor listMovie) {
        this.context = context;
        this.listMovie = listMovie;
    }

    @Override
    public MovieFavAdapter.MovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.list_item_movie,parent,false);
        MovieViewHolder movieViewHolder = new MovieViewHolder(view);
        return movieViewHolder;
    }

    public class MovieViewHolder extends RecyclerView.ViewHolder {
        ImageView imageMovie;
        TextView judul;
        TextView release;
        TextView overview;
        LinearLayout llParent;
        public MovieViewHolder(View itemView) {
            super(itemView);
            imageMovie = (ImageView)itemView.findViewById(R.id.image_view_movie);
            judul      = (TextView)itemView.findViewById(R.id.tv_title_value);
            release    = (TextView)itemView.findViewById(R.id.tv_release_value);
            llParent   = (LinearLayout)itemView.findViewById(R.id.ll_parent);
        }
    }

    @Override
    public void onBindViewHolder(MovieFavAdapter.MovieViewHolder holder, int position) {
        positionMovie = position;
        Movie movie = getItem(positionMovie);
        holder.judul.setText(movie.getTitle());
        holder.release.setText(movie.getReleaseDate());
        final String pathGambar = movie.getBackdropPath();
        Glide.with(context).load("https://image.tmdb.org/t/p/w500"+pathGambar).into(holder.imageMovie);

    }

    @Override
    public int getItemCount() {
        if (listMovie == null) return 0;
        return listMovie.getCount();
    }

    private Movie getItem(int position){
        if (!listMovie.moveToPosition(position)) {
            throw new IllegalStateException("Position invalid");
        }
        return new Movie(listMovie);
    }



}
