package com.app.bandeco;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.IBinder;

import java.util.Calendar;

import erick.bandeco.database.DatabaseHelper;
import erick.bandeco.database.OperationsWithDB;
import erick.bandeco.model.Day;
import erick.bandeco.model.Meal;
import erick.bandeco.controller.MealNotification;
import erick.bandeco.controller.Settings;

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

		if (!Utils.shouldNotifyToday(daysToNotifyCode, today))
			return START_NOT_STICKY;

		DatabaseHelper databaseHelper = new DatabaseHelper(getBaseContext());
		SQLiteDatabase database = databaseHelper.getReadableDatabase();

		int dayOfTheWeek = Day.adaptDayOfWeek(today.get(Calendar.DAY_OF_WEEK));

		Meal meal = OperationsWithDB.getMealFromDatabase(database, dayOfTheWeek, mealType);

		String notificationMessage = Utils.getTextFromMeal(meal, getApplicationContext());

		if (shouldNotify(notifyWhenOption, meal, database, getApplicationContext())) {
			MealNotification.notify(getApplicationContext(), Utils.getMealType(meal, getApplicationContext()), notificationMessage);
		}

		database.close();
		stopSelf();
		return START_NOT_STICKY;
	}

	private static boolean shouldNotify(int notifyWhenOption, Meal meal, SQLiteDatabase db, Context context) {
		if (notifyWhenOption == Settings.NOTIFY_ALWAYS)
			return true;

		if (notifyWhenOption == Settings.NOTIFY_IF_LIKE)
			return Utils.hasLikedItem(meal, db, context);

		else if (notifyWhenOption == Settings.NOTIFY_IF_NOT_DISLIKE)
			return !Utils.hasDislikedItem(meal, db, context);

		return false;
	}
}
