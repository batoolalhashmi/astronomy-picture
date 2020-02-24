package com.barmej.apod.utils;

import android.util.Log;

import com.barmej.apod.entity.AstronomyInfo;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.HttpURLConnection;

import static com.android.volley.VolleyLog.TAG;

public class NASADataParser {
    private static final String DATE = "date";
    private static final String EXPLAN = "explanation";
    private static final String HDURL = "hdurl";
    private static final String TITLE = "title";
    private static final String URL = "url";
    private static final String NASA_MESSAGE_CODE = "code";
    private static final String MEDIA_TYPE = "media_type";

    private static boolean isError(JSONObject jsonObject) {
        if (jsonObject == null) {
            return true;
        }
        try {
            if (jsonObject.has(NASA_MESSAGE_CODE)) {
                int errorCode = jsonObject.getInt(NASA_MESSAGE_CODE);
                switch (errorCode) {
                    case HttpURLConnection.HTTP_NOT_FOUND:
                        Log.e(TAG, " Invalid");
                    default:
                        Log.e(TAG, "Server probably down");
                }
            } else {
                return false;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return true;
    }

    public static AstronomyInfo getAstronomyInfoObjectFromJson(JSONObject astronomyJson) throws JSONException {

        if (isError(astronomyJson)) {
            return null;
        }
        AstronomyInfo astronomyInfo = new AstronomyInfo();
        astronomyInfo.setTitle(astronomyJson.getString(TITLE));
        astronomyInfo.setUrl(astronomyJson.getString(URL));
        astronomyInfo.setDate(astronomyJson.getString(DATE));
        astronomyInfo.setExplanation(astronomyJson.getString(EXPLAN));
        astronomyInfo.setMediaType(astronomyJson.getString(MEDIA_TYPE));

        if (astronomyJson.has(HDURL)) {
            astronomyInfo.setHdurl(astronomyJson.getString(HDURL));
        } else {
            astronomyInfo.setHdurl(null);
        }
        return astronomyInfo;
    }
}
