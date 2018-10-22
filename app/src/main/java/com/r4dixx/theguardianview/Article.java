package com.r4dixx.theguardianview;


public class Article {

    private String mTitle;
    private String mUrl;
    private String mTime;
    private String mThumbnailUrl;
    private String mButtonText;

    public Article(String title, String url, String time, String thumbnailUrl, String buttonText) {
        mTitle = title;
        mUrl = url;
        mTime = time;
        mThumbnailUrl = thumbnailUrl;
        mButtonText = buttonText;
    }

    public String getTitle() {
        return mTitle;
    }

    public String getUrl() {
        return mUrl;
    }

    public String getTime() {
        return mTime;
    }

    public String getThumbnailUrl() {
        return mThumbnailUrl;
    }

    public String getButtonText() {
        return mButtonText;
    }
}
