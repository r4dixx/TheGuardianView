package com.r4dixx.theguardianview;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ListView;

import java.util.ArrayList;

public class ArticleActivity extends AppCompatActivity {

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

        final ArrayList<Article> articles = new ArrayList<>();
        articles.add(new Article("The Guardian view on the Trump-Putin meeting: good for Vladimir, but not for the rest of us"));
        articles.add(new Article("The Guardian view on home-schooling in England: a register is needed"));
        articles.add(new Article("The Guardian view on Donald Trump in Britain: this was the visit from hell"));
        articles.add(new Article("The Guardian view on alien life: what if it’s not there?"));
        articles.add(new Article("The Guardian view on England’s World Cup: savour the wins, learn from the defeat"));
        articles.add(new Article("The Guardian view on the Trump visit: not welcome in Britain"));
        articles.add(new Article("The Guardian view on schools: boost children, not results"));
        articles.add(new Article("The Guardian view on controlling social media: the start of a long road"));
        articles.add(new Article("The Guardian view on China and human rights: standing with Liu Xia"));

        ArticleAdapter adapter = new ArticleAdapter(this, articles);
        ListView listView = findViewById(R.id.article_list);
        listView.setAdapter(adapter);
    }
}
