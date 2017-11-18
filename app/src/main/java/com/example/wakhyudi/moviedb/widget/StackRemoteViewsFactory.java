package com.example.wakhyudi.moviedb.widget;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.os.Binder;
import android.os.Bundle;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.Target;
import com.example.wakhyudi.moviedb.R;
import com.example.wakhyudi.moviedb.database.DatabaseHelper;
import com.example.wakhyudi.moviedb.model.Movie;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import static com.example.wakhyudi.moviedb.provider.MovFavProvider.CONTENT_URI;

/**
 * Created by DELLL on 11/18/2017.
 */

public class StackRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {

    private List<String> mWidgetItems = new ArrayList<>();
    private Context mContext;
    private int mAppWidgetId;
    private DatabaseHelper database;
    private Cursor cursor;
    private static final String TAG = "StackRemoteViewsFactory";
    private RemoteViews rv;

    public StackRemoteViewsFactory(Context context, Intent intent) {
        mContext = context;
        mAppWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
                AppWidgetManager.INVALID_APPWIDGET_ID);
    }
    @Override
    public void onCreate() {
//        mWidgetItems.add(BitmapFactory.decodeResource(mContext.getResources(),
//                R.drawable.example_appwidget_preview));
//        mWidgetItems.add(BitmapFactory.decodeResource(mContext.getResources(),
//                R.drawable.example_appwidget_preview));
//        mWidgetItems.add(BitmapFactory.decodeResource(mContext.getResources(),
//                R.drawable.example_appwidget_preview));
//        mWidgetItems.add(BitmapFactory.decodeResource(mContext.getResources(),R.drawable.example_appwidget_preview));
//        mWidgetItems.add(BitmapFactory.decodeResource(mContext.getResources(),R.drawable.example_appwidget_preview));
    }

    @Override
    public void onDataSetChanged() {
//        getFavoriteMovies(mContext);

        //https://stackoverflow.com/questions/9497270/widget-with-content-provider-impossible-to-use-readpermission
        final long identityToken = Binder.clearCallingIdentity();

        // Update your cursor or whaterver here
        // ...

        getFavoriteMovies(mContext);

        // Restore the identity - not sure if it's needed since we're going
        // to return right here, but it just *seems* cleaner
        Binder.restoreCallingIdentity(identityToken);
    }

    private void getFavoriteMovies(Context mContext) {
        database = new DatabaseHelper(mContext);
        cursor = mContext.getContentResolver().query(CONTENT_URI,null,null,null,null);

        for (int i = 0; i < cursor.getCount(); i++) {
            Movie movie = getItem(i);
//            holder.judul.setText(movie.getTitle());
//            holder.release.setText(movie.getReleaseDate());
            final String pathGambar = movie.getBackdropPath();
            Log.d(TAG, "getFavoriteMovies: "+ pathGambar);
//            Glide.with(context).load("https://image.tmdb.org/t/p/w500"+pathGambar).into(holder.imageMovie);
            mWidgetItems.add(pathGambar);
        }


    }

    private Movie getItem(int position){
        if (!cursor.moveToPosition(position)) {
            throw new IllegalStateException("Position invalid");
        }
        return new Movie(cursor);
    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        return mWidgetItems.size();
    }

    @Override
    public RemoteViews getViewAt(int position) {
        rv = new RemoteViews(mContext.getPackageName(), R.layout.widget_item);
//        rv.setImageViewBitmap(R.id.imageView,mWidgetItems.get(position));

        Bitmap bmp = null;
        try {

            bmp = Glide.with(mContext)
                    .load("https://image.tmdb.org/t/p/w500" + mWidgetItems.get(position))
                    .asBitmap()
                    .error(new ColorDrawable(mContext.getResources().getColor(android.R.color.darker_gray)))
                    .into(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL).get();

        }catch (InterruptedException | ExecutionException e){
            Log.d("Widget Load Error","error");
        }

        rv.setImageViewBitmap(R.id.imageView,bmp);

        Bundle extras = new Bundle();
        extras.putInt(MovieWidget.EXTRA_ITEM, position);
        Intent fillInIntent = new Intent();
        fillInIntent.putExtras(extras);

        rv.setOnClickFillInIntent(R.id.imageView, fillInIntent);
        return rv;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;

    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }
}
