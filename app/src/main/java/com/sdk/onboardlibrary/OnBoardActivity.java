package com.sdk.onboardlibrary;

import android.content.Intent;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.text.HtmlCompat;

public class OnBoardActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getResources().getIdentifier("activity_onboard_intro", "layout", getPackageName()));
        setupLink();
        int btnId = getResources().getIdentifier("btn_continue", "id", this.getPackageName());
        findViewById(btnId).setOnClickListener(v -> {
            startActivity(new Intent(this, PollActivity.class));
            finish();
        });
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

    @Override
    public void onBackPressed() {}
}