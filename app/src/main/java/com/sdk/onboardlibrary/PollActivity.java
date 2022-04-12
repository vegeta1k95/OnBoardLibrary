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
import com.sdk.linkinglibrary.Linking;

import java.util.ArrayList;
import java.util.List;

public class PollActivity extends AppCompatActivity {

    public static final String KEY_FRAGMENT = "fragment";

    private final List<Integer> mPolls = new ArrayList<>();
    private final List<Bundle> mLinks = new ArrayList<>();

    private class TabsAdapter extends FragmentStateAdapter {

        TabsAdapter(@NonNull FragmentActivity fragmentActivity) {
            super(fragmentActivity);
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            if (position < mPolls.size()) {
                int layoutId = mPolls.get(position);
                return OnBoardPollFragment.newFragment(layoutId);
            } else {
                Bundle args = mLinks.get(position-mPolls.size());
                return OnBoardLinkFragment.newFragment(args);
            }
        }

        @Override
        public int getItemCount() {
            return mPolls.size() + mLinks.size();
        }

        @SuppressLint("NotifyDataSetChanged")
        public void addPoll(int layoutId) {
            mPolls.add(layoutId);
            notifyDataSetChanged();
        }

        @SuppressLint("NotifyDataSetChanged")
        public void addLinking(Bundle args) {
            mLinks.add(args);
            notifyDataSetChanged();
        }
    }

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

        setupTabs();

        btnBack.setOnClickListener(v -> {
            int current = mViewPager.getCurrentItem();
            if (current > 0)
                mViewPager.setCurrentItem(current-1);
        });

        btnSkip.setOnClickListener(v -> {
            Intent intent = new Intent(this, OfferActivity.class);
            intent.putExtra(OfferActivity.KEY_SKIP, true);
            startActivity(intent);
            finish();
        });

        btnNext.setOnClickListener(v -> {
            int current = mViewPager.getCurrentItem();
            if (current < mPolls.size() + mLinks.size() - 1) {
                if (current < mPolls.size() - 1) {
                    btnNext.setClickable(false);
                    tvNext.setEnabled(false);
                    ivNext.setEnabled(false);
                }
                mViewPager.setCurrentItem(current + 1);
            } else if (current == mPolls.size() + mLinks.size() - 1) {
                startActivity(new Intent(this, LoadingActivity.class));
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

        mAdapter.addPoll(mRes.getIdentifier("fragment_onboard_poll1", "layout", getPackageName()));
        mAdapter.addPoll(mRes.getIdentifier("fragment_onboard_poll2", "layout", getPackageName()));
        mAdapter.addPoll(mRes.getIdentifier("fragment_onboard_poll3", "layout", getPackageName()));

        Linking.inflateOnBoardItem(this, 2,
                item -> mAdapter.addLinking(item.toBundle()));
    }
}