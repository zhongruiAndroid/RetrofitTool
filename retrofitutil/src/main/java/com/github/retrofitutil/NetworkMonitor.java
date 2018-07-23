package com.github.retrofitutil;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by Administrator on 2017/1/10.
 */
public class NetworkMonitor {
    public static boolean isConnected(Context context) {
        ConnectivityManager cm =(ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null &&activeNetwork.isConnectedOrConnecting();
    }
}
