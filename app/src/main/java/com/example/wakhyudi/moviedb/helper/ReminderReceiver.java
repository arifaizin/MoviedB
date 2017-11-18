package com.example.wakhyudi.moviedb.helper;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.support.v4.content.ContextCompat;
import android.widget.Toast;

import com.example.wakhyudi.moviedb.R;
import com.example.wakhyudi.moviedb.view.NavDrawerActivity;

import java.util.Calendar;

public class ReminderReceiver extends BroadcastReceiver {
public static final String TYPE_REMINDER = "reminderAlarm";
public static final String EXTRA_MESSAGE = "message";
public static final String EXTRA_TYPE = "type";

private final int NOTIF_ID_REMINDER = 101;

    public ReminderReceiver() {

    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String type = intent.getStringExtra(EXTRA_TYPE);
        String message = intent.getStringExtra(EXTRA_MESSAGE);
        String title = "Reminder Movie";
        int notifId = NOTIF_ID_REMINDER;  //type.equal

        showReminderNotification(context,title,message,notifId);
    }


    //bagian notifikasi
    private void showReminderNotification(Context context, String title, String message, int notifId){
        NotificationManager  notificationManagerCompat = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        Intent intent = new Intent(context, NavDrawerActivity.class);
        PendingIntent pendingIntent = TaskStackBuilder.create(context)
//                .addParentStack(DetailActivity.class)
                .addNextIntent(intent)
                .getPendingIntent(NOTIF_ID_REMINDER,PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                .setSmallIcon(R.drawable.smile_sedih)
                .setContentTitle(title)
                .setContentText(message)
                .setContentIntent(pendingIntent)
                .setColor(ContextCompat.getColor(context,android.R.color.transparent))
                .setVibrate(new long[]{1000, 1000, 1000, 1000, 1000})
                .setSound(alarmSound);

        notificationManagerCompat.notify(notifId,builder.build());
    }

    //bagian setting reminder waktunya
    public void setReminder(Context context, String type, String time, String message){
        AlarmManager alarmManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, ReminderReceiver.class);
        intent.putExtra(EXTRA_MESSAGE,message);
        intent.putExtra(EXTRA_TYPE,type);
        String timeArray[] = time.split(":");
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY,Integer.parseInt(timeArray[0]));
        calendar.set(Calendar.MINUTE,Integer.parseInt(timeArray[1]));
        calendar.set(Calendar.SECOND,0);

        int requestCode = NOTIF_ID_REMINDER;
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context,requestCode,intent,0);
        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);

        Toast.makeText(context,"Reminder berhasil disimpan",Toast.LENGTH_SHORT).show();
    }

    public void cancelReminder(Context context){
        AlarmManager alarmManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context,ReminderReceiver.class);
        int requestCode = NOTIF_ID_REMINDER;
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context,requestCode,intent,0);
        alarmManager.cancel(pendingIntent);
        Toast.makeText(context,"Reminder berhasil dihilangkan",Toast.LENGTH_SHORT).show();
    }
}
