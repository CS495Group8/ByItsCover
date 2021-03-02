package com.example.byitscover.helpers;

import android.os.AsyncTask;

import com.example.byitscover.scrapers.AmazonScraper;
import com.example.byitscover.scrapers.BarnesAndNobleScraper;
import com.example.byitscover.scrapers.GoodreadsScraper;
import com.example.byitscover.scrapers.GoogleScraper;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

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
        Map<String, String> valuesFromSite = new HashMap<>();
        if (instance.getAuthor() != null && instance.getTitle() != null) {
            try {
                switch(scrapeSite) {
                    case ScraperConstants.GOODREADS:
                        valuesFromSite = GoodreadsScraper.getInfo();
                        break;
                    case ScraperConstants.AMAZON:
                        valuesFromSite = AmazonScraper.getInfo();
                        break;
                    case ScraperConstants.BARNES_AND_NOBLE:
                        valuesFromSite = BarnesAndNobleScraper.getInfo();
                        break;
                    case ScraperConstants.GOOGLE_BOOKS:
                        valuesFromSite = GoogleScraper.getInfo();
                        break;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            Map<String, String> valuesfromInstance = instance.getReviewRatingValues();

            Map<String, String> result = new HashMap<>();
            result.putAll(valuesFromSite);
            if (valuesfromInstance != null) {
                result.putAll(valuesfromInstance);
            }

            instance.setReviewRatingValues(result);
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
