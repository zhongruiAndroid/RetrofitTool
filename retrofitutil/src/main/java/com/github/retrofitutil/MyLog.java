package com.github.retrofitutil;

import android.util.Log;

/**
 * Created by Administrator on 2017/1/9.
 */
public class MyLog {
    private static int length=2000;
    public static void i(String logString) {
        i("#MyLog#", logString);
    }
    public static void i(String key, String logString) {
        if (logString.length() > length) {
//            i.i("LogUtils", "logString.length = " + logString.length());
            int chunkCount = logString.length() / length;
            for (int i = 0; i <= chunkCount; i++) {
                int max = length * (i + 1);
                if (max >= logString.length()) {
                    Log.i(key, logString.substring(length * i));
                } else {
                    Log.i(key, logString.substring(length * i, max));
                }
            }
        } else {
            Log.i(key,logString.toString());
        }
    }
}
