package com.r4dixx.theguardianview;

import android.content.Context;
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

        TextView cardTitle = listItemView.findViewById(R.id.cardTitle);
        TextView cardDesc = listItemView.findViewById(R.id.cardDesc);
        Button cardButton = listItemView.findViewById(R.id.cardButton);

        // originalTitle = The Guardian view on home-schooling in England: a register is needed
        // titleSimplified = home-schooling in England
        // desc = a register is needed
        // titlePropercase = Home-schooling in England
        // descPropercase = A register is needed
        //

        // If anything goes wrong from The Guardian side
        // i.e. article wrongly sorted falling in this category
        // (or title not following the usual "The Guardian view on" naming convention)
        // skip the description and only show original title with no modification
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
            cardTitle.setPadding(0, 0, 0, 40);
        }

        // Random text in buttons in an array of 4 strings (see strings.xml)
        String[] arrayOfStrings = getContext().getResources().getStringArray(R.array.button_text);
        String randomString = arrayOfStrings[new Random().nextInt(arrayOfStrings.length)];
        cardButton.setText(randomString);

        return listItemView;

    }
}

