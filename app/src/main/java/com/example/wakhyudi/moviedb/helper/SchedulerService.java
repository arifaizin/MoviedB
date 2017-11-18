package com.example.wakhyudi.moviedb.helper;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.example.wakhyudi.moviedb.R;
import com.example.wakhyudi.moviedb.view.DetailActivity;
import com.google.android.gms.gcm.GcmNetworkManager;
import com.google.android.gms.gcm.GcmTaskService;
import com.google.android.gms.gcm.TaskParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by wakhyudi on 27/09/17.
 */

public class SchedulerService extends GcmTaskService {
    public static final String TAG = "GetMovie";
    private final String API_KEY ="a27648284b6aa518fd4ceeff26cdc383";
    public static String TAG_TASK_MOVIE_LOG = "MovieTask";

    @Override
    public int onRunTask(TaskParams taskParams) {
        int result = 0;
        if(taskParams.getTag().equals(TAG_TASK_MOVIE_LOG)){
            getCurrentMovie();
            result = GcmNetworkManager.RESULT_SUCCESS;
        }
        return result;
    }

    private void getCurrentMovie(){
        String url = "https://api.themoviedb.org/3/movie/upcoming?api_key="+API_KEY+"&language=en-US";

        OkHttpClient network =  new OkHttpClient();
        Request request = new Request.Builder()
                                .url(url)
                                .build();

        network.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d(TAG,e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String jsonData = response.body().string();

                try {
                    JSONObject jsonObject = new JSONObject(jsonData);
                    JSONArray jsonArray = jsonObject.getJSONArray("results");
                    JSONObject data = jsonArray.getJSONObject(0);
                    String title = data.getString("title");
                    String pathGambar =  data.getString("poster_path");
                    String release = data.getString("release_date");
                    String overview = data.getString("overview");
                    int notifId =102;
                    showNotification(getApplicationContext(),title,release,notifId,overview,pathGambar);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        });


    }

    @Override
    public void onInitializeTasks() {
        super.onInitializeTasks();
        SchedulerTask mSchedulerTask = new SchedulerTask(this);
        mSchedulerTask.createPeriodicTask();
    }

    private void showNotification(Context context, String title, String message, int notifId, String overview, String pathGambar){
        NotificationManager notificationManagerCompat = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        Intent intent = new Intent(context, DetailActivity.class);

        Bundle b = new Bundle();
        b.putString("gambar",pathGambar);
        b.putString("judul",title);
        b.putString("release",message);
        b.putString("overview",overview);

        intent.putExtras(b);
        PendingIntent pendingIntent = TaskStackBuilder.create(context)
//                .addParentStack(DetailActivity.class)
                .addNextIntent(intent)
                .getPendingIntent(102,PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                .setContentTitle(title)
                .setSmallIcon(R.drawable.wink)
                .setContentText("Release pada : "+message)
                .setColor(ContextCompat.getColor(context,android.R.color.black))
                .setVibrate(new long[]{1000, 1000, 1000, 1000, 1000})
                .setSound(alarmSound)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);

        notificationManagerCompat.notify(notifId,builder.build());
    }


}
