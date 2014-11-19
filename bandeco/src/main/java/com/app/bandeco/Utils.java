package com.app.bandeco;

import android.annotation.TargetApi;
import android.os.Build;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.view.WindowManager;

import com.readystatesoftware.systembartint.SystemBarTintManager;

import java.util.Calendar;

import static com.app.bandeco.Constants.DAYS_TO_NOTIFY_CODES;

public class Utils {

    @TargetApi(Build.VERSION_CODES.KITKAT)
    public static void changeStatusColor(ActionBarActivity activity, View parentLayout){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT &&
           Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP){  // Let Lollipop handle this
            parentLayout.setFitsSystemWindows(true);
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

    public static final  <T> void arraySwap(T[] array, int pos1, int pos2){
        T tmp = array[pos1];
        array[pos1] = array[pos2];
        array[pos2] = tmp;
    }

    public static final Integer[] extractMenuCodesFromInt(int coded){
        Integer[] result = new Integer[7];

        for(int i = 0; i < result.length; i++){
            result[i] = coded & 0x07;
            coded = coded >> 3;
        }

        return result;
    }

    public static final int codifyMenuCodesFromArray(Integer[] array){
        int result = 0;

        for (int i = 0; i < array.length; i++)
            result |= array[i] << (3 * i);

        return result;
    }

    public static final String[] sortMenuEntries(String[] stringArray, Integer[] menuEntriesOrder) {
        String[] result = new String[stringArray.length];

        for(Integer i : menuEntriesOrder)
            result[i] = stringArray[menuEntriesOrder[i]];

        return result;
    }

    public static final Boolean[] extractEnabledMenuEntriesFromInt(int coded, Integer[] menuEntriesOrder) {
        Boolean[] result = new Boolean[menuEntriesOrder.length];

        for(int i = 0; i < menuEntriesOrder.length; i++ ) {
            int pos = menuEntriesOrder[i];
            result[i] = (coded & (1 << pos)) != 0;
        }

        return result;
    }

    public static final int codifyEnabledMenuEntriesFromArray(Boolean[] enabledMenuEntries, Integer[] menuEntriesOrder) {
        int code = 0;

        for(int i = 0; i < menuEntriesOrder.length; i++){
            int pos = menuEntriesOrder[i];
            if(enabledMenuEntries[i])
                code |= 1 << pos;
        }

        return code;
    }
}
