package com.sdk.onboard;

import android.content.Context;
import android.content.Intent;

import androidx.annotation.Nullable;

public class OnBoard {

    public interface IOnBoardCompleted {
        void onCompleted();
    }

    static IOnBoardCompleted mCallback;

    public static void showOnBoard(Context activity, @Nullable IOnBoardCompleted callback) {
        showOnBoard(activity, callback, false);
    }

    public static void showOnBoard(Context activity, @Nullable IOnBoardCompleted callback, boolean onlyFirstTime) {
        mCallback = callback;
        if (!ActivityOnBoard.isOnBoardShown(activity))
            activity.startActivity(new Intent(activity, ActivityOnBoard.class));
        else if (mCallback != null && !onlyFirstTime)
            mCallback.onCompleted();
    }
}