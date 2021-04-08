package com.example.byitscover.scrapers;

import com.example.byitscover.helpers.Book;
import com.example.byitscover.helpers.BookListing;
import com.example.byitscover.helpers.Scraper;
import com.example.byitscover.helpers.ScraperConstants;
import com.example.byitscover.helpers.Review;
import com.example.byitscover.helpers.Query;
import com.google.api.services.customsearch.model.Result;
import com.google.common.annotations.VisibleForTesting;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.safety.Whitelist;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

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
        String url = getUrlWithQuery(query);
        System.out.println(url);
        Document document = Jsoup.connect(url).get();
        Elements bookLinks = document.getElementsByClass("pImageLink ");

        List<BookListing> listings = new ArrayList<BookListing>();

        BookListing firstListing = getListingFromElement(bookLinks, 0, query);
        listings.add(firstListing);

        if (listings.size() > 1) {
            BookListing secondListing = getListingFromElement(bookLinks, 1, query);
            listings.add(secondListing);
        }
        if (listings.size() > 1) {
            BookListing thirdListing = getListingFromElement(bookLinks, 2, query);
            listings.add(thirdListing);
        }

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

    /**
     * This method forms the url based on the base Url that is hardcoded and the title and
     * author values that are inputted and a part of the query
     * @param query
     * @return the url as a String
     */
    private String getUrlWithQuery(Query query) {
        String url = "https://www.barnesandnoble.com/s/"
                + query.getQuery().replaceAll(" ", "%20");
        return url;
    }

    /**
     * This method grabs the information for the specified book and returns a BookListing object
     * based on the information found on the specific website
     *
     * @param elements this is a list of all of the books found on the website search
     * @param i specifies which listing you want to get the information of
     * @param query
     * @return Book listing based off of the information found on the website
     * @throws MalformedURLException Raised if the new URL() call produces an error due to bad input
     */
    private BookListing getListingFromElement(Elements elements, int i, Query query) throws MalformedURLException {
        Document document = new Document(null);
        String url = "";
        try {
            url = "https://www.barnesandnoble.com" + elements.get(i).attr("href");
            System.out.println(url);
            document = Jsoup.connect(url).get();
        } catch (Exception e) {
            System.out.println("Book number " + (i+1) + " is not available");
        }
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

        try {
            //get review
            Element reviewValue = (Element) document.getElementById("overviewSection").childNode(1).childNode(1)
                    .childNode(3).childNode(1).childNode(1).childNode(1);
            reviewValueString = Jsoup.clean(reviewValue.toString(), Whitelist.none());
        } catch (Exception ex) {
            reviewValueString = "";
        }

        //get title and author
        Element titleElement = document.getElementsByClass("pdp-header-title ").get(0);
        Element authorElement = document.getElementById("key-contributors");

        String titleString = titleElement.childNode(0).toString();
        String authorString = authorElement.childNode(1).childNode(0).toString();

        Review review = new Review(null, reviewValueString, null);
        List<Review> reviews = new ArrayList<Review>();
        reviews.add(review);

        BookListing listing = new BookListing(new URL(url),
                ScraperConstants.BARNES_AND_NOBLE,
                new Book(titleString, authorString, null, null),
                rating,
                null,
                reviews,
                null,
                getPrice(document));

        return listing;
    }
}