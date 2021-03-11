package com.example.byitscover.scrapers;

import com.example.byitscover.helpers.Book;
import com.example.byitscover.helpers.BookListing;
import com.example.byitscover.helpers.Scraper;
import com.example.byitscover.helpers.ScraperConstants;
import com.example.byitscover.helpers.ScraperHelper;
import com.example.byitscover.helpers.Review;
import com.example.byitscover.helpers.Query;
import com.google.api.services.customsearch.model.Result;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.safety.Whitelist;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BarnesAndNobleScraper implements Scraper {
    public BarnesAndNobleScraper() {

    }

    public List<BookListing> scrape(Query query) throws IOException {
        List<Result> results = ScraperHelper.googleAPISearch(ScraperConstants.BARNES_AND_NOBLE, query);
        String searchingUrl = getActualBookResult(results);
        Document document = Jsoup.connect(searchingUrl).get();
        System.out.println(searchingUrl);

        Map<String, String> toReturn = new HashMap<String, String>();

        //get rating by parsing json js var
        String htmlString = document.html();
        Double rating = null;

        try {
            rating = Double.parseDouble(htmlString.substring(htmlString.indexOf(",\"rating\":") + 10,
                    htmlString.indexOf(",\"rating\":") + 13));
        } catch (NumberFormatException ex) {

        }

        //get review
        Element reviewValue = (Element) document.getElementById("overviewSection").childNode(1).childNode(1)
                .childNode(3).childNode(1).childNode(1).childNode(1);

        Review review = new Review(null, Jsoup.clean(reviewValue.toString(), Whitelist.none()), null);
        List<Review> reviews = new ArrayList<Review>();
        reviews.add(review);

        BookListing listing = new BookListing(new URL(ScraperHelper.getTopResultUrl(results)),
                ScraperConstants.BARNES_AND_NOBLE,
                new Book(query.getTitle(), query.getAuthor(), null, null),
                rating,
                null,
                reviews,
                null);

        List<BookListing> listings = new ArrayList<BookListing>();
        listings.add(listing);
        return listings;
    }

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