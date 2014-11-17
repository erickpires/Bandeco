package com.app.bandeco;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.IBinder;

import java.util.ArrayList;
import java.util.Calendar;

import erick.bandeco.database.DatabaseHelper;
import erick.bandeco.model.Day;
import erick.bandeco.model.Meal;
import erick.bandeco.view.MealNotification;
import erick.bandeco.view.Settings;

import static erick.bandeco.database.DatabaseContract.*;

public class NotificationService extends Service {

    public NotificationService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        SharedPreferences preferences = getSharedPreferences(getString(R.string.app_name), 0);
        int notifyWhenOption = preferences.getInt(Settings.NOTIFY_WHEN, Settings.NOTIFY_ALWAYS);
        int daysToNotifyCode = preferences.getInt(Settings.DAYS_TO_NOTIFY, Constants.DEFAULT_DAYS_TO_NOTIFY_CODE);
        int mealType = intent.getExtras().getInt(Settings.MEAL_TYPE);

        Calendar today = Calendar.getInstance();

        if(!Utils.shouldNotifyToday(daysToNotifyCode, today))
            return START_NOT_STICKY;

        DatabaseHelper databaseHelper = new DatabaseHelper(getBaseContext());
        SQLiteDatabase database = databaseHelper.getReadableDatabase();

        int dayOfTheWeek = Day.adaptDayOfWeek(today.get(Calendar.DAY_OF_WEEK));

        Meal meal = OperationsWithDB.getMealFromDatabase(database, dayOfTheWeek, mealType);

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
            ArrayList<String> likeList = OperationsWithDB.getListFromDB(db, PositiveWords.TABLE_NAME, new String[]{PositiveWords.WORDS});
            return hasMatch(meal, likeList);
        }

        else if(notifyWhenOption == Settings.NOTIFY_IF_NOT_DISLIKE) {
            ArrayList<String> dislikeList = OperationsWithDB.getListFromDB(db, NegativeWords.TABLE_NAME, new String[]{NegativeWords.WORDS});
            return !hasMatch(meal, dislikeList);
        }

        return false;
    }

    private static boolean hasMatch(Meal meal, ArrayList<String> list) {
        String tmp = meal.toString();

        for (String s : list)
            if (tmp.contains(s))
                return true;

        return false;
    }
}
