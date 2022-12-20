package com.sdk.onboardlibrary;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class OfferActivity extends AppCompatActivity {

    public static final String KEY_NO_BACK = "key_no_back";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Resources res = getResources();
        setContentView(res.getIdentifier("activity_onboard_offer", "layout", getPackageName()));

        ImageView btnBack = findViewById(res.getIdentifier("btn_back", "id", getPackageName()));

        Intent intent = getIntent();
        boolean noBack = intent.getBooleanExtra(KEY_NO_BACK, false);

        if (noBack) {
            btnBack.setVisibility(View.GONE);
        } else {
            btnBack.setOnClickListener(v -> {
                Intent intent1 = new Intent(this, PollActivity.class);
                intent1.putExtra(PollActivity.KEY_FRAGMENT, 2);
                startActivity(intent1);
                finish();
            });
        }

        int btnId = getResources().getIdentifier("btn_continue", "id", this.getPackageName());
        findViewById(btnId).setOnClickListener(v -> {
            OnBoard.mCallback.onFinish();
            finish();
        });
    }
}