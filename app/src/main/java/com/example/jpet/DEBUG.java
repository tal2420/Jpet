package com.example.jpet;

import android.util.Log;

/**
 * Created by yakov on 5/25/2017.
 */

public class DEBUG {

    private static final boolean debug = false;

    public static void MSG(Class c, String MSG) {
        if (debug) {
            Log.e(c != null ? c.toString() : "", MSG != null ? MSG : "");
        }
    }
}
