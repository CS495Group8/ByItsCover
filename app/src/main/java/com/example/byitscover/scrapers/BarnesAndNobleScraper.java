package com.example.byitscover.scrapers;

import com.example.byitscover.helpers.Book;
import com.example.byitscover.helpers.BookListing;
import com.example.byitscover.helpers.Scraper;
import com.example.byitscover.helpers.ScraperConstants;
import com.example.byitscover.helpers.ScraperHelper;
import com.example.byitscover.helpers.Review;
import com.example.byitscover.helpers.Query;
import com.google.api.services.customsearch.model.Result;
import com.google.common.annotations.VisibleForTesting;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.safety.Whitelist;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The Class scrapes the Barnes and Noble website to get the rating and review from their website
 *
 * @version 1.0
 * @author Marc
 */
public class BarnesAndNobleScraper implements Scraper {
    public BarnesAndNobleScraper() {

    }

    /**
     * Standard method to scrape the data and return it to reviewPage. This one is finicky as to
     * get the rating you have to search a string of json to get it
     *
     * @param query this contains the book information that is being looked for on the website
     * @return list of the book listings info found on the Barnes and Noble website
     * @throws IOException
     */
    public List<BookListing> scrape(Query query) throws IOException {
        List<Result> results = ScraperHelper.googleAPISearch(ScraperConstants.BARNES_AND_NOBLE, query);
        String searchingUrl = getActualBookResult(results);
        Document document = Jsoup.connect(searchingUrl).get();
        System.out.println(searchingUrl);

        Map<String, String> toReturn = new HashMap<String, String>();

        //get rating by parsing json js var
        String htmlString = document.html();
        Double rating = null;
        String reviewValueString;

        try {
            rating = Double.parseDouble(htmlString.substring(htmlString.indexOf(",\"rating\":") + 10,
                    htmlString.indexOf(",\"rating\":") + 13));
        } catch (Exception ex) {
            rating = 0.0;
        }

        URL coverUrl;
        try {
            Node bookCoverValue = document.getElementById("pdpMainImage");
            coverUrl = new URL(bookCoverValue.absUrl("src"));
        } catch (Exception e) {
            coverUrl = null;
        }


        try {
            //get review
            Element reviewValue = (Element) document.getElementById("overviewSection").childNode(1).childNode(1)
                    .childNode(3).childNode(1).childNode(1).childNode(1);
            reviewValueString = Jsoup.clean(reviewValue.toString(), Whitelist.none());
        } catch (Exception ex) {
            reviewValueString = "";
        }

        Review review = new Review(null, reviewValueString, null);
        List<Review> reviews = new ArrayList<Review>();
        reviews.add(review);

        BookListing listing = new BookListing(new URL(ScraperHelper.getTopResultUrl(results)),
                ScraperConstants.BARNES_AND_NOBLE,
                new Book(query.getTitle(), query.getAuthor(), null, null),
                rating,
                null,
                reviews,
                coverUrl,
                getPrice(document));

        List<BookListing> listings = new ArrayList<BookListing>();
        listings.add(listing);
        return listings;
    }

    /**
     * This method returns the price found on the website
     * @return price
     */
    private BigDecimal getPrice(Document document) {
        String priceString;
        try {
            Element priceValue = (Element) document.getElementById("pdp-cur-price");
            priceString = priceValue.childNode(1).toString();
            return new BigDecimal(priceString);
        } catch (Exception ex) {
            priceString = null;
        }
        return null;
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
    @VisibleForTesting
    static String getActualBookResult(List<Result> results) {
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