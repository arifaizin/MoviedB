package org.indihome.sandec.movfavapp;


import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import org.indihome.sandec.movfavapp.adapter.MovFavAdapter;

import static org.indihome.sandec.movfavapp.database.DatabaseHelper.CONTENT_URI;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {
    private MovFavAdapter adapter;
    private final int LOAD_MOV_FAV_ID = 200;
    ListView lvMovFav;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        lvMovFav = (ListView)findViewById(R.id.lv_mov_fav);

        adapter = new MovFavAdapter(this,null,true);
        lvMovFav.setAdapter(adapter);

        getSupportLoaderManager().initLoader(LOAD_MOV_FAV_ID,null,this);

    }

    @Override
    protected void onResume() {
        super.onResume();
        getSupportLoaderManager().restartLoader(LOAD_MOV_FAV_ID, null, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(this,CONTENT_URI,null,null,null,null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        adapter.swapCursor(data);
    }



    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        adapter.swapCursor(null);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        getSupportLoaderManager().destroyLoader(LOAD_MOV_FAV_ID);
    }
}
