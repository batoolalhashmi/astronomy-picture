package com.barmej.apod.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.barmej.apod.R;
import com.barmej.apod.entity.AstronomyInfo;
import com.bumptech.glide.Glide;
import com.ortiz.touchview.TouchImageView;

import static com.android.volley.VolleyLog.TAG;

public class MainFragment extends Fragment {
    private TouchImageView mAstronomyImage;
    private WebView mAstronomyVideo;
    private TextView mTitle;
    private TextView mTitleInfo;
    private AstronomyInfo mAstronomyInfo;
    private ProgressBar mprogressBar;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main, container, false);
    }

    public void updateAstronomyInfo(AstronomyInfo astronomyInfo) {
        mAstronomyInfo = astronomyInfo;
        showAstronomyInfo();
    }

    private void showAstronomyInfo() {
        if (mAstronomyInfo == null) {
            return;
        }
        String title = mAstronomyInfo.getTitle();
        mTitle.setText(title);
        String titleInfo = mAstronomyInfo.getExplanation();
        mTitleInfo.setText(titleInfo);
        String url = mAstronomyInfo.getUrl();
        Log.i(TAG, "main fragment" + url);
        String hdurl = mAstronomyInfo.getHdurl();
        if (hdurl == null) {
            mAstronomyVideo.setWebViewClient(new WebViewClient());
            mAstronomyVideo.loadUrl(url);
            WebSettings webSettings = mAstronomyVideo.getSettings();
            webSettings.setJavaScriptEnabled(true);
            mprogressBar.setVisibility(View.GONE);
            mAstronomyVideo.setVisibility(View.VISIBLE);
            mAstronomyImage.setVisibility(View.GONE);
        } else {
            Glide.with(MainFragment.this)
                    .load(url)
                    .centerCrop()
                    .into(mAstronomyImage);
            mprogressBar.setVisibility(View.GONE);
            mAstronomyImage.setVisibility(View.VISIBLE);
            mAstronomyVideo.setVisibility(View.GONE);
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        View mainView = getView();
        if (mainView == null) return;
        mprogressBar = mainView.findViewById(R.id.progress_bar);
        mAstronomyImage = mainView.findViewById(R.id.img_picture_view);
        mAstronomyVideo = mainView.findViewById(R.id.wv_video_player);
        mTitle = mainView.findViewById(R.id.title_text);
        mTitleInfo = mainView.findViewById(R.id.info_text);
        showAstronomyInfo();
    }

    public void onProgressBarVisible() {
        mprogressBar.setVisibility(View.VISIBLE);
    }
}