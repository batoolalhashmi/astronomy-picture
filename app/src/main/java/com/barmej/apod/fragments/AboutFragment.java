package com.barmej.apod.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.barmej.apod.R;

public class AboutFragment extends Fragment {
    private TextView mAboutTitleTextView;
    private TextView mAboutTextView;
    private ImageView mNasaImage;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_about, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        View mainView = getView();

        mAboutTitleTextView = mainView.findViewById(R.id.txt_about_title);
        mAboutTextView = mainView.findViewById(R.id.txt_about);
        mNasaImage = mainView.findViewById(R.id.nasa_image);

        showAboutInfo();
    }

    private void showAboutInfo() {
        mAboutTitleTextView.setText(R.string.about_label);
        mAboutTextView.setText(R.string.about_content);
        mNasaImage.setImageResource(R.drawable.ic_nasa_logo);
    }
}

