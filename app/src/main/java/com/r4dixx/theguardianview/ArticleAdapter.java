package com.r4dixx.theguardianview;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Random;

public class ArticleAdapter extends ArrayAdapter<Article> {

    // Title should be the part after PREFIX and before TITLE_SEPARATOR
    // Description the part after TITLE_SEPARATOR
    private static final String PREFIX = "The Guardian view on ";
    private static final String PREFIX_BIS = "The Guardian view of ";
    private static final String PREFIX_TER = "The Guardian view about ";
    private static final String TITLE_SEPARATOR = ": ";
    private static final String TIME_SEPARATOR = "T";

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
        String time = currentArticle.getTime();
        String thumbnailUrl = currentArticle.getThumbnailUrl();

        TextView cardTitle = listItemView.findViewById(R.id.cardTitle);
        TextView cardDesc = listItemView.findViewById(R.id.cardDesc);
        TextView cardDate = listItemView.findViewById(R.id.cardDate);
        Button cardButton = listItemView.findViewById(R.id.cardButton);
        TextView cardUrl = listItemView.findViewById(R.id.thumbnailUrl);

        // originalTitle = The Guardian view on home-schooling in England: a register is needed
        // titleSimplified = home-schooling in England
        // desc = a register is needed
        // titlePropercase = Home-schooling in England
        // descPropercase = A register is needed
        //
        // If anything goes wrong from The Guardian side
        // i.e. article wrongly sorted falling in this category
        // (or title not following the usual PREFIXes naming convention)
        // skip the description and only show original title with no modification
        // TODO: Find a better way to implement this logic. This is very verbose and not clean
        if (originalTitle.contains(PREFIX)) {
            // Splits the original title into title and description
            String[] parts = originalTitle.split(TITLE_SEPARATOR);
            String title = parts[0];
            String desc = parts[1];

            // Gets rid of the prefix
            String titleSimplified = title.replace(PREFIX, "");

            // Propercase = Capitalize first letter
            String titlePropercase = titleSimplified.substring(0, 1).toUpperCase() + titleSimplified.substring(1);
            String descPropercase = desc.substring(0, 1).toUpperCase() + desc.substring(1);

            cardTitle.setText(titlePropercase);
            cardDesc.setText(descPropercase);
        } else if (originalTitle.contains(PREFIX_BIS)) {
            String[] parts = originalTitle.split(TITLE_SEPARATOR);
            String title = parts[0];
            String desc = parts[1];
            String titleSimplified = title.replace(PREFIX_BIS, "");
            String titlePropercase = titleSimplified.substring(0, 1).toUpperCase() + titleSimplified.substring(1);
            String descPropercase = desc.substring(0, 1).toUpperCase() + desc.substring(1);
            cardTitle.setText(titlePropercase);
            cardDesc.setText(descPropercase);
        } else if (originalTitle.contains(PREFIX_TER)) {
            String[] parts = originalTitle.split(TITLE_SEPARATOR);
            String title = parts[0];
            String desc = parts[1];
            String titleSimplified = title.replace(PREFIX_TER, "");
            String titlePropercase = titleSimplified.substring(0, 1).toUpperCase() + titleSimplified.substring(1);
            String descPropercase = desc.substring(0, 1).toUpperCase() + desc.substring(1);
            cardTitle.setText(titlePropercase);
            cardDesc.setText(descPropercase);
        } else {
            cardTitle.setText(originalTitle);
            cardDesc.setVisibility(View.GONE);
        }

        if ((thumbnailUrl.equals("") || thumbnailUrl.equals("null") || thumbnailUrl.isEmpty())) {
            cardUrl.setVisibility(View.GONE);
            CardView cardUrlContainer = listItemView.findViewById(R.id.thumbnail_container);
            cardUrlContainer.setVisibility(View.GONE);
        } else {
            cardUrl.setText(thumbnailUrl);
        }

        String[] parts = time.split(TIME_SEPARATOR);
        String date = parts[0];
        cardDate.setText(date);

        // Random text in buttons in an array of 4 strings (see strings.xml)
        // TODO: Move this piece to hook up buttons with their parent Card (probably a getter to set in Article)
        String[] arrayOfStrings = getContext().getResources().getStringArray(R.array.button_text);
        String randomString = arrayOfStrings[new Random().nextInt(arrayOfStrings.length)];
        cardButton.setText(randomString);

        return listItemView;

    }
}

