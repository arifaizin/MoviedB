package org.indihome.sandec.movfavapp.adapter;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import org.indihome.sandec.movfavapp.R;

import static org.indihome.sandec.movfavapp.database.DatabaseHelper.MOVIE_PICTURE;
import static org.indihome.sandec.movfavapp.database.DatabaseHelper.MOVIE_RELEASE;
import static org.indihome.sandec.movfavapp.database.DatabaseHelper.MOVIE_TITLE;
import static org.indihome.sandec.movfavapp.database.DatabaseHelper.getColumnString;

/**
 * Created by wakhyudi on 15/11/17.
 */

public class MovFavAdapter extends CursorAdapter {
    public MovFavAdapter(Context context, Cursor c, boolean autoRequery) {
        super(context, c, autoRequery);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_mov_fav,parent,false);

        return view;
    }

    @Override
    public Cursor getCursor() {
        return super.getCursor();
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        if(cursor !=null){
            TextView tvTitle = (TextView)view.findViewById(R.id.tv_title_value_fav);
            TextView tvRelease = (TextView)view.findViewById(R.id.tv_release_value_fav);
            ImageView ivPict = (ImageView)view.findViewById(R.id.iv_movie_fav);

            tvTitle.setText(getColumnString(cursor,MOVIE_TITLE));
            tvRelease.setText(getColumnString(cursor,MOVIE_RELEASE));
            String pathGambar = getColumnString(cursor,MOVIE_PICTURE);
            Glide.with(context).load("https://image.tmdb.org/t/p/w500"+pathGambar).into(ivPict);

        }
    }
}
