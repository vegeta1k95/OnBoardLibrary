package com.sdk.onboardlibrary;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.sdk.billinglibrary.Billing;

import java.util.ArrayList;
import java.util.List;

public class PollActivity extends AppCompatActivity {

    public static final String KEY_FRAGMENT = "fragment";

    private final List<Integer> mPolls = new ArrayList<>();

    private class TabsAdapter extends FragmentStateAdapter {

        TabsAdapter(@NonNull FragmentActivity fragmentActivity) {
            super(fragmentActivity);
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            int layoutId = mPolls.get(position);
            return OnBoardPollFragment.newFragment(layoutId);
        }

        @Override
        public int getItemCount() {
            return mPolls.size();
        }

        @SuppressLint("NotifyDataSetChanged")
        public void addPoll(int layoutId) {
            mPolls.add(layoutId);
            notifyDataSetChanged();
        }
    }

    private boolean mSkipLoadingAndOffer = false;

    private Resources mRes;

    private TabsAdapter mAdapter;
    private ViewPager2 mViewPager;
    private TabLayout mTabLayout;

    private View btnNext;
    private View btnBack;
    private View btnSkip;
    private TextView tvNext;
    private TextView ivNext;

    public View getNextButton() {
        return btnNext;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mRes = getResources();
        setContentView(mRes.getIdentifier("activity_onboard_poll", "layout", getPackageName()));

        mTabLayout = findViewById(mRes.getIdentifier("tabs", "id", getPackageName()));
        mViewPager = findViewById(mRes.getIdentifier("viewpager", "id", getPackageName()));

        btnNext = findViewById(mRes.getIdentifier("btn_next", "id", getPackageName()));
        btnBack = findViewById(mRes.getIdentifier("btn_back", "id", getPackageName()));
        btnSkip = findViewById(mRes.getIdentifier("btn_skip", "id", getPackageName()));
        tvNext = findViewById(mRes.getIdentifier("txt_next", "id", getPackageName()));
        ivNext = findViewById(mRes.getIdentifier("arrow_next", "id", getPackageName()));

        mSkipLoadingAndOffer = getIntent().getBooleanExtra(OnBoardActivity.KEY_SKIP_OFFER, false);

        setupTabs();

        btnBack.setOnClickListener(v -> {
            int current = mViewPager.getCurrentItem();
            if (current > 0)
                mViewPager.setCurrentItem(current-1);
        });

        btnSkip.setOnClickListener(v -> {
            if (mSkipLoadingAndOffer) {
                OnBoardActivity.notifyOnBoardDone(this);
                Billing.startBillingActivity(this, false);
            } else {
                Intent intent = new Intent(this, OfferActivity.class);
                intent.putExtra(OfferActivity.KEY_SKIP, true);
                startActivity(intent);
            }
            finish();
        });

        btnNext.setOnClickListener(v -> {
            int current = mViewPager.getCurrentItem();
            if (current < mPolls.size() - 1) {
                if (current < mPolls.size() - 1) {
                    btnNext.setClickable(false);
                    tvNext.setEnabled(false);
                    ivNext.setEnabled(false);
                }
                mViewPager.setCurrentItem(current + 1);
            } else if (current == mPolls.size() - 1) {
                if (mSkipLoadingAndOffer) {
                    Billing.startBillingActivity(this, false);
                } else {
                    if (getResources().getIdentifier("activity_onboard_loading", "layout", getPackageName()) != 0)
                        startActivity(new Intent(this, LoadingActivity.class));
                    else if (getResources().getIdentifier("activity_onboard_offer", "layout", getPackageName()) != 0)
                        startActivity(new Intent(this, OfferActivity.class));
                    else
                        Billing.startBillingActivity(this, false);
                }
                OnBoardActivity.notifyOnBoardDone(this);
                finish();
            }
        });
        btnNext.setClickable(false);

        Intent intent = getIntent();
        int fragment = intent.getIntExtra(KEY_FRAGMENT, 0);
        mViewPager.setCurrentItem(fragment);
        mTabLayout.setClickable(false);
    }

    private void setupTabs() {
        mAdapter = new TabsAdapter(this);
        mViewPager.setUserInputEnabled(false);
        mViewPager.setAdapter(mAdapter);
        mTabLayout.setTabRippleColor(null);
        new TabLayoutMediator(mTabLayout, mViewPager, (tab, position)
                -> tab.view.setClickable(false)).attach();

        for (int i = 1; i <= 5; i++) {
            int id = mRes.getIdentifier("fragment_onboard_poll" + i, "layout", getPackageName());
            if (id != 0)
                mAdapter.addPoll(id);
        }

        if (mAdapter.getItemCount() == 0) {
            OnBoardActivity.notifyOnBoardDone(this);
            Billing.startBillingActivity(this, false);
            finish();
        }
    }

    @Override
    public void onBackPressed() {}
}