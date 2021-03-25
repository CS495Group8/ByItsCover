package com.example.byitscover.scrapers;

import com.example.byitscover.helpers.Book;
import com.example.byitscover.helpers.BookListing;
import com.example.byitscover.helpers.Query;
import com.example.byitscover.helpers.Review;
import com.example.byitscover.helpers.Scraper;
import com.example.byitscover.helpers.ScraperConstants;
import com.example.byitscover.helpers.ScraperHelper;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.safety.Whitelist;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * This class is designed to get rating and review information from Amazon.com
 *
 * @author Marc
 * @version 1.0
 */
public class StorygraphScraper implements Scraper {

    public StorygraphScraper() {

    }

    /**
     * Standard get info method. Gets the rating and review value from Amazon for the book
     *
     * @return map of the keys and strings for the info gotten from Amazon
     * @throws IOException
     */
    public List<BookListing> scrape(Query query) throws IOException {
        String url = ScraperHelper.getGoogleUrlNoAPI(ScraperConstants.STORYGRAPH, query);
        Document googlePage = Jsoup.connect(url).get();

        //go to first search result link
        Elements searchResults = googlePage.select("div.g");
        List<Element> links = new ArrayList<Element>();
        for (int i = 0; i < 10; i++) {
            if (i == 0) {
                links.add((Element) searchResults.get(i).childNode(1).childNode(0).childNode(0));
            }
            else {
                links.add((Element) searchResults.get(i).childNode(0).childNode(0).childNode(0));
            }
        }

        String bookUrl = getTopBookLink(links);

        Document bookDocument = Jsoup.connect(bookUrl).get();

        Double rating;
        String reviewValueString;

        try {
            Node ratingValue = bookDocument.selectFirst("span.average-star-rating").childNode(0);
            rating = Double.valueOf(ratingValue.toString());
        } catch (Exception e) {
            rating = 0.0;
        }

        try {
            Node reviewValue = bookDocument.selectFirst("div.blurb-pane");
            reviewValueString = Jsoup.clean(Jsoup.parse(reviewValue.toString()).text(), Whitelist.simpleText());
        } catch (Exception e) {
            reviewValueString = "";
        }

        Review review = new Review(null, reviewValueString, null);
        List<Review> reviews = new ArrayList<Review>();
        reviews.add(review);

        BookListing listing = new BookListing(new URL(bookUrl),
                ScraperConstants.STORYGRAPH,
                new Book(query.getTitle(), query.getAuthor(), null, null),
                rating,
                null,
                reviews,
                null);

        List<BookListing> listings = new ArrayList<BookListing>();
        listings.add(listing);
        return listings;
    }

    private static String getTopBookLink(List<Element> links) {
        String currentLink;
        for (Element link : links) {
            currentLink = link.attr("abs:href");
            if (currentLink.contains("/book")) {
                return currentLink;
            }
        }
        return null;
    }

}
