package erick.bandeco.view;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Vibrator;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.support.v7.widget.Toolbar;

import com.app.bandeco.Constants;
import com.app.bandeco.NotificationService;
import com.app.bandeco.R;
import com.app.bandeco.Utils;
import com.nhaarman.listviewanimations.itemmanipulation.DynamicListView;
import com.nhaarman.listviewanimations.itemmanipulation.dragdrop.TouchViewDraggableManager;
import com.sleepbot.datetimepicker.time.RadialPickerLayout;
import com.sleepbot.datetimepicker.time.TimePickerDialog;

import java.util.ArrayList;
import java.util.Calendar;

import erick.bandeco.adapters.MenuEntriesListAdapter;
import erick.bandeco.database.DatabaseContract;
import erick.bandeco.database.DatabaseHelper;

import static android.app.AlarmManager.*;
import static android.content.SharedPreferences.Editor;
import static android.widget.CompoundButton.*;
import static com.app.bandeco.OperationsWithDB.*;
import static com.app.bandeco.Constants.*;
import static com.sleepbot.datetimepicker.time.TimePickerDialog.*;
import static java.util.Calendar.*;


public class Settings extends ActionBarActivity {

    public static final int LUNCH_ONLY = 0;
    public static final int DINNER_ONLY = 1;
    public static final int BOTH_MEALS = 2;

    public static final int NOTIFY_ALWAYS = 0;
    public static final int NOTIFY_IF_LIKE = 1;
    public static final int NOTIFY_IF_NOT_DISLIKE = 2;
    public static final int NEVER_NOTIFY = 3;

    private String[] mealsChoices;
    private String[] notifyWhenChoices;
    private String[] daysOfTheWeek;

    public static final String SHOW_MEALS = "ShowMeals";
    public static final String MENU_ENTRIES_ORDER = "MenuEntriesOrder";
    public static final String ENABLED_MENU_ENTRIES = "EnabledMenuEntries";
    public static final String LUNCH_NOTIFICATION_HOUR = "LunchNotificationTime";
    public static final String LUNCH_NOTIFICATION_MINUTE = "LunchNotificationMinute";
    public static final String DINNER_NOTIFICATION_HOUR = "DinnerNotificationTime";
    public static final String DINNER_NOTIFICATION_MINUTE = "DinnerNotificationMinute";
    public static final String NOTIFY_WHEN = "NotifyWhen";
    public static final String DAYS_TO_NOTIFY = "DaysToNotify";

    public static final String TIMEPICKER_TAG = "timepicker";
    public static final String MEAL_TYPE = "MealTime";

    private int mealOption;
    private int lunchNotificationHour;
    private int lunchNotificationMinute;
    private int dinnerNotificationHour;
    private int dinnerNotificationMinute;
    private int notifyWhenOption;
    private int daysToNotifyCode;
    private Integer[] menuEntriesOrder;
    private Boolean[] enabledMenuEntries;

    private boolean shouldUpdateDB = false;

    private SharedPreferences settings;
    private TextView mealType;
    private TextView negativeWordsList;
    private TextView positiveWordsList;
    private View lunchNotificationLayout;
    private TextView lunchNotificationTimeTextView;
    private View dinnerNotificationLayout;
    private TextView dinnerNotificationTimeTextView;
    private TextView lunchNotificationTextView;
    private TextView dinnerNotificationTextView;
    private TextView notifyWhenOptionTextView;

    private ArrayList<String> negativeList;
    private ArrayList<String> positiveList;
    private SQLiteDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        Toolbar toolbar = (Toolbar) findViewById(R.id.settings_toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();

        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        View parentLayout = findViewById(R.id.parent_layout_settings);
        Utils.changeStatusColor(this, parentLayout);

        settings = getSharedPreferences(getString(R.string.app_name), 0);

        mealsChoices = getResources().getStringArray(R.array.show_options);
        notifyWhenChoices = getResources().getStringArray(R.array.notify_when_options);
        daysOfTheWeek = getResources().getStringArray(R.array.days_array);

        database = new DatabaseHelper(getBaseContext()).getWritableDatabase();

        getSettings();
        createUI();
    }

    @Override
    protected void onPause() {
        super.onPause();
        System.out.println("Saving Settings");
        saveSettings();
        setupNotificationAlarm(MEAL_TYPE_LUNCH);
        setupNotificationAlarm(MEAL_TYPE_DINNER);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        database.close();
    }

    private void saveSettings() {
        Editor editor = settings.edit();

        editor.putInt(SHOW_MEALS, mealOption);
        editor.putInt(MENU_ENTRIES_ORDER, Utils.codifyMenuCodesFromArray(menuEntriesOrder));
        editor.putInt(ENABLED_MENU_ENTRIES, Utils.codifyEnabledMenuEntriesFromArray(enabledMenuEntries, menuEntriesOrder));
        editor.putInt(LUNCH_NOTIFICATION_HOUR, lunchNotificationHour);
        editor.putInt(LUNCH_NOTIFICATION_MINUTE, lunchNotificationMinute);
        editor.putInt(DINNER_NOTIFICATION_HOUR, dinnerNotificationHour);
        editor.putInt(DINNER_NOTIFICATION_MINUTE, dinnerNotificationMinute);
        editor.putInt(NOTIFY_WHEN, notifyWhenOption);
        editor.putInt(DAYS_TO_NOTIFY, daysToNotifyCode);
        editor.apply();

        if (shouldUpdateDB) {
            saveListToDB(database, positiveList, DatabaseContract.PositiveWords.TABLE_NAME, DatabaseContract.PositiveWords.WORDS);
            saveListToDB(database, negativeList, DatabaseContract.NegativeWords.TABLE_NAME, DatabaseContract.NegativeWords.WORDS);
        }
    }

    private void getSettings() {
        mealOption = settings.getInt(SHOW_MEALS, BOTH_MEALS);
        lunchNotificationHour = settings.getInt(LUNCH_NOTIFICATION_HOUR, 12);
        lunchNotificationMinute = settings.getInt(LUNCH_NOTIFICATION_MINUTE, 0);
        dinnerNotificationHour = settings.getInt(DINNER_NOTIFICATION_HOUR, 18);
        dinnerNotificationMinute = settings.getInt(DINNER_NOTIFICATION_MINUTE, 0);
        notifyWhenOption = settings.getInt(NOTIFY_WHEN, NOTIFY_ALWAYS);
        daysToNotifyCode = settings.getInt(DAYS_TO_NOTIFY, DEFAULT_DAYS_TO_NOTIFY_CODE);

        int menuEntriesOrderCoded = settings.getInt(MENU_ENTRIES_ORDER, Constants.DEFAULT_MENU_ENTRIES_CODE);
        menuEntriesOrder = Utils.extractMenuCodesFromInt(menuEntriesOrderCoded);

        int enabledMenuEntriesCoded = settings.getInt(ENABLED_MENU_ENTRIES, Constants.ALL_MENU_ENTRIES_CODE);
        enabledMenuEntries = Utils.extractEnabledMenuEntriesFromInt(enabledMenuEntriesCoded, menuEntriesOrder);

        positiveList = getListFromDB(database, DatabaseContract.PositiveWords.TABLE_NAME, new String[]{DatabaseContract.PositiveWords.WORDS});
        negativeList = getListFromDB(database, DatabaseContract.NegativeWords.TABLE_NAME, new String[]{DatabaseContract.NegativeWords.WORDS});
    }

    private void createUI() {
        View showMeals = findViewById(R.id.show_meals);
        mealType = (TextView) findViewById(R.id.meal_type);

        View manageMenuEntries = findViewById(R.id.manage_menu_entries_layout);

        View negativeWordsLayout = findViewById(R.id.negative_words);
        negativeWordsList = (TextView) findViewById(R.id.negative_words_list);

        View positiveWordsLayout = findViewById(R.id.positive_words);
        positiveWordsList = (TextView) findViewById(R.id.positive_words_list);

        lunchNotificationLayout = findViewById(R.id.lunch_notification_time_layout);
        lunchNotificationTextView = (TextView) findViewById(R.id.lunch_notification_textview);
        lunchNotificationTimeTextView = (TextView) findViewById(R.id.lunch_notification_time_textview);

        dinnerNotificationLayout = findViewById(R.id.dinner_notification_time_layout);
        dinnerNotificationTextView = (TextView) findViewById(R.id.dinner_notification_textview);
        dinnerNotificationTimeTextView = (TextView) findViewById(R.id.dinner_notification_time_textview);

        View notifyWhenLayout = findViewById(R.id.notify_when_layout);
        notifyWhenOptionTextView = (TextView) findViewById(R.id.notify_when_option_textView);

        final View daysToNotifyLayout = findViewById(R.id.days_to_notify_layout);

        //Meals to show
        mealType.setText(mealsChoices[mealOption]);

        showMeals.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(Settings.this);

                builder.setSingleChoiceItems(mealsChoices, mealOption, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mealOption = which;
                        mealType.setText(mealsChoices[mealOption]);

                        updateLunchNotificationLayout();
                        updateDinnerNotificationLayout();

                        dialog.dismiss();
                    }
                });
                AlertDialog alert = builder.create();
                alert.show();
            }
        });

        //Manage menu entries
        manageMenuEntries.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(Settings.this);

                LayoutInflater inflater = getLayoutInflater();
                View view = inflater.inflate(R.layout.menu_entries_list, null);
                DynamicListView dynamicListView = (DynamicListView) view.findViewById(R.id.menu_entries_dynamic_listview);

                MenuEntriesListAdapter adapter = new MenuEntriesListAdapter(Settings.this, menuEntriesOrder, enabledMenuEntries);
                dynamicListView.setAdapter(adapter);

                dynamicListView.enableDragAndDrop();
                dynamicListView.setDraggableManager(new mTouchViewDraggableManager(R.id.drag_and_drop));

                builder.setView(view);

                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });


        //Negative words
        updateNegativeWords();

        negativeWordsLayout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                shouldUpdateDB = true;

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
                shouldUpdateDB = true;

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

        //Notify when
        notifyWhenOptionTextView.setText(notifyWhenChoices[notifyWhenOption]);

        notifyWhenLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(Settings.this);

                builder.setSingleChoiceItems(notifyWhenChoices, notifyWhenOption, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        notifyWhenOption = which;
                        notifyWhenOptionTextView.setText(notifyWhenChoices[which]);

                        updateLunchNotificationLayout();
                        updateDinnerNotificationLayout();

                        dialog.dismiss();
                    }
                });
                AlertDialog alert = builder.create();
                alert.show();
            }
        });

        //Days to notify
        daysToNotifyLayout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(Settings.this);
                boolean[] checked = Utils.getBooleansFromDaysCode(daysToNotifyCode);

                builder.setMultiChoiceItems(daysOfTheWeek, checked, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                        daysToNotifyCode ^= DAYS_TO_NOTIFY_CODES[which];
                    }
                });

                AlertDialog alert = builder.create();
                alert.show();
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
    }

    private void updateLunchNotificationLayout() {
        boolean enabled = notifyWhenOption != NEVER_NOTIFY && mealOption != DINNER_ONLY;

        lunchNotificationLayout.setEnabled(enabled);
        lunchNotificationTextView.setEnabled(enabled);
        lunchNotificationTimeTextView.setEnabled(enabled);
    }

    private void updateDinnerNotificationLayout() {
        boolean enabled = notifyWhenOption != NEVER_NOTIFY && mealOption != LUNCH_ONLY;

        dinnerNotificationLayout.setEnabled(enabled);
        dinnerNotificationTextView.setEnabled(enabled);
        dinnerNotificationTimeTextView.setEnabled(enabled);
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

        if (notifyWhenOption == NEVER_NOTIFY)
            return;

        if (mealType == MEAL_TYPE_LUNCH && mealOption == DINNER_ONLY)
            return;

        if (mealType == MEAL_TYPE_DINNER && mealOption == LUNCH_ONLY)
            return;

        Calendar calendar = getCalendar(mealType);

        alarmManager.setInexactRepeating(RTC, calendar.getTimeInMillis(), INTERVAL_DAY, pendingIntent);
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

    private class mTouchViewDraggableManager extends TouchViewDraggableManager {
        final long[] magicVibrationPattern = { 0, 7, 14, 7 };
        public mTouchViewDraggableManager(int drag_and_drop) {
            super(drag_and_drop);
        }
         @Override
         public boolean isDraggable(@NonNull View view, int position, float x, float y) {
            boolean result = super.isDraggable(view, position, x, y);

             if(result){
                 Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
                 vibrator.vibrate(magicVibrationPattern , -1);
             }

             return result;
        }
    }
}
