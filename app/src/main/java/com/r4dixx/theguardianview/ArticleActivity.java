package com.r4dixx.theguardianview;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class ArticleActivity extends AppCompatActivity {

    // TODO: Append API key from api_key.xml (instead of copy-pasting it here)
    private static final String GUARDIAN_REQUEST_URL = "https://content.guardianapis.com/search?order-by=relevance&show-fields=headline%2Cbyline&q=%22The%20Guardian%20view%20on%22%20AND%20%22%20%7C%20Editorial%22&api-key=";

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

        // This is where we ask the adapter to go fetch the ArrayList in QueryUtils
        mAdapter = new ArticleAdapter(this, new ArrayList<Article>());
        listView.setAdapter(mAdapter);

        ArticleAsyncTask task = new ArticleAsyncTask();
        task.execute(GUARDIAN_REQUEST_URL);

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
    }

    /**
     * {@link AsyncTask} to perform the network request on a background thread, and then
     * update the UI with the list of articles in the response.
     */
    private class ArticleAsyncTask extends AsyncTask<String, Void, List<Article>> {

        @Override
        protected List<Article> doInBackground(String... urls) {
            if (urls.length < 1 || urls[0] == null) {
                return null;
            }
            List<Article> result = QueryUtils.fetchArticlesData(urls[0]);
            return result;
        }

        @Override
        protected void onPostExecute(List<Article> data) {

            mAdapter.clear();

            if (data != null && !data.isEmpty()) {
                mAdapter.addAll(data);
            }
        }
    }
}
