package com.app.bandeco;

import android.app.Service;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.IBinder;

import java.util.Calendar;

import database.DatabaseHelper;
import model.Day;
import model.Meal;
import view.MealNotification;

public class NotificationService extends Service {
    public NotificationService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        int mealType = intent.getExtras().getInt(Settings.MEAL_TYPE);
        int dayOfTheWeek;

        Calendar today = Calendar.getInstance();
        DatabaseHelper databaseHelper = new DatabaseHelper(getBaseContext());
        SQLiteDatabase database = databaseHelper.getReadableDatabase();

        dayOfTheWeek = Day.adaptDayOfWeek(today.get(Calendar.DAY_OF_WEEK));

        Meal meal = ApplicationHelper.getMealFromDatabase(database, dayOfTheWeek, mealType);

        MealNotification.notify(getApplicationContext(), meal.getType(), meal.toString());

        database.close();
        stopSelf();
        return START_NOT_STICKY;
    }
}
