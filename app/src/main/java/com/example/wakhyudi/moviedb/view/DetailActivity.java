package com.example.wakhyudi.moviedb.view;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.wakhyudi.moviedb.R;
import com.example.wakhyudi.moviedb.database.DatabaseHelper;

import static com.example.wakhyudi.moviedb.database.DatabaseHelper.MOVIE_TITLE;
import static com.example.wakhyudi.moviedb.provider.MovFavProvider.CONTENT_URI;

public class DetailActivity extends AppCompatActivity {
ImageView imGambarMovie;
TextView tvTitle, tvRelease, tvOverview;
ImageView ivFav;
Boolean isFav;
SharedPreferences pref;
DatabaseHelper database = new DatabaseHelper(this);
Bundle b;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        getSupportActionBar().setTitle(R.string.movie_detail);
        imGambarMovie = (ImageView)findViewById(R.id.img_detail);
        tvTitle = (TextView)findViewById(R.id.tv_title_detail);
        tvRelease = (TextView)findViewById(R.id.tv_release_detail);
        tvOverview = (TextView)findViewById(R.id.tv_overview_detail);
        ivFav = (ImageView)findViewById(R.id.iv_fav);
        b = getIntent().getExtras();

        String pathGambar = b.getString("gambar");
        Glide.with(this).load("https://image.tmdb.org/t/p/w500"+pathGambar).into(imGambarMovie);
        tvTitle.setText(b.getString("judul"));
        tvRelease.setText(b.getString("release"));
        tvOverview.setText(b.getString("overview"));
        pref = getSharedPreferences("moviefav",MODE_PRIVATE);
        isFav = pref.getBoolean("fav"+b.getString("judul"),false);

        cekFavorit(isFav);
ivFav.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        if(isFav){

            long id = getContentResolver().delete(CONTENT_URI,MOVIE_TITLE+ " = ?",new String[]{b.getString("judul")});
            //long id = database.delete(b.getString("judul"));

            if(id<=0){
                Toast.makeText(getApplicationContext(),"movie gagal dihapus dari favorit",Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(getApplicationContext(),"movie berhasil dihapus dari favorit",Toast.LENGTH_SHORT).show();
                SharedPreferences.Editor editor = pref.edit();
                editor.putBoolean("fav"+b.getString("judul"),false);
                editor.commit();
                isFav = false;
            }

        }else{
            ContentValues cv = new ContentValues();
            cv.put("title", b.getString("judul"));
            cv.put("pict", b.getString("gambar"));
            cv.put("release",b.getString("release"));
            cv.put("overview",b.getString("overview"));
            Uri uri = getContentResolver().insert(CONTENT_URI,cv);

            long newID= ContentUris.parseId(uri);
//            long id = database.insertData(b.getString("judul"),
//                    b.getString("gambar"),
//                    b.getString("release"),
//                    b.getString("overview"));
            if(newID>1){
                Toast.makeText(getApplicationContext(),"movie berhasil disimpan ke favorit",Toast.LENGTH_SHORT).show();
                SharedPreferences.Editor editor = pref.edit();
                editor.putBoolean("fav"+b.getString("judul"),true);
                editor.commit();
                isFav = true;
            }else{
                Toast.makeText(getApplicationContext(),"movie gagal disimpan ke favorit",Toast.LENGTH_SHORT).show();

            }


        }

        cekFavorit(isFav);
    }
});

    }

    private void cekFavorit(Boolean isFav) {

        if(isFav){
            ivFav.setImageResource(R.drawable.ic_fav);
        }else{
            ivFav.setImageResource(R.drawable.ic_nofav);
        }
    }

//    @Override
//    public void onBackPressed() {
//        super.onBackPressed();
//        startActivity(new Intent(this,MainActivity.class));
//        finish();
//    }
}
