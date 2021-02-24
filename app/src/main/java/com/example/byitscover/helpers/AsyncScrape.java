package com.example.byitscover.helpers;

import android.os.AsyncTask;

import com.example.byitscover.scrapers.AmazonScraper;
import com.example.byitscover.scrapers.GoodreadsScraper;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * This class asynchronously calls the functions to scrape data and collects it once returned.
 * The developer has the option to choose which scraping website is chosen when instantiating the
 * class. The class executes from a <code>new AsyncScrape(ScraperConstants.site).execute();</code>
 * call. The doInBackground starts when called. The on post execute is required. The string
 * called scrapeSite holds the site that the data is coming from for the instance.
 *
 * @author Marc
 * @version 1.0
 *
 */
public class AsyncScrape extends AsyncTask<Void, Void, Map<String, String>> {

    public static String scrapeSite;

    /**
     * Constructor for the class. Called like this: <code>new AsyncScrape(scrapeSiteIn).execute();
     * </code>. Using <code>super()</code> to make sure it creates an AsyncTask
     *
     * @param scrapeSiteIn
     */
    public AsyncScrape(String scrapeSiteIn) {
        super();
        scrapeSite = scrapeSiteIn;
    }

    /**
     * Main method for the class. Determines which site it is to go scrape, and then scrapes for the
     * info needed. then adds the info to the singleton holding the data
     *
     * @param params are essentially none
     * @return although something is returned, it isn't really do to the nature of AsyncTask
     */
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
                        break;
                    case ScraperConstants.AMAZON:
                        Map<String, String> valuesFromAmazon = AmazonScraper.getInfo();
                        valuesfromInstance = instance.getReviewRatingValues();

                        Map<String, String> resultAmz = new HashMap<>();
                        resultAmz.putAll(valuesFromAmazon);
                        if (valuesfromInstance != null) {
                            resultAmz.putAll(valuesfromInstance);
                        }

                        instance.setReviewRatingValues(resultAmz);
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


    /**
     * Just called once the scraping is done, returns to caller.
     *
     * @param reviewRatingValues Just the info returned from <code>doInBackground()</code>
     */
    @Override
    protected void onPostExecute(Map<String, String> reviewRatingValues) {
        return;
    }

}
