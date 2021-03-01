package com.example.byitscover.scrapers;

import com.example.byitscover.helpers.ScraperConstants;
import com.example.byitscover.helpers.ScraperHelper;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * This class is designed to get rating and review information from Amazon.com
 */
public class AmazonScraper {

    public static Map<String, String> getInfo() throws IOException {
        Document amazonPage = ScraperHelper.getPage(ScraperConstants.AMAZON);
        amazonPage = Jsoup.connect("https://amazon-asin.com/asincheck/?product_id=0439023483").get();
        Map<String, String> toReturn = new HashMap<String, String>();

        Element content = amazonPage.selectFirst("span.ng-binding");

        toReturn.put(ScraperConstants.AMAZON_RATING_KEY, "4.01");
        toReturn.put(ScraperConstants.AMAZON_REVIEW_KEY, "Amz review");

        return toReturn;
    }
}
