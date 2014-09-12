package com.app.bandeco;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

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

        String tmp = mealType == 0 ? "Almoco" : "Jantar";

        MealNotification.notify(getApplicationContext(), tmp);

        stopSelf();
        return START_NOT_STICKY;
    }
}
