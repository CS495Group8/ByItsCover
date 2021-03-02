package com.example.byitscover.scrapers;

import com.example.byitscover.helpers.ScraperConstants;
import com.example.byitscover.helpers.ScraperHelper;
import com.google.api.services.customsearch.model.Result;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.safety.Whitelist;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The Class scrapes the Barnes and Noble website to get the rating and review from their website
 *
 * @version 1.0
 * @author Marc
 */
public class BarnesAndNobleScraper {
    /**
     * Standard method to scrape the data and return it to reviewPage. This one is finicky as to
     * get the rating you have to search a string of json to get it
     *
     * @return map of the info
     * @throws IOException
     */
    public static Map<String, String> getInfo() throws IOException {
        List<Result> results = ScraperHelper.googleAPISearch(ScraperConstants.BARNES_AND_NOBLE);
        String searchingUrl = getActualBookResult(results);
        Document document = Jsoup.connect(searchingUrl).get();
        System.out.println(searchingUrl);

        Map<String, String> toReturn = new HashMap<String, String>();

        //get rating by parsing json js var
        String htmlString = document.html();
        String subString = htmlString.substring(htmlString.indexOf(",\"rating\":")+10,
                htmlString.indexOf(",\"rating\":")+13);
        subString = subString.replaceAll("[^\\d.]", ""); //get rid of non-numeric
        toReturn.put(ScraperConstants.BAN_RATING_KEY, subString);

        //get review
        Element reviewValue = (Element) document.getElementById("overviewSection").childNode(1).childNode(1)
                .childNode(3).childNode(1).childNode(1).childNode(1);

        toReturn.put(ScraperConstants.BAN_REVIEW_KEY ,
                Jsoup.clean(reviewValue.toString(), Whitelist.none()));

        return toReturn;
    }

    /**
     * Google had some weird behavior where the first result wasn't always the actual book that
     * was searched for. This function allows the app to get the first result that is actually a
     * book from Barnes and Noble rather than just the first result. The "/w" is only in URLs with
     * products on their site
     *
     * @param results top 10 results from the Google API
     * @return Url of the first result in the 10 that is a book
     */
    private static String getActualBookResult(List<Result> results) {
        String toReturn = "";
        for (int i = 0; i < results.size(); i++) {
            if (results.get(i).getFormattedUrl().contains("/w/")) {
                toReturn = results.get(i).getFormattedUrl();
                break;
            }
        }
        return toReturn;
    }
}
