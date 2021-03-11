package com.example.byitscover.scrapers;

import android.content.res.Resources;
import android.webkit.WebView;

import com.example.byitscover.helpers.ScraperConstants;
import com.example.byitscover.helpers.ScraperHelper;
import com.google.api.services.customsearch.model.Result;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class is designed to get rating and review information from Amazon.com
 *
 * @author Marc
 * @version 1.0
 */
public class StorygraphScraper {

    /**
     * Standard get info method. Gets the rating and review value from Amazon for the book
     *
     * @return map of the keys and strings for the info gotten from Amazon
     * @throws IOException
     */
    public static Map<String, String> getInfo() throws IOException {
        String url = ScraperHelper.getGoogleUrlNoAPI(ScraperConstants.STORYGRAPH);
        Document googlePage = Jsoup.connect(url).get();
        //go to first search result link
        Element link = (Element) googlePage.select("div.g").first().childNode(1)
                .childNode(0).childNode(0);
        String bookUrl = link.attr("abs:href");
        Document bookDocument = Jsoup.connect(bookUrl).get();

        Map<String, String> toReturn = new HashMap<String, String>();

        Node ratingValue = bookDocument.selectFirst("span.average-star-rating").childNode(0);
        toReturn.put(ScraperConstants.STORYGRAPH_RATING_KEY, ratingValue.toString());

        Node reviewValue = bookDocument.selectFirst("div.blurb-pane").childNode(0);
        toReturn.put(ScraperConstants.STORYGRAPH_REVIEW_KEY, reviewValue.toString());

        return toReturn;
    }

}
