package org.indihome.sandec.movfavapp.database;

import android.database.Cursor;
import android.net.Uri;

/**
 * Created by wakhyudi on 15/11/17.
 */

public class DatabaseHelper {

    public final static String TABLE_NAME = "favorite_movie";
    public final static String MOVIE_ID = "_id";
    public final static String MOVIE_TITLE = "title";
    public final static String MOVIE_PICTURE = "pict";
    public final static String MOVIE_RELEASE = "release";
    public final static String MOVIE_OVERVIEW = "overview";

    // Authority yang digunakan
    public static final String AUTHORITY = "com.example.wakhyudi.moviedb";

    // Base content yang digunakan untuk akses content provider
    public static final Uri CONTENT_URI = new Uri.Builder().scheme("content")
            .authority(AUTHORITY)
            .appendPath(DatabaseHelper.TABLE_NAME)
            .build();

    public static String getColumnString(Cursor cursor, String columnName) {
        return cursor.getString( cursor.getColumnIndex(columnName) );
    }

    public static int getColumnInt(Cursor cursor, String columnName) {
        return cursor.getInt( cursor.getColumnIndex(columnName) );
    }

}
