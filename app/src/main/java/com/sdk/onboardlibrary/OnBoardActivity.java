package com.sdk.onboardlibrary;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.color.MaterialColors;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;
import java.util.List;


public class OnBoardActivity extends AppCompatActivity {

    private static final String PREFERENCES = "onboard";
    private static final String KEY_SHOWN = "shown";

    private void setOnBoardShown() {
        SharedPreferences preferences = getSharedPreferences(PREFERENCES, MODE_PRIVATE);
        preferences.edit().putBoolean(KEY_SHOWN, true).apply();
    }

    public static boolean isOnBoardShown(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(PREFERENCES, MODE_PRIVATE);
        return preferences.getBoolean(KEY_SHOWN, false);
    }

    static class PageAdapter extends FragmentStateAdapter {

        private final List<String> onBoardLayouts = new ArrayList<>();

        PageAdapter(@NonNull FragmentActivity fragmentActivity) {
            super(fragmentActivity);
            int[] attrs = new int[] { R.attr.onboard_layouts };
            TypedArray a = fragmentActivity.obtainStyledAttributes(attrs);
            int id = a.getResourceId(0 , 0);
            a.recycle();
            if (id == 0)
                return;
            TypedArray layouts = fragmentActivity.getResources().obtainTypedArray(id);
            for (int i = 0; i < layouts.length(); i++) {
                onBoardLayouts.add(layouts.getString(i));
            }
            layouts.recycle();
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            return OnBoardFragment.getInstance(onBoardLayouts.get(position));
        }

        @Override
        public int getItemCount() {
            return onBoardLayouts.size();
        }
    }

    private PageAdapter mAdapter;
    private ViewPager2 mViewPager;

    @Override
    public void onBackPressed() {}

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_on_board);

        Window window = getWindow();
        window.setStatusBarColor(MaterialColors.getColor(this, R.attr.onboard_status_bar_color, R.attr.colorAccent));

        mAdapter = new PageAdapter(this);
        mViewPager = findViewById(R.id.pager);
        mViewPager.setAdapter(mAdapter);

        TabLayout tabs = findViewById(R.id.tabDots);
        tabs.setTabRippleColor(null);

        if (mAdapter.getItemCount() == 1)
            tabs.setVisibility(View.INVISIBLE);

        new TabLayoutMediator(tabs, mViewPager, (tab, position) -> {}).attach();

        Button btnContinue = findViewById(R.id.btn_continue);

        TypedValue typedValue = new TypedValue();
        Resources.Theme theme = getTheme();
        if (theme.resolveAttribute(R.attr.onboard_button_text_color, typedValue, true)) {
            @ColorInt int color = typedValue.data;
            btnContinue.setTextColor(color);
        }
        btnContinue.setOnClickListener(v -> {
            int current = mViewPager.getCurrentItem();
            if (current == mAdapter.getItemCount() - 1) {
                setOnBoardShown();
                //LocalConfig.setConsent(true);
                if (OnBoard.mCallback != null)
                    OnBoard.mCallback.onCompleted();
                finish();
            } else {
                mViewPager.setCurrentItem(current+1);
            }
        });
    }

    public static void setWindowFlag(Activity activity, final int bits, boolean on) {
        Window win = activity.getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }


}
