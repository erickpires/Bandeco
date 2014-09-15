package com.app.bandeco;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.IBinder;

import java.util.ArrayList;
import java.util.Calendar;

import database.DatabaseContract;
import database.DatabaseHelper;
import model.Day;
import model.Meal;
import view.MealNotification;

import static database.DatabaseContract.*;

public class NotificationService extends Service {

    private int notifyWhenOption;

    public NotificationService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        SharedPreferences preferences = getSharedPreferences(getString(R.string.app_name), 0);
        notifyWhenOption = preferences.getInt(Settings.NOTIFY_WHEN, Settings.NOTIFY_ALWAYS);

        int mealType = intent.getExtras().getInt(Settings.MEAL_TYPE);

        DatabaseHelper databaseHelper = new DatabaseHelper(getBaseContext());
        SQLiteDatabase database = databaseHelper.getReadableDatabase();

        Calendar today = Calendar.getInstance();
        int dayOfTheWeek = Day.adaptDayOfWeek(today.get(Calendar.DAY_OF_WEEK));

        Meal meal = ApplicationHelper.getMealFromDatabase(database, dayOfTheWeek, mealType);

        if(shouldNotify(notifyWhenOption, meal, database)) {
            MealNotification.notify(getApplicationContext(), meal.getType(), meal.toString());
        }

        database.close();
        stopSelf();
        return START_NOT_STICKY;
    }

    private static boolean shouldNotify(int notifyWhenOption, Meal meal, SQLiteDatabase db) {
        if(notifyWhenOption == Settings.NOTIFY_ALWAYS)
            return true;

        if(notifyWhenOption == Settings.NOTIFY_IF_LIKE) {
            ArrayList<String> likeList = ApplicationHelper.getListFromDB(db, PositiveWords.TABLE_NAME, new String[]{PositiveWords.WORD});
            return hasMatch(meal, likeList);
        }

        else {
            ArrayList<String> dislikeList = ApplicationHelper.getListFromDB(db, NegativeWords.TABLE_NAME, new String[]{NegativeWords.WORD});
            return hasMatch(meal, dislikeList);
        }
    }

    private static boolean hasMatch(Meal meal, ArrayList<String> list) {
        String tmp = meal.toString();

        for (String s : list)
            if (tmp.contains(s))
                return true;

        return false;
    }
}
