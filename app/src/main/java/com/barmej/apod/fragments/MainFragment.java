package com.barmej.apod.fragments;

import android.graphics.drawable.Drawable;
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
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.ortiz.touchview.TouchImageView;

import static com.android.volley.VolleyLog.TAG;

public class MainFragment extends Fragment {
    private static final String SAVED_OBJECT = "obj";
    private TouchImageView mAstronomyImage;
    private WebView mAstronomyVideo;
    private TextView mTitle;
    private TextView mTitleInfo;
    private AstronomyInfo mAstronomyInfo;
    private ProgressBar mprogressBar;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            mAstronomyInfo = savedInstanceState.getParcelable(SAVED_OBJECT);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
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
        mTitle.setText(title + "");
        String titleInfo = mAstronomyInfo.getExplanation();
        mTitleInfo.setText(titleInfo);
        String url = mAstronomyInfo.getUrl();
        Log.i(TAG, "main fragment" + url);
        String mediaType = mAstronomyInfo.getMediaType();
        if (mediaType.equals("video")) {
            mAstronomyVideo.setWebViewClient(new WebViewClient());
            mAstronomyVideo.loadUrl(url);
            WebSettings webSettings = mAstronomyVideo.getSettings();
            webSettings.setJavaScriptEnabled(true);
            mprogressBar.setVisibility(View.GONE);
            mAstronomyVideo.setVisibility(View.VISIBLE);
            mAstronomyImage.setVisibility(View.GONE);
        } else {
            mprogressBar.setVisibility(View.GONE);
            mAstronomyVideo.setVisibility(View.GONE);
            mAstronomyImage.setVisibility(View.VISIBLE);
            Glide.with(mAstronomyImage)
                    .load(url)
                    .fitCenter()
                    .listener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                            System.out.println("LOAD FAILED");
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                            System.out.println("LOAD COMPLETE");
                            return false;
                        }
                    })
                    .into(mAstronomyImage);

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

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putParcelable(SAVED_OBJECT, mAstronomyInfo);
        super.onSaveInstanceState(outState);
    }

}