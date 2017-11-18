package com.example.wakhyudi.moviedb.model;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by wakhyudi on 26/09/17.
 */

public class ReminderMoviePreference {
    private final static String PREF_NAME = "reminderMoviePreferences";
    private final static String KEY_REMINDER_MOVIE_TIME = "reminderTime";
    private final static String KEY_REMINDER_MESSAGE = "reminderMessage";

    public SharedPreferences sharedPreferences;
    public SharedPreferences.Editor editor;

    public ReminderMoviePreference(Context context) {
        sharedPreferences = context.getSharedPreferences(PREF_NAME,Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public void setReminderTime(String time){
        editor.putString(KEY_REMINDER_MOVIE_TIME,time);
        editor.commit();
    }

    public String getReminderTime(){
        return sharedPreferences.getString(KEY_REMINDER_MOVIE_TIME,null);
    }

    public void setReminderMessage (String message){
        editor.putString(KEY_REMINDER_MESSAGE,message);
    }

    public String getReminderMessage(){
        return  sharedPreferences.getString(KEY_REMINDER_MESSAGE,null);
    }

    public void clear(){
        editor.clear();
        editor.commit();
    }
}
