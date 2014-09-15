package com.app.bandeco;

import android.app.Service;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.IBinder;
import android.widget.Toast;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Date;

import database.DatabaseHelper;
import html.Html;
import model.Day;
import model.Week;

import static com.app.bandeco.ApplicationHelper.*;

public class UpdateService extends Service {
    public UpdateService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (checkConnection()) {

            Thread t = new Thread() {
                public void run() {
                    URL url = null;
                    try {
                        url = new URL(ApplicationHelper.url);

                        URLConnection connection = url.openConnection();

                        Date date = new Date(connection.getLastModified());

                        Html html = new Html(connection);

                        if (html != null)
                            updateDatabaseInfo(html, date);
                        else
                            System.out.println("Failed to get site");

                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    stopSelf();
                }
            };

            t.start();
        } else
            Toast.makeText(getBaseContext(), R.string.no_connection, Toast.LENGTH_SHORT).show();

        return START_NOT_STICKY;
    }

    private void updateDatabaseInfo(Html html, Date date) {
        DatabaseHelper databaseHelper = new DatabaseHelper(getBaseContext());
        SQLiteDatabase database = databaseHelper.getWritableDatabase();

        Week week = new Week(html.getTables());

        for (int i = 0; i < DAYS_IN_THE_WEEK; i++) {
            Day day = week.getDayAt(i);

            day.getLunch();

            insertMealInDatabase(database, day.getLunch(), i, MEAL_TYPE_LUNCH);
            insertMealInDatabase(database, day.getDinner(), i, MEAL_TYPE_DINNER);

            updateLastModifiedInDatabase(database, date);
        }

        database.close();
    }

    private boolean checkConnection() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        if (networkInfo == null ||
                !networkInfo.isConnected() ||
                !networkInfo.isAvailable())

            return false;

        return true;
    }
}
