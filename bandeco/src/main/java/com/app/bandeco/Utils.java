package com.app.bandeco;

import android.app.Activity;
import android.os.Build;
import android.support.v7.app.ActionBarActivity;
import android.view.WindowManager;

import com.readystatesoftware.systembartint.SystemBarTintManager;

/**
 * Created by erick on 31/10/14.
 */
public class Utils {

    public static void changeStatusColor(ActionBarActivity activity){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            SystemBarTintManager tintManager = new SystemBarTintManager(activity);
            tintManager.setStatusBarTintEnabled(true);
            tintManager.setStatusBarTintColor(activity.getResources().getColor(R.color.status_color));
        }
    }
}
