package com.example.wakhyudi.moviedb.view;

import android.app.TimePickerDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.wakhyudi.moviedb.R;
import com.example.wakhyudi.moviedb.helper.ReminderReceiver;
import com.example.wakhyudi.moviedb.helper.SchedulerTask;
import com.example.wakhyudi.moviedb.model.ReminderMoviePreference;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class NotificationSettingActivity extends AppCompatActivity {
public Switch swDailyReminder, swUpcomingReminder;
public SchedulerTask schedulerTask;
public ReminderReceiver reminderReceiver;
public ReminderMoviePreference reminderMoviePreference;
public final static String KEY_HEADER_UPCOMING_REMINDER = "upcomingReminder";
public final static String KEY_HEADER_DAILY_REMINDER = "dailyReminder";
public final static String KEY_FIELD_UPCOMING_REMINDER = "checkedUpcoming";
public final static String KEY_FIELD_DAILY_REMINDER = "checkedDaily";
public SharedPreferences spUpcomingReminder, spDailyReminder;
public SharedPreferences.Editor editorUpcomingReminder, editorDailyReminder;

public Button btnReminderDailyNotification;
public TextView tvReminderDailyNotification;
public LinearLayout llDailyNotification;
public Calendar calReminder;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_setting);
        getSupportActionBar().setTitle(R.string.movie_notification_setting);
        swDailyReminder = (Switch)findViewById(R.id.sw_daily_reminder);
        swUpcomingReminder = (Switch)findViewById(R.id.sw_upcoming_reminder);
        btnReminderDailyNotification = (Button)findViewById(R.id.btn_reminder_notification);
        tvReminderDailyNotification = (TextView)findViewById(R.id.tv_time_daily_reminder_notification);
        llDailyNotification = (LinearLayout)findViewById(R.id.ll_daily_reminder_detail);

        //mencetak objek
        reminderReceiver = new ReminderReceiver();
        reminderMoviePreference = new ReminderMoviePreference(this);
        schedulerTask = new SchedulerTask(this);
        calReminder = Calendar.getInstance();


        //mengakses sharedpreferences yang ada di memory
        spUpcomingReminder = getSharedPreferences(KEY_HEADER_UPCOMING_REMINDER,MODE_PRIVATE);
        spDailyReminder = getSharedPreferences(KEY_HEADER_DAILY_REMINDER,MODE_PRIVATE);

        //inisialisasi keadaan Reminder dari sharedpreferences
        boolean checkSwUpcomingReminder = spUpcomingReminder.getBoolean(KEY_FIELD_UPCOMING_REMINDER,false);
        swUpcomingReminder.setChecked(checkSwUpcomingReminder);

        boolean checkSwDailyReminder = spDailyReminder.getBoolean(KEY_FIELD_DAILY_REMINDER,false);
        swDailyReminder.setChecked(checkSwDailyReminder);

        //Reminder Upcoming di off atau di on kan
        swUpcomingReminder.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                editorUpcomingReminder = spUpcomingReminder.edit();
                if(swUpcomingReminder.isChecked()){
                    editorUpcomingReminder.putBoolean(KEY_FIELD_UPCOMING_REMINDER,true);
                    editorUpcomingReminder.commit();
                    upcomingReminderOn();
                }else {
                    editorUpcomingReminder.putBoolean(KEY_FIELD_UPCOMING_REMINDER,false);
                    editorUpcomingReminder.commit();
                    upcomingReminderOff();
                }
            }
        });

        swDailyReminder.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                editorDailyReminder = spDailyReminder.edit();
                if(swDailyReminder.isChecked()){
                    llDailyNotification.setVisibility(View.VISIBLE);
                    editorDailyReminder.putBoolean(KEY_FIELD_DAILY_REMINDER,true);
                    editorDailyReminder.commit();
                    dailyReminderOn();
                }else {
                    llDailyNotification.setVisibility(View.GONE);
                    editorDailyReminder.putBoolean(KEY_FIELD_DAILY_REMINDER,false);
                    editorDailyReminder.commit();
                    dailyReminderOff();
                }
            }
        });


    }

    private void upcomingReminderOn() {
        schedulerTask.createPeriodicTask();
        Toast.makeText(this,"Periodic Task Created",Toast.LENGTH_SHORT).show();
    }

    private void upcomingReminderOff() {
        schedulerTask.canccelPeriodicTask();
    }

    private void dailyReminderOn() {
        btnReminderDailyNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar currentDate = Calendar.getInstance();
                new TimePickerDialog(NotificationSettingActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        calReminder.set(Calendar.HOUR_OF_DAY,hourOfDay);
                        calReminder.set(Calendar.MINUTE,minute);
                        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm");
                        tvReminderDailyNotification.setText(dateFormat.format(calReminder.getTime()));
                    }
                }, currentDate.get(Calendar.HOUR_OF_DAY), currentDate.get(Calendar.MINUTE), true).show();

                SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
                String reminderTime = timeFormat.format(calReminder.getTime());
                String message = "MovieDb miss you, please come Back Soon";
                reminderMoviePreference.setReminderTime(reminderTime);
                reminderMoviePreference.setReminderMessage(message);
                reminderReceiver.setReminder(NotificationSettingActivity.this,ReminderReceiver.TYPE_REMINDER,reminderTime,message);

            }
        });
    }

    private void dailyReminderOff() {
        reminderReceiver.cancelReminder(NotificationSettingActivity.this);
    }


}
