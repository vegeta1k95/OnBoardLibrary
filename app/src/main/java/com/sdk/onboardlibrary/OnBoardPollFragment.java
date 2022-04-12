package com.sdk.onboardlibrary;

import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

public class OnBoardPollFragment extends Fragment {

    public static OnBoardPollFragment newFragment(int layoutId) {
        OnBoardPollFragment fragment = new OnBoardPollFragment();

        Bundle bundle = new Bundle();
        bundle.putInt("layout", layoutId);

        fragment.setArguments(bundle);
        return fragment;
    }

    private Context mContext;

    public OnBoardPollFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mContext = inflater.getContext();
        int layoutId;

        Bundle bundle = getArguments();
        if (bundle == null)
            layoutId = savedInstanceState.getInt("layout");
        else
            layoutId = bundle.getInt("layout");

        return inflater.inflate(layoutId, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle bundle) {

        Resources res = mContext.getResources();

        RadioGroup rg = view.findViewById(res.getIdentifier("radio_group", "id", mContext.getPackageName()));
        rg.setOnCheckedChangeListener((group, checkedId) -> {
            View btn = ((PollActivity) getActivity()).getNextButton();
            btn.setClickable(true);
            btn.findViewById(res.getIdentifier("txt_next", "id", mContext.getPackageName())).setEnabled(true);
            btn.findViewById(res.getIdentifier("arrow_next", "id", mContext.getPackageName())).setEnabled(true);
        });
    }

}
