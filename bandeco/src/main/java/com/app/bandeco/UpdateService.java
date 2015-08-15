package com.app.bandeco;

import android.app.Service;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;
import android.widget.Toast;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Date;

import erick.bandeco.database.DatabaseHelper;
import erick.bandeco.database.OperationsWithDB;
import erick.bandeco.html.Html;
import erick.bandeco.model.Day;
import erick.bandeco.model.Week;

import static com.app.bandeco.Constants.DAYS_IN_THE_WEEK;
import static com.app.bandeco.Constants.MEAL_TYPE_DINNER;
import static com.app.bandeco.Constants.MEAL_TYPE_LUNCH;
import static com.app.bandeco.Constants.SITE_URL;
import static erick.bandeco.database.OperationsWithDB.insertMealInDatabase;
import static erick.bandeco.database.OperationsWithDB.updateLastModifiedInDatabase;

public class UpdateService extends Service {

	@Override
	public IBinder onBind(Intent intent) {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		if (!checkConnection()) {
			Toast.makeText(getBaseContext(), R.string.no_connection, Toast.LENGTH_SHORT).show();
			stopSelf();

			return START_NOT_STICKY;
		}

		Thread t = new Thread() {
			public void run() {
				try {
					URL url = new URL(SITE_URL);

					URLConnection connection = url.openConnection();

					Date date = new Date(connection.getLastModified());

					Html html = new Html(connection);

					updateDatabaseInfo(html, date);

					LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(new Intent("update_event"));
				} catch (MalformedURLException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				} finally {
					stopSelf();
				}
			}
		};

		t.start();


		return START_NOT_STICKY;
	}

	private void updateDatabaseInfo(Html html, Date date) {
		DatabaseHelper databaseHelper = new DatabaseHelper(getBaseContext());
		try {
			SQLiteDatabase database = databaseHelper.getWritableDatabase();

			Week week = new Week(html.getTables());

			for (int i = 0; i < DAYS_IN_THE_WEEK; i++) {
				Day day = week.getDayAt(i);

				day.getLunch();

				insertMealInDatabase(database, day.getLunch(), i, MEAL_TYPE_LUNCH);
				insertMealInDatabase(database, day.getDinner(), i, MEAL_TYPE_DINNER);

				updateLastModifiedInDatabase(database, date);
			}

			Main.week = OperationsWithDB.getWeekFromDatabase(database);

			database.close();
		}catch (Exception e){
			e.printStackTrace();
			//TODO: Log
		}
	}

	private boolean checkConnection() {
		ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

		return (networkInfo != null &&
						networkInfo.isConnected() &&
						networkInfo.isAvailable());

	}
}
