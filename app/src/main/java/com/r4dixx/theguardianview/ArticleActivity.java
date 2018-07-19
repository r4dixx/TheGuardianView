package com.r4dixx.theguardianview;

import android.app.LoaderManager;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class ArticleActivity extends AppCompatActivity implements LoaderCallbacks<List<Article>> {

    private TextView mEmptyStateTextView;

    private static final int ARTICLE_LOADER_ID = 0;

    private ArticleAdapter mAdapter;

    final LoaderManager loaderManager = getLoaderManager();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SwipeRefreshLayout swipeLayout = findViewById(R.id.swiperefresh);

        // Sets the toolbar as the app bar for the activity and hides title to allow custom text
        android.support.v7.widget.Toolbar toolBar = findViewById(R.id.toolbar);
        setSupportActionBar(toolBar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        // Light action bar and navigation bar
        // a la Google News and cie
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
        decorView.setSystemUiVisibility(uiOptions);

        ListView listView = findViewById(R.id.list);

        // Hook up the TextView as the empty view of the ListView
        mEmptyStateTextView = findViewById(R.id.empty_view);
        listView.setEmptyView(mEmptyStateTextView);

        // This is where we ask the adapter to go fetch the ArrayList in QueryUtils
        // ↑ TBC NOT SURE ABOUT THIS ↑
        mAdapter = new ArticleAdapter(this, new ArrayList<Article>());
        listView.setAdapter(mAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Article currentArticle = mAdapter.getItem(position);
                Uri articleUri = Uri.parse(currentArticle.getUrl());
                Intent websiteIntent = new Intent(Intent.ACTION_VIEW, articleUri);
                startActivity(websiteIntent);
            }
        });

        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()) {
            loaderManager.initLoader(ARTICLE_LOADER_ID, null, this);
        } else {
            View loadingIndicator = findViewById(R.id.loading_indicator);
            loadingIndicator.setVisibility(View.GONE);
            mEmptyStateTextView.setText(R.string.no_internet);
            loaderManager.restartLoader(ARTICLE_LOADER_ID, null, this);
        }

        swipeLayout.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        loaderManager.restartLoader(ARTICLE_LOADER_ID, null, ArticleActivity.this);
                    }
                }
        );

    }


    @Override
    public Loader<List<Article>> onCreateLoader(int i, Bundle bundle) {

        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);

        String apiKey = sharedPrefs.getString(
                getString(R.string.settings_api_key),
                getString(R.string.settings_api_key_default)
        );

        String orderBy = sharedPrefs.getString(
                getString(R.string.settings_order_by_key),
                getString(R.string.settings_order_by_default)
        );

        // Builds the complete uri `https://content.guardianapis.com/profile/editorial?order-by=newest&show-fields=headline&page-size=50'
        // and adds the user personal API key (which he can now copy-paste)
        Uri.Builder uriBuilder = new Uri.Builder();
        uriBuilder.scheme("https")
                .authority("content.guardianapis.com")
                .appendPath("profile")
                .appendPath("editorial")
                .appendQueryParameter("order-by", orderBy)
                .appendQueryParameter("show-fields", "headline")
                .appendQueryParameter("page-size", "50")
                .appendQueryParameter("api-key", apiKey);

        final String guardianRequestURL = uriBuilder.build().toString();

        return new ArticleLoader(this, guardianRequestURL);
    }

    @Override
    public void onLoadFinished(Loader<List<Article>> loader, List<Article> articles) {

        View loadingIndicator = findViewById(R.id.loading_indicator);
        loadingIndicator.setVisibility(View.GONE);
        mEmptyStateTextView.setText(R.string.no_articles);
        mAdapter.clear();
        SwipeRefreshLayout swipeLayout = findViewById(R.id.swiperefresh);
        swipeLayout.setRefreshing(false);

        if (articles != null && !articles.isEmpty()) {
            mAdapter.addAll(articles);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<Article>> loader) {
        mAdapter.clear();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.settings_button) {
            Intent settingsActivity = new Intent(this, SettingsActivity.class);
            startActivity(settingsActivity);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        loaderManager.restartLoader(ARTICLE_LOADER_ID, null, this);
    }
}