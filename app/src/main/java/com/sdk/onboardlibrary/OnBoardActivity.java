package com.sdk.onboardlibrary;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.text.HtmlCompat;

import com.sdk.billinglibrary.Billing;

public class OnBoardActivity extends AppCompatActivity {

    public static final String KEY_FIRST_TIME = "first_time";
    public static final String KEY_SKIP_OFFER = "skip_loading_offer";

    public static void start(Activity activity, boolean skipLoadingAndOffer) {
        if (activity != null) {
            Intent intent = new Intent(activity, OnBoardActivity.class);
            intent.putExtra(KEY_SKIP_OFFER, skipLoadingAndOffer);
            activity.startActivity(intent);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getResources().getIdentifier("activity_onboard_intro", "layout", getPackageName()));

        if (isFirstTime()) {
            setupLink();
            findViewById(R.id.btn_continue).setOnClickListener(v -> {
                Intent intent = new Intent(this, PollActivity.class);
                intent.putExtra(KEY_SKIP_OFFER, getIntent().getBooleanExtra(KEY_SKIP_OFFER, false));
                if (getIntent().getBooleanExtra(KEY_SKIP_OFFER, false))
                    notifyOnBoardDone(this);
                startActivity(intent);
                finish();
            });
        } else {
            Billing.startBillingActivity(this, true);
            finish();
        }
    }

    private void setupLink() {
        TextView tos = findViewById(getResources().getIdentifier("txt_tos", "id", getPackageName()));
        tos.setMovementMethod(LinkMovementMethod.getInstance());

        TypedArray array = obtainStyledAttributes(new int[] { R.attr.onboard_privacy_policy });
        int resID = array.getResourceId(0, 0);
        array.recycle();

        if (resID == 0)
            return;

        String url = getString(resID);
        String link = "<a href=\"" + url + "\">" + getString(R.string.onboard_tos) + "</a>";
        tos.setText(HtmlCompat.fromHtml(link, HtmlCompat.FROM_HTML_MODE_LEGACY));
    }

    private boolean isFirstTime() {
        return !getSharedPreferences("onboard", MODE_PRIVATE).contains(KEY_FIRST_TIME);
    }

    public static void notifyOnBoardDone(Context context) {
        context.getSharedPreferences("onboard", MODE_PRIVATE).edit().putBoolean(KEY_FIRST_TIME, false).apply();
    }

    @Override
    public void onBackPressed() {}
}