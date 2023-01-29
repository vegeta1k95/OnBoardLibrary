package com.sdk.onboardlibrary;

import android.app.Activity;
import android.content.Intent;

import androidx.annotation.Nullable;

public class OnBoard {

    public interface IOnBoardCompleted {
        void onCompleted();
    }

    static IOnBoardCompleted mCallback;

    public static void showOnBoard(Activity activity, @Nullable IOnBoardCompleted callback) {
        showOnBoard(activity, callback, false);
    }

    public static void showOnBoard(Activity activity, @Nullable IOnBoardCompleted callback, boolean onlyFirstTime) {
        mCallback = callback;
        if (!OnBoardActivity.isOnBoardShown(activity))
            activity.startActivity(new Intent(activity, OnBoardActivity.class));
        else if (mCallback != null && !onlyFirstTime)
            mCallback.onCompleted();
    }
}