package com.r4dixx.theguardianview;

import android.app.LoaderManager;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class ArticleActivity extends AppCompatActivity implements LoaderCallbacks<List<Article>> {

    // DO NOT HARDCODE YOUR PRIVATE API KEY HERE
    // Fill it in api_key.xml instead and keep this file private
    //
    // section=commentisfre
    // order-by=newest
    // show-fields=headline
    // show-fields=byline (i.e. author name)
    // page-size=50 (i.e. number of articles)
    // Content shows "The Guardian view on " AND " | Editorial"
    // for api-key cf. api-key.xml
    //
    //

    private static final String GUARDIAN_REQUEST_URL =
            "https://content.guardianapis.com/search?section=commentisfree&order-by=newest&show-fields=headline%2Cbyline&page-size=50&q=%22The%20Guardian%20view%20on%20%22%20AND%20%22%20%7C%20Editorial%22&api-key=";

    private TextView mEmptyStateTextView;

    private static final int ARTICLE_LOADER_ID = 0;

    private ArticleAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Sets the toolbar as the app bar for the activity and hides title to allow custom text
        android.support.v7.widget.Toolbar toolBar = findViewById(R.id.toolbar);
        setSupportActionBar(toolBar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        // Light action bar and navigation bar
        // a la Google News and cie
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
        decorView.setSystemUiVisibility(uiOptions);

        ListView listView = findViewById(R.id.article_list);

        // Hook up the TextView as the empty view of the ListView
        mEmptyStateTextView = findViewById(R.id.empty_view);
        listView.setEmptyView(mEmptyStateTextView);

        // This is where we ask the adapter to go fetch the ArrayList in QueryUtils
        // ↑ TBC NOT SURE ABOUT THIS ↑
        mAdapter = new ArticleAdapter(this, new ArrayList<Article>());
        listView.setAdapter(mAdapter);

        // TODO: onItemClickListener and OnItemClick on card button instead
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Article currentEarthquake = mAdapter.getItem(position);
                Uri earthquakeUri = Uri.parse(currentEarthquake.getUrl());
                Intent websiteIntent = new Intent(Intent.ACTION_VIEW, earthquakeUri);
                startActivity(websiteIntent);
            }
        });

        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()) {

            LoaderManager loaderManager = getLoaderManager();
            loaderManager.initLoader(ARTICLE_LOADER_ID, null, this);
        } else {
            View loadingIndicator = findViewById(R.id.loading_indicator);
            loadingIndicator.setVisibility(View.GONE);
            mEmptyStateTextView.setText(R.string.no_internet);
        }

    }

    @Override
    public Loader<List<Article>> onCreateLoader(int i, Bundle bundle) {
        return new ArticleLoader(this, GUARDIAN_REQUEST_URL.concat(getString(R.string.guardian_api_key)));
    }

    @Override
    public void onLoadFinished(Loader<List<Article>> loader, List<Article> earthquakes) {

        View loadingIndicator = findViewById(R.id.loading_indicator);
        loadingIndicator.setVisibility(View.GONE);
        mEmptyStateTextView.setText(R.string.no_articles);
        mAdapter.clear();

        if (earthquakes != null && !earthquakes.isEmpty()) {
            mAdapter.addAll(earthquakes);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<Article>> loader) {
        mAdapter.clear();
    }
}