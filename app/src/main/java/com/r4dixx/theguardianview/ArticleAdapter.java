package com.r4dixx.theguardianview;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Random;

public class ArticleAdapter extends ArrayAdapter<Article> {

    // Title should be the part after PREFIX and before SEPARATOR
    // Description the part after SEPARATOR
    private static final String PREFIX = "The Guardian view on ";
    private static final String SEPARATOR = ": ";

    public ArticleAdapter(Context context, ArrayList<Article> articles) {
        super(context, 0, articles);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.article_list_item, parent, false);
        }

        Article currentArticle = getItem(position);
        String originalTitle = currentArticle.getTitle();

        CardView cardView = listItemView.findViewById(R.id.cardView);
        TextView cardTitle = listItemView.findViewById(R.id.cardTitle);
        TextView cardDesc = listItemView.findViewById(R.id.cardDesc);
        Button cardButton = listItemView.findViewById(R.id.cardButton);

        if (originalTitle.contains(PREFIX)) {

            // Splits the original title into title and description
            String[] parts = originalTitle.split(SEPARATOR);
            String title = parts[0];
            String desc = parts[1];

            // Gets rid of the prefix
            String titleSimplified = title.replace(PREFIX, "");

            // Propercase = Capitalize first letter
            String titlePropercase = titleSimplified.substring(0, 1).toUpperCase() + titleSimplified.substring(1);
            String descPropercase = desc.substring(0, 1).toUpperCase() + desc.substring(1);

            cardTitle.setText(titlePropercase);
            cardDesc.setText(descPropercase);
        } else {
            cardTitle.setText(originalTitle);
            cardDesc.setVisibility(View.GONE);
            cardTitle.setPadding(0,0,0,40);
        }

        String[] arrayOfStrings = getContext().getResources().getStringArray(R.array.button_text);
        String randomString = arrayOfStrings[new Random().nextInt(arrayOfStrings.length)];
        cardButton.setText(randomString);

        return listItemView;

    }
}

