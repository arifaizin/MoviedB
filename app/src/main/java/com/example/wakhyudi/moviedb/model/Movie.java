package com.example.wakhyudi.moviedb.model;

import android.database.Cursor;

import com.example.wakhyudi.moviedb.database.DatabaseHelper;

import static com.example.wakhyudi.moviedb.database.DatabaseHelper.getColumnString;

/**
 * Created by wakhyudi on 03/06/17.
 */

public class Movie {
    private String backdropPath;
    private String title;
    private String releaseDate;
    private String overview;

    public Movie(String backdropPath,String title,String releaseDate, String overview){
        this.backdropPath = backdropPath;
        this.title = title;
        this.releaseDate = releaseDate;
        this.overview = overview;
    }

    public Movie(Cursor cursor){
        this.backdropPath = getColumnString(cursor, DatabaseHelper.MOVIE_PICTURE);
        this.title = getColumnString(cursor, DatabaseHelper.MOVIE_TITLE);
        this.releaseDate = getColumnString(cursor, DatabaseHelper.MOVIE_RELEASE);
        this.overview = getColumnString(cursor, DatabaseHelper.MOVIE_OVERVIEW);
    }

    public String getBackdropPath() {
        return backdropPath;
    }
    public String getTitle() {
        return title;
    }
    public String getReleaseDate() {
        return releaseDate;
    }
    public String getOverview() {
        return overview;
    }






}
