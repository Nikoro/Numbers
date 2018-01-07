package com.dominikkrajcer.numbers;

import android.content.Context;
import android.net.ConnectivityManager;

/**
 * Created by Dominik on 07.01.2018.
 */

public class InternetUtils {

    public static boolean isOnline(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        return connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnectedOrConnecting();
    }
}
