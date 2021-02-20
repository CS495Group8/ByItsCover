package com.example.byitscover.helpers;

import android.os.AsyncTask;

import com.example.byitscover.scrapers.GoodreadsScraper;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class AsyncScrape extends AsyncTask<Void, Void, Map<String, String>> {

    @Override
    protected Map<String, String> doInBackground(Void... params) {
        CurrentBook instance = CurrentBook.getInstance();
        Map<String, String> values = new HashMap<>();
        if (instance.getAuthor() != null && instance.getTitle() != null) {
            try {
                //Add values to map here
                values.putAll(GoodreadsScraper.getInfo());
                //other scraper calls go here
                instance.setReviewRatingValues(values);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return instance.getReviewRatingValues();
    }


    @Override
    protected void onPostExecute(Map<String, String> reviewRatingValues) {
        return;
    }
}
