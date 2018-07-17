package com.r4dixx.theguardianview;

public class Article {

    private String mTitle;
    private String mUrl;
    private String mTime;

    public Article(String title, String url, String time) {
        mTitle = title;
        mUrl = url;
        mTime = time;
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
}
