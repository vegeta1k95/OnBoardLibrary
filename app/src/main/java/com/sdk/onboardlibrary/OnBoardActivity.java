package com.sdk.onboardlibrary;

import android.content.Intent;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.text.HtmlCompat;

import com.sdk.billinglibrary.Billing;

public class OnBoardActivity extends AppCompatActivity {

    private static final String KEY_FIRST_TIME = "first_time";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getResources().getIdentifier("activity_onboard_intro", "layout", getPackageName()));

        if (isFirstTime()) {
            setupLink();
            findViewById(R.id.btn_continue).setOnClickListener(v -> {
                startActivity(new Intent(this, PollActivity.class));
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
        boolean isFirstTime = !getPreferences(MODE_PRIVATE).contains(KEY_FIRST_TIME);
        getPreferences(MODE_PRIVATE).edit().putBoolean(KEY_FIRST_TIME, false).apply();
        return isFirstTime;
    }
}