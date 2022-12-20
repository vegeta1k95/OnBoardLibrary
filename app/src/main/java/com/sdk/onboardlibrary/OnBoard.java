package com.sdk.onboardlibrary;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

public class OnBoard {

    interface ICallback {
        void onFinish();
    }

    static ICallback mCallback;

    public static void start(Activity activity, ICallback callback) {
        mCallback = callback;
        if (activity != null && isFirstTime(activity))
            activity.startActivity(new Intent(activity, OnBoardActivity.class));
    }

    private static final String KEY_FIRST_TIME = "first_time";

    static boolean isFirstTime(Context context) {
        return !context.getSharedPreferences("onboard", Context.MODE_PRIVATE)
                .contains(KEY_FIRST_TIME);
    }

    static void setOnBoardDone(Context context) {
        context.getSharedPreferences("onboard", Context.MODE_PRIVATE)
                .edit()
                .putLong(KEY_FIRST_TIME, System.currentTimeMillis())
                .apply();
    }

}
