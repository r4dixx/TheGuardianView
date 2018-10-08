package com.r4dixx.theguardianview;


public class Article {

    private String mTitle;
    private String mUrl;
    private String mTime;
    private String mThumbnailUrl;

    public Article(String title, String url, String time, String thumbnailUrl) {
        mTitle = title;
        mUrl = url;
        mTime = time;
        mThumbnailUrl = thumbnailUrl;
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
}
