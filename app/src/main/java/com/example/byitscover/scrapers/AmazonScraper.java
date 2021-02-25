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
        Map<String, String> toReturn = new HashMap<String, String>();

        //get rating value
        Element ratingValue = amazonPage.selectFirst("div[class = AverageCustomerReviews]");

        return toReturn;
    }
}
