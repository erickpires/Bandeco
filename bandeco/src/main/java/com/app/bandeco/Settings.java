package com.app.bandeco;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.sleepbot.datetimepicker.time.RadialPickerLayout;
import com.sleepbot.datetimepicker.time.TimePickerDialog;

import java.util.ArrayList;
import java.util.Calendar;

import database.DatabaseContract;
import database.DatabaseHelper;
import view.ListDialogFragment;

import static android.app.AlarmManager.*;
import static android.content.SharedPreferences.Editor;
import static android.widget.CompoundButton.*;
import static com.app.bandeco.ApplicationHelper.*;
import static com.sleepbot.datetimepicker.time.TimePickerDialog.*;
import static java.util.Calendar.*;


public class Settings extends ActionBarActivity {

    public static final int LUNCH_ONLY = 0;
    public static final int DINNER_ONLY = 1;
    public static final int BOTH_MEALS = 2;

    public static final int NOTIFY_ALWAYS = 0;
    public static final int NOTIFY_IF_LIKE = 1;
    public static final int NOTIFY_IF_DISLIKE = 2;

//    private static final int[] mealsOptions = new int[]{R.string.lunch_only,
//            R.string.dinner_only,
//            R.string.both_meals};

    private String[] mealsChoices;
    private String[] notifyWhenChoices;

    public static final String SHOW_MEALS = "ShowMeals";
    public static final String RECEIVE_NOTIFICATIONS = "ReceiveNotifications";
    public static final String LUNCH_NOTIFICATION_HOUR = "LunchNotificationTime";
    public static final String LUNCH_NOTIFICATION_MINUTE = "LunchNotificationMinute";
    public static final String DINNER_NOTIFICATION_HOUR = "DinnerNotificationTime";
    public static final String DINNER_NOTIFICATION_MINUTE = "DinnerNotificationMinute";
    public static final String NOTIFY_WHEN = "NotifyWhen";

    public static final String TIMEPICKER_TAG = "timepicker";
    public static final String MEAL_TYPE = "MealTime";

    private int mealOption;
    private boolean receiveNotifications;
    //TODO notifications time options
    private int lunchNotificationHour;
    private int lunchNotificationMinute;
    private int dinnerNotificationHour;
    private int dinnerNotificationMinute;
    private int notifyWhenOption;

    private SharedPreferences settings;
    private View showMeals;
    private TextView mealType;
    private View negativeWordsLayout;
    private TextView negativeWordsList;
    private View positiveWordsLayout;
    private TextView positiveWordsList;
    private View receiveNotificationsLayout;
    private CheckBox receiveNotificationsCheckBox;
    private View lunchNotificationLayout;
    private TextView lunchNotificationTimeTextView;
    private View dinnerNotificationLayout;
    private TextView dinnerNotificationTimeTextView;
    private TextView lunchNotificationTextView;
    private TextView dinnerNotificationTextView;
    private View notifyWhenLayout;
    private TextView notifyWhenTexView;
    private TextView notifyWhenOptionTextView;

    private ArrayList<String> negativeList;
    private ArrayList<String> positiveList;
    private SQLiteDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        ActionBar actionBar = getSupportActionBar();

        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        settings = getSharedPreferences(getString(R.string.app_name), 0);

        mealsChoices = new String[]{getString(R.string.lunch_only),
                getString(R.string.dinner_only),
                getString(R.string.both_meals)};

        notifyWhenChoices = new String[]{getString(R.string.always),
                getString(R.string.only_if_I_like),
                getString(R.string.only_if_I_dislike)
        };

        database = new DatabaseHelper(getBaseContext()).getWritableDatabase();

        getSettings();
        createUI();
    }

    @Override
    protected void onPause() {
        saveSettings();
        setupNotificationAlarm(MEAL_TYPE_LUNCH);
        setupNotificationAlarm(MEAL_TYPE_DINNER);
        super.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        database.close();
    }

    private void saveSettings() {
        Editor editor = settings.edit();

        editor.putInt(SHOW_MEALS, mealOption);
        editor.putBoolean(RECEIVE_NOTIFICATIONS, receiveNotifications);
        editor.putInt(LUNCH_NOTIFICATION_HOUR, lunchNotificationHour);
        editor.putInt(LUNCH_NOTIFICATION_MINUTE, lunchNotificationMinute);
        editor.putInt(DINNER_NOTIFICATION_HOUR, dinnerNotificationHour);
        editor.putInt(DINNER_NOTIFICATION_MINUTE, dinnerNotificationMinute);
        editor.putInt(NOTIFY_WHEN, notifyWhenOption);

        saveListToDB(database, positiveList, DatabaseContract.PositiveWords.TABLE_NAME);
        saveListToDB(database, negativeList, DatabaseContract.NegativeWords.TABLE_NAME);

        editor.commit();
    }

    private void getSettings() {
        mealOption = settings.getInt(SHOW_MEALS, BOTH_MEALS);
        receiveNotifications = settings.getBoolean(RECEIVE_NOTIFICATIONS, true);
        lunchNotificationHour = settings.getInt(LUNCH_NOTIFICATION_HOUR, 12);
        lunchNotificationMinute = settings.getInt(LUNCH_NOTIFICATION_MINUTE, 0);
        dinnerNotificationHour = settings.getInt(DINNER_NOTIFICATION_HOUR, 18);
        dinnerNotificationMinute = settings.getInt(DINNER_NOTIFICATION_MINUTE, 0);
        notifyWhenOption = settings.getInt(NOTIFY_WHEN, NOTIFY_ALWAYS);

        positiveList = getListFromDB(database, DatabaseContract.PositiveWords.TABLE_NAME, new String[]{DatabaseContract.PositiveWords.WORD});
        negativeList = getListFromDB(database, DatabaseContract.NegativeWords.TABLE_NAME, new String[]{DatabaseContract.NegativeWords.WORD});
    }

    private void createUI() {
        showMeals = findViewById(R.id.show_meals);
        mealType = (TextView) findViewById(R.id.meal_type);

        negativeWordsLayout = findViewById(R.id.negative_words);
        negativeWordsList = (TextView) findViewById(R.id.negative_words_list);

        positiveWordsLayout = findViewById(R.id.positive_words);
        positiveWordsList = (TextView) findViewById(R.id.positive_words_list);

        receiveNotificationsLayout = findViewById(R.id.receive_notifications);
        receiveNotificationsCheckBox = (CheckBox) findViewById(R.id.receive_notifications_checkbox);

        lunchNotificationLayout = findViewById(R.id.lunch_notification_time_layout);
        lunchNotificationTextView = (TextView) findViewById(R.id.lunch_notification_textview);
        lunchNotificationTimeTextView = (TextView) findViewById(R.id.lunch_notification_time_textview);

        dinnerNotificationLayout = findViewById(R.id.dinner_notification_time_layout);
        dinnerNotificationTextView = (TextView) findViewById(R.id.dinner_notification_textview);
        dinnerNotificationTimeTextView = (TextView) findViewById(R.id.dinner_notification_time_textview);

        notifyWhenLayout = findViewById(R.id.notify_when_layout);
        notifyWhenTexView = (TextView) findViewById(R.id.notify_when_textView);
        notifyWhenOptionTextView = (TextView) findViewById(R.id.notify_when_option_textView);

        //Meals to show
        updateMealType();

        showMeals.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(Settings.this);


                builder.setSingleChoiceItems(mealsChoices, mealOption, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mealOption = which;
                        updateMealType();
                        dialog.dismiss();
                    }
                });
                AlertDialog alert = builder.create();
                alert.show();
            }
        });


        //Negative words
        updateNegativeWords();

        negativeWordsLayout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                ListDialogFragment dialogFragment = new ListDialogFragment(negativeList);
                dialogFragment.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        updateNegativeWords();
                    }
                });

                dialogFragment.show(getSupportFragmentManager(), "");
            }
        });

        //Positive words
        updatePositiveWords();

        positiveWordsLayout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                ListDialogFragment dialogFragment = new ListDialogFragment(positiveList);
                dialogFragment.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        updatePositiveWords();
                    }
                });

                dialogFragment.show(getSupportFragmentManager(), "");

            }
        });


        //Receive notifications
        receiveNotificationsLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                receiveNotificationsCheckBox.toggle();
            }
        });

        receiveNotificationsCheckBox.setChecked(receiveNotifications);

        receiveNotificationsCheckBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                receiveNotifications = isChecked;

                updateLunchNotificationLayout();
                updateDinnerNotificationLayout();
                updateNotifyWhenLayout();
            }
        });

        //Lunch notification time
        updateLunchNotificationTime();

        lunchNotificationLayout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                OnTimeSetListener onTimeSetListener = new OnTimeSetListener() {
                    @Override
                    public void onTimeSet(RadialPickerLayout radialPickerLayout, int hour, int minute) {
                        lunchNotificationHour = hour;
                        lunchNotificationMinute = minute;
                        updateLunchNotificationTime();
                    }
                };

                TimePickerDialog timePicker = TimePickerDialog.newInstance(onTimeSetListener, lunchNotificationHour, lunchNotificationMinute, true);
                timePicker.show(getSupportFragmentManager(), TIMEPICKER_TAG);
            }
        });

        //Dinner notification time
        updateDinnerNotificationTime();

        dinnerNotificationLayout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                OnTimeSetListener onTimeSetListener = new OnTimeSetListener() {
                    @Override
                    public void onTimeSet(RadialPickerLayout radialPickerLayout, int hour, int minute) {
                        dinnerNotificationHour = hour;
                        dinnerNotificationMinute = minute;
                        updateDinnerNotificationTime();
                    }
                };

                TimePickerDialog timePicker = TimePickerDialog.newInstance(onTimeSetListener, dinnerNotificationHour, dinnerNotificationMinute, true);
                timePicker.show(getSupportFragmentManager(), TIMEPICKER_TAG);
            }
        });

        //Notify when
        notifyWhenOptionTextView.setText(notifyWhenChoices[notifyWhenOption]);
        updateNotifyWhenLayout();

        notifyWhenLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(Settings.this);

                builder.setSingleChoiceItems(notifyWhenChoices, notifyWhenOption, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        notifyWhenOption = which;
                        notifyWhenOptionTextView.setText(notifyWhenChoices[which]);
                        dialog.dismiss();
                    }
                });
                AlertDialog alert = builder.create();
                alert.show();
            }
        });
    }

    private void updateMealType() {
        mealType.setText(mealsChoices[mealOption]);

        updateLunchNotificationLayout();
        updateDinnerNotificationLayout();
    }

    private void updateLunchNotificationLayout() {
        boolean enabled = receiveNotifications && mealOption != DINNER_ONLY;

        lunchNotificationLayout.setEnabled(enabled);
        lunchNotificationTextView.setEnabled(enabled);
        lunchNotificationTimeTextView.setEnabled(enabled);
    }

    private void updateDinnerNotificationLayout() {
        boolean enabled = receiveNotifications && mealOption != LUNCH_ONLY;

        dinnerNotificationLayout.setEnabled(enabled);
        dinnerNotificationTextView.setEnabled(enabled);
        dinnerNotificationTimeTextView.setEnabled(enabled);
    }

    private void updateNotifyWhenLayout() {
        notifyWhenLayout.setEnabled(receiveNotifications);
        notifyWhenOptionTextView.setEnabled(receiveNotifications);
        notifyWhenTexView.setEnabled(receiveNotifications);
    }

    private void updateLunchNotificationTime() {
        String tmp = String.format("%02d:%02d", lunchNotificationHour, lunchNotificationMinute);
        lunchNotificationTimeTextView.setText(tmp);
    }

    private void updateDinnerNotificationTime() {
        String tmp = String.format("%02d:%02d", dinnerNotificationHour, dinnerNotificationMinute);
        dinnerNotificationTimeTextView.setText(tmp);
    }

    private void updateNegativeWords() {
        clearList(negativeList);
        negativeWordsList.setText(getStringFromList(negativeList));
    }

    private void updatePositiveWords() {
        clearList(positiveList);
        positiveWordsList.setText(getStringFromList(positiveList));
    }

    private String getStringFromList(ArrayList<String> list) {
        return list.toString().replaceAll("(\\[|\\])", "");
    }

    private void clearList(ArrayList<String> list) {

        while (list.contains(""))
            list.remove("");

        if (list.isEmpty())
            list.add("");
    }

    private void setupNotificationAlarm(int mealType) {
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

        Intent intentNotificationService = new Intent(getApplicationContext(), NotificationService.class);
        intentNotificationService.putExtra(MEAL_TYPE, mealType);

        PendingIntent pendingIntent = PendingIntent.getService(getApplicationContext(), mealType, intentNotificationService, 0);

        alarmManager.cancel(pendingIntent);

        if (mealType == MEAL_TYPE_LUNCH && mealOption == DINNER_ONLY)
            return;

        if (mealType == MEAL_TYPE_DINNER && mealOption == LUNCH_ONLY)
            return;

        Calendar calendar = getCalendar(mealType);

        alarmManager.setInexactRepeating(RTC, calendar.getTimeInMillis(), INTERVAL_DAY, pendingIntent);

        System.out.println("Notifying: " + mealType);
    }

    private Calendar getCalendar(int mealType) {
        Calendar calendar = getInstance();
        switch (mealType) {
            case MEAL_TYPE_LUNCH:
                calendar.set(HOUR_OF_DAY, lunchNotificationHour);
                calendar.set(MINUTE, lunchNotificationMinute);
                break;
            case MEAL_TYPE_DINNER:
                calendar.set(HOUR_OF_DAY, dinnerNotificationHour);
                calendar.set(MINUTE, dinnerNotificationMinute);
                break;
        }
        calendar.set(SECOND, 0);

        if (calendar.before(getInstance()))
            calendar.add(DATE, 1);
        return calendar;
    }
}
