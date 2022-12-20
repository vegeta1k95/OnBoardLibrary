package com.sdk.onboardlibrary;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.animation.AccelerateInterpolator;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class LoadingActivity extends AppCompatActivity {

    private ProgressBar pbLoading;
    private TextView tvProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Resources res = getResources();

        setContentView(res.getIdentifier("activity_onboard_loading", "layout", getPackageName()));

        pbLoading = findViewById(res.getIdentifier("progress_loading", "id", getPackageName()));
        tvProgress = findViewById(res.getIdentifier("txt_progress", "id", getPackageName()));

        animate();
    }

    private void animate() {
        ValueAnimator valueAnimator = ValueAnimator.ofInt(0, 100);
        valueAnimator.setDuration(4000);
        valueAnimator.setInterpolator(new AccelerateInterpolator());
        valueAnimator.addUpdateListener(va -> {
            pbLoading.setProgress((int) va.getAnimatedValue());
            tvProgress.setText(va.getAnimatedValue().toString() + "%");
        });
        valueAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {

                Handler handler = new Handler(Looper.getMainLooper());
                handler.postDelayed(() -> {
                    startActivity(new Intent(LoadingActivity.this, OfferActivity.class));
                    finish();
                }, 300);
            }
        });
        valueAnimator.start();
    }

    @Override
    public void onBackPressed() {}
}