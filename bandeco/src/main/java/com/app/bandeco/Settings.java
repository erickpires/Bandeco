package com.app.bandeco;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import java.util.ArrayList;

import view.ListDialogFragment;

import static android.content.SharedPreferences.Editor;
import static android.widget.CompoundButton.*;


public class Settings extends ActionBarActivity {

    public static final int LUNCH_ONLY = 0;
    public static final int DINNER_ONLY = 1;
    public static final int BOTH_MEALS = 2;

    private static final int[] mealsOptions = new int[]{R.string.lunch_only,
            R.string.dinner_only,
            R.string.both_meals};


    private String[] mealsChoices;

    public static final String SHOW_MEALS = "ShowMeals";
    public static final String NEGATIVE_WORDS = "NegativeWords";
    public static final String POSITIVE_WORDS = "PositiveWords";
    public static final String RECEIVE_NOTIFICATIONS = "ReceiveNotifications";
    public static final String LUNCH_NOTIFICATION_TIME = "LunchNotificationTime";
    public static final String DINNER_NOTIFICATION_TIME = "DinnerNotificationTime";

    private int mealOption;
    private String negativeWords;
    private String positiveWords;
    //TODO use a database to get rid of this workaround
    private boolean receiveNotifications;
    //TODO notifications time options

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
    private Context context;
    private ArrayList<String> negativeList;
    private ArrayList<String> positiveList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        ActionBar actionBar = getSupportActionBar();

        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        settings = getSharedPreferences(getString(R.string.app_name), 0);

        mealsChoices = new String[]{getString(mealsOptions[0]),
                getString(mealsOptions[1]),
                getString(mealsOptions[2])};

        context = getApplicationContext();

        negativeList = new ArrayList<String>();
        negativeList.add("Peixe");
        negativeList.add("Cação");

        positiveList = new ArrayList<String>();
        positiveList.add("");

        getSettings();
        createUI();
    }

    @Override
    protected void onPause() {
        saveSettings();
        super.onPause();
    }

    private void saveSettings() {
        Editor editor = settings.edit();

        editor.putInt(SHOW_MEALS, mealOption);
        editor.putString(NEGATIVE_WORDS, negativeWords);
        editor.putString(POSITIVE_WORDS, positiveWords);
        editor.putBoolean(RECEIVE_NOTIFICATIONS, receiveNotifications);


        editor.commit();
    }

    private void getSettings() {
        mealOption = settings.getInt(SHOW_MEALS, BOTH_MEALS);
        negativeWords = settings.getString(NEGATIVE_WORDS, null);
        positiveWords = settings.getString(POSITIVE_WORDS, null);
        receiveNotifications = settings.getBoolean(RECEIVE_NOTIFICATIONS, false);

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
                dinnerLunchNotificationLayout();
            }
        });

        //Lunch notification time

        lunchNotificationTimeTextView.setText("12:00");

        //Dinner notification time

        dinnerNotificationTimeTextView.setText("17:00");
    }

    private void updateMealType() {
        mealType.setText(mealsChoices[mealOption]);

        updateLunchNotificationLayout();
        dinnerLunchNotificationLayout();
    }

    private void updateLunchNotificationLayout() {
        if (receiveNotifications && mealOption != DINNER_ONLY) {
            lunchNotificationLayout.setEnabled(true);
            lunchNotificationTextView.setEnabled(true);
            lunchNotificationTimeTextView.setEnabled(true);
        } else {
            lunchNotificationLayout.setEnabled(false);
            lunchNotificationTextView.setEnabled(false);
            lunchNotificationTimeTextView.setEnabled(false);
        }
    }

    private void dinnerLunchNotificationLayout() {
        if (receiveNotifications && mealOption != LUNCH_ONLY) {
            dinnerNotificationLayout.setEnabled(true);
            dinnerNotificationTextView.setEnabled(true);
            dinnerNotificationTimeTextView.setEnabled(true);
        } else {
            dinnerNotificationLayout.setEnabled(false);
            dinnerNotificationTextView.setEnabled(false);
            dinnerNotificationTimeTextView.setEnabled(false);
        }
    }

    private void updateNegativeWords() {
        negativeWordsList.setText("" + negativeList);
    }

    private void updatePositiveWords(){
        positiveWordsList.setText("" + positiveList);
    }
}
