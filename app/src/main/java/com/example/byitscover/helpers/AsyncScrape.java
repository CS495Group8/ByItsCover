package com.example.byitscover.helpers;

import android.os.AsyncTask;

import com.example.byitscover.scrapers.GoodreadsScraper;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class AsyncScrape extends AsyncTask<Void, Void, Map<String, String>> {

    public static String scrapeSite;

    public AsyncScrape(String scrapeSiteIn) {
        super();
        scrapeSite = scrapeSiteIn;
    }

    @Override
    protected Map<String, String> doInBackground(Void... params) {
        CurrentBook instance = CurrentBook.getInstance();
        Map<String, String> valuesfromInstance = new HashMap<>();
        if (instance.getAuthor() != null && instance.getTitle() != null) {
            try {
                switch(scrapeSite) {
                    case ScraperConstants.GOODREADS:
                        Map<String, String> valuesFromGoodreads = GoodreadsScraper.getInfo();
                        valuesfromInstance = instance.getReviewRatingValues();

                        Map<String, String> result = new HashMap<>();
                        result.putAll(valuesFromGoodreads);
                        if (valuesfromInstance != null) {
                            result.putAll(valuesfromInstance);
                        }

                        instance.setReviewRatingValues(result);
                        break;
                    case ScraperConstants.AMAZON:
                        break;
                    case ScraperConstants.BARNES_AND_NOBLE:
                        break;
                    case ScraperConstants.GOOGLE_BOOKS:
                        break;
                }
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
