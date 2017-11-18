package com.example.wakhyudi.moviedb.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.wakhyudi.moviedb.R;
import com.example.wakhyudi.moviedb.model.Movie;
import com.example.wakhyudi.moviedb.view.DetailActivity;

import java.util.ArrayList;

/**
 * Created by wakhyudi on 04/10/17.
 */

public class MovieNowAdapter extends RecyclerView.Adapter<MovieNowAdapter.MovieNowViewHolder> {
    private Context context;
    private ArrayList<Movie>listMovie;

    public MovieNowAdapter(Context context, ArrayList<Movie> listMovie) {
        this.context = context;
        this.listMovie = listMovie;
    }

    @Override
    public MovieNowAdapter.MovieNowViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_movie_playing_now,parent,false);

        return new MovieNowViewHolder(view);
    }

    public class MovieNowViewHolder extends RecyclerView.ViewHolder {
        ImageView imMovie;
        public MovieNowViewHolder(View itemView) {
            super(itemView);
            imMovie = (ImageView) itemView.findViewById(R.id.im_now_playing);
        }
    }

    @Override
    public void onBindViewHolder(MovieNowAdapter.MovieNowViewHolder holder, final int position) {
        final String pathGambar = listMovie.get(position).getBackdropPath();
        Glide.with(context).load("https://image.tmdb.org/t/p/w500"+pathGambar).into(holder.imMovie);

        holder.imMovie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(),DetailActivity.class);
                Bundle b = new Bundle();
                b.putString("gambar",pathGambar);
                b.putString("judul",listMovie.get(position).getTitle());
                b.putString("release",listMovie.get(position).getReleaseDate());
                b.putString("overview",listMovie.get(position).getOverview());
                intent.putExtras(b);
                v.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return listMovie.size();
    }


}
