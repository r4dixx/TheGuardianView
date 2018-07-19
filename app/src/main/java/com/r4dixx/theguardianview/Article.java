package com.r4dixx.theguardianview;

public class Article {

    private String mTitle;
    private String mUrl;
    private String mTime;
    private String mSectionName;

    public Article(String title, String url, String time, String sectionName) {
        mTitle = title;
        mUrl = url;
        mTime = time;
        mSectionName = sectionName;
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

    public String getSectionName() {
        return mSectionName;
    }
}
