package com.r4dixx.theguardianview;

public class Article {

    private String mTitle;
    private String mUrl;

    public Article(String title, String url) {
        mTitle = title;
        mUrl = url;
    }

    public String getTitle() {
        return mTitle;
    }

    public String getUrl() {
        return mUrl;
    }
}
