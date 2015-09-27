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
import java.net.URL;
import java.net.URLConnection;
import java.util.Calendar;

import erick.bandeco.database.DatabaseHelper;
import erick.bandeco.database.OperationsWithDB;
import erick.bandeco.html.Html;
import erick.bandeco.model.Day;
import erick.bandeco.model.Week;

import static com.app.bandeco.Constants.DAYS_IN_THE_WEEK;
import static com.app.bandeco.Constants.MEAL_TYPE_DINNER;
import static com.app.bandeco.Constants.MEAL_TYPE_LUNCH;
import static com.app.bandeco.Constants.SITE_URL;
import static erick.bandeco.database.OperationsWithDB.insertMealDayDateInDatabase;
import static erick.bandeco.database.OperationsWithDB.insertMealInDatabase;

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

					Html html = new Html(connection);

					updateDatabaseInfo(html);

					LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(new Intent("update_event"));
				} catch (IOException e) {
					//TODO(Maybe): treat MalformedURLException differently
					e.printStackTrace();
				} finally {
					stopSelf();
				}
			}
		};

		t.start();


		return START_NOT_STICKY;
	}

	private void updateDatabaseInfo(Html html) {
		DatabaseHelper databaseHelper = new DatabaseHelper(getBaseContext());
		try {
			SQLiteDatabase database = databaseHelper.getWritableDatabase();

			Week week = new Week(html.getTables(), html.getLastModified());

			for (int i = 0; i < DAYS_IN_THE_WEEK; i++) {
				Day day = week.getDayAt(i);

				insertMealInDatabase(database, day.getLunch(), i, MEAL_TYPE_LUNCH);
				insertMealInDatabase(database, day.getDinner(), i, MEAL_TYPE_DINNER);

				Calendar dayDate = day.getDate();
				if(dayDate != null)
					insertMealDayDateInDatabase(database, i, dayDate.getTimeInMillis());
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
