package com.barmej.apod;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.app.DownloadManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.barmej.apod.entity.AstronomyInfo;
import com.barmej.apod.fragments.AboutFragment;
import com.barmej.apod.fragments.DatePickerFragment;
import com.barmej.apod.fragments.MainFragment;
import com.barmej.apod.network.NetworkUtils;
import com.barmej.apod.utils.NASADataParser;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity implements DatePickerFragment.OnDateChangeListener {
    private static final String TAG = "MainActivity";
    private FragmentManager mFragmentManager;
    final MainFragment mainFragment = new MainFragment();
    final AboutFragment aboutFragment = new AboutFragment();
    final DialogFragment datePickerFragment = new DatePickerFragment();
    private NetworkUtils mNetworkUtils;
    private AstronomyInfo mAstronomyInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mNetworkUtils = NetworkUtils.getInstance(this);
        onMainFragmentStart();
        requestAstronomyInfo(null);
    }

    private void requestAstronomyInfo(final String date) {
        String astronomyRequestUrl = NetworkUtils.getAstronomyViewUrl(MainActivity.this, date).toString();
        JsonObjectRequest astronomyRequest = new JsonObjectRequest(
                Request.Method.GET,
                astronomyRequestUrl,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            mAstronomyInfo = NASADataParser.getAstronomyInfoObjectFromJson(response);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        if (mAstronomyInfo != null) {
                            Toast.makeText(MainActivity.this, "Request Operation Completed!", Toast.LENGTH_SHORT).show();
                            mainFragment.updateAstronomyInfo(mAstronomyInfo);
                        } else {
                            Toast.makeText(MainActivity.this, "error happened", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
        );
        mNetworkUtils.addToRequestQueue(astronomyRequest);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if (mAstronomyInfo != null) {
            String hdurl = mAstronomyInfo.getHdurl();
            MenuItem downloadItem = menu.findItem(R.id.action_download_hd);
            Log.d(TAG, "onCreateOptionsMenu: " + hdurl);
            if (hdurl != null) {
                downloadItem.setVisible(true);
            } else {
                downloadItem.setVisible(false);
            }
        }
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        mFragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
        if (id == R.id.action_about) {
            fragmentTransaction.replace(R.id.main_layout, aboutFragment);
        } else if (id == R.id.action_download_hd) {
            startDownloadHd();
        } else if (id == R.id.action_share) {
            onSharedClicked();
        } else if (id == R.id.action_pick_day){
            datePickerFragment.show(getSupportFragmentManager(), "datePicker");
        }else {
            showLanguageDialog();
        }
        fragmentTransaction.commit();
        return super.onOptionsItemSelected(item);
    }

    private void startDownloadHd() {
        String hdurl = mAstronomyInfo.getHdurl();
        String title = mAstronomyInfo.getTitle();
        Log.d(TAG, "startDownloadHd: hdurl" + hdurl);
        Uri uri = Uri.parse(hdurl);
        DownloadManager downloadManager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
        DownloadManager.Request request = new DownloadManager.Request(uri);
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE);
        request.setAllowedOverRoaming(false);
        request.setTitle(title);
        request.setDestinationInExternalFilesDir(this, Environment.DIRECTORY_DOWNLOADS, title + ".jpg");
        downloadManager.enqueue(request);
    }

    private void onSharedClicked() {
        String url = mAstronomyInfo.getUrl();
        String title = mAstronomyInfo.getTitle();
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/*");
        shareIntent.putExtra(Intent.EXTRA_TEXT, title + " : " + url);
        startActivity(shareIntent);
    }

    @Override
    public void onDateChange(String date) {
        Log.d(TAG, "onDateSet: " + date);
        requestAstronomyInfo(date);
        onMainFragmentStart();
        mainFragment.onProgressBarVisible();
    }

    private void onMainFragmentStart() {
        mFragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.main_layout, mainFragment);
        fragmentTransaction.commit();
    }
    private void showLanguageDialog() {
        AlertDialog alertDialog = new AlertDialog.Builder(this)
                .setTitle(R.string.change_lang_text)
                .setItems(R.array.languages, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {
                        String language = "ar";
                        switch (which) {
                            case 0:
                                language = "ar";
                                break;
                            case 1:
                                language = "en";
                                break;
                        }
                        LocaleHelper.setLocale(MainActivity.this, language);
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }
                }).create();
        alertDialog.show();
    }
}