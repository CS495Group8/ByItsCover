package com.example.byitscover;

import android.os.AsyncTask;

import com.example.byitscover.scrapers.GoodreadsScraper;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class AsyncScrape extends AsyncTask<Void, Void, Map<String, Double>> {

    @Override
    protected Map<String, Double> doInBackground(Void... params) {
        Map<String, Double> reviewValues = new HashMap<String, Double>();
        CurrentBook instance = CurrentBook.getInstance();
        if (instance.getAuthor() != null && instance.getTitle() != null) {
            try {
                //Add values to map here
                reviewValues.put("Goodreads", GoodreadsScraper.getAverageReviewScore());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return reviewValues;
    }


    @Override
    protected void onPostExecute(Map<String, Double> reviewValues) {
        System.out.print("Goodreads output: ");
        System.out.println(reviewValues.get("Goodreads"));
    }
}
