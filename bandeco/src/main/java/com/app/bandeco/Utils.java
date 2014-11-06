package com.app.bandeco;

import android.os.Build;
import android.support.v7.app.ActionBarActivity;
import android.view.WindowManager;

import com.readystatesoftware.systembartint.SystemBarTintManager;

import java.util.Calendar;

import static com.app.bandeco.Constants.DAYS_TO_NOTIFY_CODES;

public class Utils {

    public static void changeStatusColor(ActionBarActivity activity){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            SystemBarTintManager tintManager = new SystemBarTintManager(activity);
            tintManager.setStatusBarTintEnabled(true);
            tintManager.setStatusBarTintColor(activity.getResources().getColor(R.color.status_color));
        }
    }

    public static boolean[] getBooleansFromDaysCode(int daysCode){
       boolean[] result = new boolean[7];

        for(int i = 0; i < DAYS_TO_NOTIFY_CODES.length; i++)
            result[i] = (daysCode & DAYS_TO_NOTIFY_CODES[i]) != 0;

        return result;
    }

    public static boolean shouldNotifyToday(int daysToNotifyCode, Calendar today) {
        int todayNumber = today.get(Calendar.DAY_OF_WEEK) - 1;

        return (daysToNotifyCode & DAYS_TO_NOTIFY_CODES[todayNumber]) != 0;
    }
}
