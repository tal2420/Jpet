package com.example.jpet;

import android.util.Log;

/**
 * Created by yakov on 5/25/2017.
 */

public class DEBUG {

    private static final boolean debug = true;

    public static void MSG(Class c, String MSG) {
        if (debug) {
            Log.e(c != null ? c.toString() : "", MSG != null ? MSG : "");
        }
    }

    public static void trace(int maxLines) {
        StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();
        int length = stackTraceElements.length > maxLines ? maxLines : stackTraceElements.length;
        for (int i = 0; i < length; i++) {
            MSG(DEBUG.class, stackTraceElements[i].toString());
        }

    }
}
