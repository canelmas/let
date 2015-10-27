package com.canelmas.let;

import android.util.Log;

/**
 * Created by can on 31/08/15.
 */
final class Logger {

    private static final String TAG = "Let";

    private Logger() {}

    public static void log(String text) {
        Log.d(TAG, text);
    }

    public static void log(String text, Throwable t) {
        Log.e(TAG, text, t);
    }

}
