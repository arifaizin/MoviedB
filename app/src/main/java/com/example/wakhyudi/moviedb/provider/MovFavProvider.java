package com.example.wakhyudi.moviedb.provider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;

import com.example.wakhyudi.moviedb.database.DatabaseHelper;

/**
 * Created by wakhyudi on 10/11/17.
 */

public class MovFavProvider extends ContentProvider {

    /*
    Integer digunakan sebagai identifier antara select all sama select by id
     */
    private static final int MOV = 1;
    private static final int MOV_ID = 2;

    // Authority yang digunakan
    public static final String AUTHORITY = "com.example.wakhyudi.moviedb";

    // Base content yang digunakan untuk akses content provider
    public static final Uri CONTENT_URI = new Uri.Builder().scheme("content")
            .authority(AUTHORITY)
            .appendPath(DatabaseHelper.TABLE_NAME)
            .build();


    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    /*
    Uri matcher untuk mempermudah identifier dengan menggunakan integer
    misal
    uri com.example.wakhyudi.moviedb di cocokan dengan integer 1
    uri com.example.wakhyudi.moviedb/# dicodokan dengan integer 2
     */
    static {

        // content://com.example.wakhyudi.moviedb/favorite_movie
        sUriMatcher.addURI(AUTHORITY, DatabaseHelper.TABLE_NAME, MOV);

        // content://com.example.wakhyudi.moviedb/favorite_movie/id
        sUriMatcher.addURI(AUTHORITY,
                DatabaseHelper.TABLE_NAME+ "/#",
                MOV_ID);
    }

    private DatabaseHelper databaseHelper;

    @Override
    public boolean onCreate() {
        databaseHelper = new DatabaseHelper(getContext());
        databaseHelper.getWritableDatabase();
        return true;
    }


    /*
    Method query digunakan ketika ingin menjalankan query Select
    Return cursor
     */
    @Override
    public Cursor query(@NonNull Uri uri, String[] strings, String s, String[] strings1, String s1) {
        Cursor cursor;
        switch(sUriMatcher.match(uri)){
            case MOV:
                cursor = databaseHelper.queryProvider();
                break;
            case MOV_ID:
                cursor = databaseHelper.queryByIdProvider(uri.getLastPathSegment());
                break;
            default:
                cursor = null;
                break;
        }

        if (cursor != null){
            cursor.setNotificationUri(getContext().getContentResolver(),uri);
        }

        return cursor;
    }


    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Override
    public Uri insert(@NonNull Uri uri, ContentValues contentValues) {

        long added ;

        switch (sUriMatcher.match(uri)){
            case MOV:
                added = databaseHelper.insertProvider(contentValues);
                break;
            default:
                added = 0;
                break;
        }

        if (added > 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return Uri.parse(CONTENT_URI + "/" + added);
    }


    @Override
    public int update(@NonNull Uri uri, ContentValues contentValues, String s, String[] strings) {
        int updated ;
        switch (sUriMatcher.match(uri)) {
            case MOV_ID:
                updated =  databaseHelper.updateProvider(uri.getLastPathSegment(),contentValues);
                break;
            default:
                updated = 0;
                break;
        }

        if (updated > 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return updated;
    }

    @Override
    public int delete(@NonNull Uri uri, String s, String[] strings) {
        int deleted;
        switch (sUriMatcher.match(uri)) {
            case MOV_ID:
                deleted =  databaseHelper.deleteProvider(uri.getLastPathSegment());
                break;
            default:
                deleted = 0;
                break;
        }

        if (deleted > 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return deleted;
    }

}
