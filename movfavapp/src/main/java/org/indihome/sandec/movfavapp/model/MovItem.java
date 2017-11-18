package org.indihome.sandec.movfavapp.model;

import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

import org.indihome.sandec.movfavapp.database.DatabaseHelper;

import static org.indihome.sandec.movfavapp.database.DatabaseHelper.MOVIE_ID;
import static org.indihome.sandec.movfavapp.database.DatabaseHelper.getColumnInt;
import static org.indihome.sandec.movfavapp.database.DatabaseHelper.getColumnString;

/**
 * Created by wakhyudi on 15/11/17.
 */

public class MovItem implements Parcelable{
    private int id;
    private String backdropPath;
    private String title;
    private String releaseDate;
    private String overview;

    public MovItem(){}

    public MovItem(Cursor cursor){
        this.backdropPath = getColumnString(cursor, DatabaseHelper.MOVIE_PICTURE);
        this.title = getColumnString(cursor, DatabaseHelper.MOVIE_TITLE);
        this.releaseDate = getColumnString(cursor, DatabaseHelper.MOVIE_RELEASE);
        this.overview = getColumnString(cursor, DatabaseHelper.MOVIE_OVERVIEW);
        this.id = getColumnInt(cursor,MOVIE_ID);
    }

    protected MovItem(Parcel in) {
        id = in.readInt();
        backdropPath = in.readString();
        title = in.readString();
        releaseDate = in.readString();
        overview = in.readString();
    }

    public static final Creator<MovItem> CREATOR = new Creator<MovItem>() {
        @Override
        public MovItem createFromParcel(Parcel in) {
            return new MovItem(in);
        }

        @Override
        public MovItem[] newArray(int size) {
            return new MovItem[size];
        }
    };

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
    public int getId() {
        return id;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(backdropPath);
        dest.writeString(title);
        dest.writeString(releaseDate);
        dest.writeString(overview);
    }
}
