package com.biology.common.util;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;
import android.provider.Settings;
import android.text.TextUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Administrator on 2018/5/31.
 */

public class SysUtils {


    private static final String TAG = "SysUtils";
    public static long exitTime = 0;

    public static void exitApp(Activity activity) {
        if ((System.currentTimeMillis() - exitTime) > 2000) {
            ToastUtils.showToastShort(activity,"再按一次退出程序");
            exitTime = System.currentTimeMillis();
        } else {
            activity.finish();
        }
    }


    public static long getStringToDate(String dateString) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        try {
            date = dateFormat.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date.getTime();
    }

    /**
     * long类型时间格式化
     *
     * @param time 秒
     * @return
     */
    public static String getDateToString(long time) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date(time);
        return df.format(date);
    }

    /**
     * long类型时间格式化
     */
    public static String getDateToStringPoint(long time) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy.MM.dd");
        Date date = new Date(time * 1000L);
        return df.format(date);
    }

    public static String getTimeDiff(String time) {
        return getTimeDiff(Long.parseLong(time));
    }

    public static void main(String arg[]) {
        getTimeArr("1552546678");
    }


    public static String[] getTimeArr(String time) {
        String[] arr = {"", "", ""};

        try {
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            Date date = new Date(Long.parseLong(time) * 1000);
            String formatStr = df.format(date);
            String[] arrSplit = formatStr.split("-");
            if (arrSplit.length >= 3) {
                for (int i = 0; i < arr.length; i++) {
                    arr[i] = arrSplit[i];
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }


        return arr;
    }


    public static String getTimeDiff(long time) {
        return getTimeDiff(time, System.currentTimeMillis() / 1000);
    }

    public static String getTimeDiff(long time, long newTime) {
        if (time > newTime) {
            return "刚刚";
        }
        //计算天数
        long timeDiff = newTime - time;
        long days = timeDiff / 86400;
        if (days > 2) {
            return getDateToString(time * 1000);
        }
        //计算小时数
        long remain = (timeDiff) % 86400;
        long hours = remain / 3600;
        //计算分钟数
        remain = remain % 3600;
        long mins = remain / 60;
        //计算秒数
        long secs = remain % 60;
        StringBuffer stringBuffer = new StringBuffer();
        if (days > 0) {
            return days + "天前";
        }
        if (hours > 0) {
            return hours + "小时前";
        }
        if (mins > 0) {
            return mins + "分钟前";
        }
        return "刚刚";
    }


}
