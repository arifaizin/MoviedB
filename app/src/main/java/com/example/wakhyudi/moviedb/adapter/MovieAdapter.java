package com.example.wakhyudi.moviedb.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.wakhyudi.moviedb.view.DetailActivity;
import com.example.wakhyudi.moviedb.R;
import com.example.wakhyudi.moviedb.model.Movie;

import java.util.ArrayList;

/**
 * Created by wakhyudi on 03/06/17.
 */

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder> {
    private Context context;
    private ArrayList<Movie>movies;
    int positionMovie;
    public MovieAdapter(Context context, ArrayList<Movie>movies) {
        this.context = context;
        this.movies = movies;
    }

    @Override
    public MovieAdapter.MovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
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
    public void onBindViewHolder(MovieAdapter.MovieViewHolder holder, int position) {
        positionMovie = position;
        holder.judul.setText(movies.get(position).getTitle());
        holder.release.setText(movies.get(position).getReleaseDate());
        final String pathGambar = movies.get(position).getBackdropPath();
        Glide.with(context).load("https://image.tmdb.org/t/p/w500"+pathGambar).into(holder.imageMovie);
        holder.llParent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(),DetailActivity.class);
                Bundle b = new Bundle();
                b.putString("gambar",pathGambar);
                b.putString("judul",movies.get(positionMovie).getTitle());
                b.putString("release",movies.get(positionMovie).getReleaseDate());
                b.putString("overview",movies.get(positionMovie).getOverview());
                intent.putExtras(b);
                v.getContext().startActivity(intent);
            }
        });



    }

    @Override
    public int getItemCount() {
        return movies.size();
    }



}
