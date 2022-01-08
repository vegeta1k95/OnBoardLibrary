package com.sdk.onboardlibrary;

import android.app.Activity;
import android.content.Intent;

public class OnBoard {
    public static void showOnBoard(Activity activity) {
        if (!OnBoardActivity.isOnBoardShown(activity))
            activity.startActivity(new Intent(activity, OnBoardActivity.class));
    }

    public static void addLayout(String name) {
        OnBoardActivity.onBoardLayouts.add(name);
    }
}
