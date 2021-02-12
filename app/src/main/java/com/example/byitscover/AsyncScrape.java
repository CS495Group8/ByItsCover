package com.example.byitscover;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.view.View;
import android.widget.TextView;

import com.example.byitscover.scrapers.GoodreadsScraper;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class AsyncScrape extends AsyncTask<Void, Void, Map<String, Double>> {

    @Override
    protected Map<String, Double> doInBackground(Void... params) {
        CurrentBook instance = CurrentBook.getInstance();
        Map<String, Double> values = new HashMap<String, Double>();
        if (instance.getAuthor() != null && instance.getTitle() != null) {
            try {
                //Add values to map here
                values.put("Goodreads", GoodreadsScraper.getAverageReviewScore());
                instance.setReviewValues(values);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return instance.getReviewValues();
    }


    @Override
    protected void onPostExecute(Map<String, Double> reviewValues) {
        System.out.print("Goodreads output: ");
        System.out.println(reviewValues.get("Goodreads"));

        return;
    }
}
