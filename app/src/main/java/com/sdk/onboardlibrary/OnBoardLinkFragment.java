package com.sdk.onboardlibrary;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.sdk.linkinglibrary.ImageDownloader;

import java.util.ArrayList;
import java.util.List;


public class OnBoardLinkFragment extends Fragment {

    private LayoutInflater mInflater;
    private Context mContext;
    private Resources mResources;

    public static OnBoardLinkFragment newFragment(Bundle args) {
        OnBoardLinkFragment fragment = new OnBoardLinkFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public OnBoardLinkFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mInflater = inflater;
        mContext = mInflater.getContext();
        mResources = mContext.getResources();
        return inflater.inflate(mResources.getIdentifier("fragment_onboard_link",
                "layout", mContext.getPackageName()), container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle bundle) {

        Bundle args = getArguments();

        if (args == null)
            return;

        String pkg = args.getString("id");
        String title = args.getString("title");
        String btnText = args.getString("button");
        String imgUrl = args.getString("image");
        ArrayList<String> featuresIconUrls = args.getStringArrayList("features_icon");
        ArrayList<String> featuresText = args.getStringArrayList("features_text");

        if (pkg  == null
                || title == null
                || btnText == null
                || imgUrl == null
                || featuresIconUrls == null
                || featuresText == null)
            return;

        TextView tvTitle = view.findViewById(mResources.getIdentifier("txt_title", "id", mContext.getPackageName()));
        TextView tvBtnText = view.findViewById(mResources.getIdentifier("txt_btn", "id", mContext.getPackageName()));

        tvTitle.setText(title);
        tvBtnText.setText(btnText);

        View btnDownload = view.findViewById(mResources.getIdentifier("btn_link", "id", mContext.getPackageName()));
        btnDownload.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse("https://play.google.com/store/apps/details?id=" + pkg));
            intent.setPackage("com.android.vending");
            if (intent.resolveActivity(mContext.getPackageManager()) != null) {
                startActivity(intent);
            }
        });

        View btn = ((PollActivity) getActivity()).getNextButton();
        btn.setClickable(true);
        btn.findViewById(mResources.getIdentifier("txt_next", "id", mContext.getPackageName())).setEnabled(true);
        btn.findViewById(mResources.getIdentifier("arrow_next", "id", mContext.getPackageName())).setEnabled(true);

        List<ImageView> images = new ArrayList<>();

        ViewGroup featuresContainer = view.findViewById(R.id.features_container);

        int layoutID = mResources.getIdentifier("linking_onboard_feature", "layout", mContext.getPackageName());

        for (String featureText : featuresText) {
            View row = mInflater.inflate(layoutID, featuresContainer, false);
            TextView tvName = row.findViewById(mResources.getIdentifier("txt_feature_name", "id", mContext.getPackageName()));
            ImageView ivIcon = row.findViewById(mResources.getIdentifier("img_feature_icon", "id", mContext.getPackageName()));

            tvName.setText(featureText);
            images.add(ivIcon);
            featuresContainer.addView(row);
        }
        images.add(view.findViewById(mResources.getIdentifier("img_big", "id", mContext.getPackageName())));

        featuresIconUrls.add(imgUrl);

        ImageDownloader.loadDrawablesFromCache(getContext(), images, featuresIconUrls);
    }
}
