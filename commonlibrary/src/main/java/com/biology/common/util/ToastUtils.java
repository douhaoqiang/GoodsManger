package com.biology.common.util;

import android.content.Context;
import android.widget.Toast;


/**
 * Created by tangzy on 2016/8/10.
 */
public class ToastUtils {

    private static Toast toast = null;

    public static void showToastLong(Context context, String msg) {
        showToast(context, msg, Toast.LENGTH_LONG);
    }

    public static void showToastShort(Context context, String msg) {
        showToast(context, msg, Toast.LENGTH_SHORT);
    }
//    private static void showToast(String msg, int time){
//        Toast.makeText(Constant.app, msg, time).show();
//    }

    private static void showToast(Context context, String msg, int time) {

        if (context==null){
            return;
        }

        if (toast == null) {
            toast = Toast.makeText(context, msg, time);
        } else {
            toast.setText(msg);
            toast.setDuration(time);
        }
        toast.show();
    }
}
