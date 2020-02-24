package com.barmej.apod.entity;

import android.os.Parcel;
import android.os.Parcelable;

public class AstronomyInfo implements Parcelable {

    private String copyright;
    private String date;
    private String explanation;
    private String hdurl;
    private String mediaType;
    private String serviceVersion;
    private String title;
    private String url;

    public AstronomyInfo(){

    }

    protected AstronomyInfo(Parcel in) {
        copyright = in.readString();
        date = in.readString();
        explanation = in.readString();
        hdurl = in.readString();
        mediaType = in.readString();
        serviceVersion = in.readString();
        title = in.readString();
        url = in.readString();
    }

    public static final Creator<AstronomyInfo> CREATOR = new Creator<AstronomyInfo>() {
        @Override
        public AstronomyInfo createFromParcel(Parcel in) {
            return new AstronomyInfo(in);
        }

        @Override
        public AstronomyInfo[] newArray(int size) {
            return new AstronomyInfo[size];
        }
    };

    public String getCopyright() {
        return copyright;
    }

    public void setCopyright(String copyright) {
        this.copyright = copyright;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getExplanation() {
        return explanation;
    }

    public void setExplanation(String explanation) {
        this.explanation = explanation;
    }

    public String getHdurl() {
        return hdurl;
    }

    public void setHdurl(String hdurl) {
        this.hdurl = hdurl;
    }

    public String getMediaType() {
        return mediaType;
    }

    public void setMediaType(String mediaType) {
        this.mediaType = mediaType;
    }

    public String getServiceVersion() {
        return serviceVersion;
    }

    public void setServiceVersion(String serviceVersion) {
        this.serviceVersion = serviceVersion;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(copyright);
        parcel.writeString(date);
        parcel.writeString(explanation);
        parcel.writeString(hdurl);
        parcel.writeString(mediaType);
        parcel.writeString(serviceVersion);
        parcel.writeString(title);
        parcel.writeString(url);
    }
}
