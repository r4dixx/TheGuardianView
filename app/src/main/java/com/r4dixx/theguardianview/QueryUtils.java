package com.r4dixx.theguardianview;

import android.content.SharedPreferences;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Helper methods to request and receive article data from The Guardian.
 */
public final class QueryUtils {

    private static final String LOG_TAG = QueryUtils.class.getSimpleName();

    private QueryUtils() {
    }

    private static final int MAX_READ_TIMEOUT = 10000;
    private static final int MAX_CONNECTION_TIMEOUT = 15000;

    /**
     * Return a list of {@link Article} objects that has been built up from
     * parsing a JSON response.
     */
    private static List<Article> extractFeatureFromJson(String guardianJSON) {

        // Create an empty ArrayList that we can start adding articles to
        List<Article> articles = new ArrayList<>();

        // Try to parse the JSON Response. If there's a problem with the way the JSON
        // is formatted, a JSONException object will be thrown.
        // Catch the exception so the app doesn't crash, and print the error message to the logs.
        try {
            // Create a JSONObject from the JSON response string
            JSONObject baseJsonResponse = new JSONObject(guardianJSON);
            // In the JSONObject Extract the JSONObject associated with the key called "response",
            JSONObject responseObject = baseJsonResponse.getJSONObject("response");
            // Extract the JSONObject associated with the key called "results",
            JSONArray resultsArray = responseObject.getJSONArray("results");

            // Cycle in the resultsArray and create an Article object for each result
            for (int i = 0; i < resultsArray.length(); i++) {
                JSONObject currentArticle = resultsArray.getJSONObject(i);
                JSONObject fields = currentArticle.getJSONObject("fields");

                String title = fields.optString("headline");
                String url = currentArticle.optString("webUrl");
                String date = currentArticle.optString("webPublicationDate");
                String thumbnailUrl = fields.optString("thumbnail");

                // TODO fix random button texts behavior
                // Random text in buttons in an array of 7 strings (see strings.xml)
                // String[] arrayOfStrings = ContextGetter.getContext().getResources().getStringArray(R.array.button_text);
                // String buttonText = arrayOfStrings[new Random().nextInt(arrayOfStrings.length)];

                Article article = new Article(title, url, date, thumbnailUrl, "Read more");
                articles.add(article);
            }

            // Basically: JSON root → "response" → cycle in the array of "results" → grab "webUrl" and ("fields" → "headline")

        } catch (JSONException e) {
            Log.e("QueryUtils", "Problem parsing the Guardian JSON results", e);
        }

        return articles;
    }

    /**
     * Returns new URL object from the given string URL.
     */
    private static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Problem building the URL ", e);
        }
        return url;
    }

    /**
     * Make an HTTP request to the given URL and return a String as the response.
     */
    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";

        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(MAX_READ_TIMEOUT);
            urlConnection.setConnectTimeout(MAX_CONNECTION_TIMEOUT);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            if (urlConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving JSON results.", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    /**
     * Convert the {@link InputStream} into a String which contains the
     * whole JSON response from the server.
     */
    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

    /**
     * Query the Guardian dataset and return a list of {@link Article} objects.
     */
    public static List<Article> fetchArticlesData(String requestUrl) {
        URL url = createUrl(requestUrl);
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem making the HTTP request.", e);
        }

        return extractFeatureFromJson(jsonResponse);
    }
}
