package com.example.wakhyudi.moviedb.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.wakhyudi.moviedb.model.Movie;

import java.util.ArrayList;

import static android.provider.BaseColumns._ID;

/**
 * Created by wakhyudi on 10/11/17.
 */

public class DatabaseHelper extends SQLiteOpenHelper {
    public final static String DATABASE_NAME = "moviefavdb";
    public final static String TABLE_NAME = "favorite_movie";
    public final static String MOVIE_ID = "_id";
    public final static String MOVIE_TITLE = "title";
    public final static String MOVIE_PICTURE = "pict";
    public final static String MOVIE_RELEASE = "release";
    public final static String MOVIE_OVERVIEW = "overview";


    public final static int DATABASE_VERSION = 2;

    public static String getColumnString(Cursor cursor, String columnName) {
        return cursor.getString( cursor.getColumnIndex(columnName) );
    }
    public static int getColumnInt(Cursor cursor, String columnName) {
        return cursor.getInt( cursor.getColumnIndex(columnName) );
    }
    public static long getColumnLong(Cursor cursor, String columnName) {
        return cursor.getLong( cursor.getColumnIndex(columnName) );
    }


    public final static String CREATE_TABLE = "CREATE TABLE "+TABLE_NAME+" ("
            +MOVIE_ID+" INTEGER PRIMARY KEY AUTOINCREMENT, "
            +MOVIE_TITLE+" TEXT , "
            +MOVIE_PICTURE+" TEXT, "
            +MOVIE_RELEASE+" TEXT, "
            +MOVIE_OVERVIEW+" TEXT);";
    private Context context;

    DatabaseHelper databaseHelper;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }



    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME);
        onCreate(db);
    }

    private SQLiteDatabase database;



//    public DatabaseHelper open() throws SQLException {
//        databaseHelper = new DatabaseHelper(context);
//        database = databaseHelper.getWritableDatabase();
//        return this;
//    }
//
//    public void close(){
//        databaseHelper.close();
//    }

    public long insertData(String title, String pict, String release, String overview){
        //kalau gagal <= 0
        // kalau berhasil > 0
        //saat insert kita dapat id
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("title", title);
        cv.put("pict", pict);
        cv.put("release",release);
        cv.put("overview",overview);

        
        long id = db.insert(TABLE_NAME, null, cv);
        db.close();
        return id;
    }

    //menghapus data favorite di SQLite
    public long delete(String title){
        SQLiteDatabase db = this.getWritableDatabase();
        String namaKolom = MOVIE_TITLE + " = ?";
        String [] isikolom = {title};

        int count = db.delete(TABLE_NAME,namaKolom,isikolom);
        db.close();
        return count;
    }

    public ArrayList<Movie> getDataFavorite() {
        ArrayList<Movie> listWisataFavorite = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String[] columnName = {MOVIE_ID, MOVIE_TITLE, MOVIE_PICTURE, MOVIE_RELEASE,MOVIE_OVERVIEW};
        Cursor kursor = db.query(
                TABLE_NAME,
                columnName,
                null, null, null, null, null);
        if (kursor != null) {
            while (kursor.moveToNext()) {
                int idMovie = kursor.getInt(kursor.getColumnIndex(MOVIE_ID));
                String titleMovie = kursor.getString(kursor.getColumnIndex(MOVIE_TITLE));
                String pictMovie = kursor.getString(kursor.getColumnIndex(MOVIE_PICTURE));
                String releaseMovie = kursor.getString(kursor.getColumnIndex(MOVIE_RELEASE));
                String overviewMovie = kursor.getString(kursor.getColumnIndex(MOVIE_OVERVIEW));


                Movie movieFavorite = new Movie(pictMovie,titleMovie,releaseMovie,overviewMovie);


                listWisataFavorite.add(movieFavorite);
            }
        }

        db.close();
        return listWisataFavorite;
    }
    
    
    /*
    METHOD DI BAWAH INI ADALAH QUERY UNTUK CONTENT PROVIDER
    NILAI BALIK CURSOR
    */

    /**
     * Ambil data dari note berdasarakan parameter id
     * Gunakan method ini untuk ambil data di dalam provider
     * @param id id note yang dicari
     * @return cursor hasil query
     */
    public Cursor queryByIdProvider(String id){
        SQLiteDatabase database = this.getWritableDatabase();
        return database.query(TABLE_NAME,null
                ,_ID + " = ?"
                ,new String[]{id}
                ,null
                ,null
                ,null
                ,null);
    }

    /**
     * Ambil data dari semua note yang ada di dalam database
     * Gunakan method ini untuk ambil data di dalam provider
     * @return cursor hasil query
     */
    public Cursor queryProvider(){
        SQLiteDatabase database = this.getWritableDatabase();
        return database.query(TABLE_NAME
                ,null
                ,null
                ,null
                ,null
                ,null
                ,MOVIE_TITLE + " DESC");
    }

    /**
     * Simpan data ke dalam database
     * Gunakan method ini untuk query insert di dalam provider
     * @param values nilai data yang akan di simpan
     * @return long id dari data yang baru saja di masukkan
     */
    public long insertProvider(ContentValues values){
        SQLiteDatabase database = this.getWritableDatabase();
        return database.insert(TABLE_NAME,null,values);
    }

    /**
     * Update data dalam database
     * @param id data dengan id berapa yang akan di update
     * @param values nilai data baru
     * @return int jumlah data yang ter-update
     */
    public int updateProvider(String id,ContentValues values){
        SQLiteDatabase database = this.getWritableDatabase();
        return database.update(TABLE_NAME,values,_ID +" = ?",new String[]{id} );
    }

    /**
     * Delete data dalam database
     * @param id data dengan judul yang akan di delete
     * @return int jumlah data yang ter-delete
     */
    public int deleteProvider(String id){
        SQLiteDatabase database = this.getWritableDatabase();
        return database.delete(TABLE_NAME,MOVIE_ID + " = ?", new String[]{id});
    }

}
