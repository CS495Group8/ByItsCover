package com.example.byitscover.scrapers;

import com.example.byitscover.helpers.Book;
import com.example.byitscover.helpers.BookListing;
import com.example.byitscover.helpers.Query;
import com.example.byitscover.helpers.Review;
import com.example.byitscover.helpers.Scraper;
import com.example.byitscover.helpers.ScraperConstants;
import com.example.byitscover.helpers.ScraperHelper;
import com.google.api.services.customsearch.model.Result;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.safety.Whitelist;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.math.BigDecimal;
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
     * Standard get info method. Gets the rating and review value from Storygraph for the book
     *
     * @param query Information about the book being searched for
     * @return information from the app/website about the book searched for
     * @throws IOException
     */
    public List<BookListing> scrape(Query query) throws IOException {
        List< Result > results = ScraperHelper.googleAPISearch(ScraperConstants.STORYGRAPH, query);

        String bookUrl = results.get(0).getLink();
        System.out.println("Storygraph- " + bookUrl);

        Document bookDocument = null;
        try {
            bookDocument = Jsoup.connect(bookUrl).get();
        } catch (Exception ex) {
            System.out.println("Book not found by Storygraph");
            bookDocument = Jsoup.connect("https://google.com").get();
        }


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

        Element titleAuthor = bookDocument.getElementsByClass("book-title-and-author").first();

        String author = null, title = null;

        //get title and author
        try {
            Node titleValue = titleAuthor.childNode(1).childNode(0);
            title = Jsoup.clean(Jsoup.parse(titleValue.toString()).text(), Whitelist.simpleText());
        } catch (Exception e) {
            title = "";
        }

        try {
            Node authorValue = bookDocument.childNode(3).childNode(1);
            author = Jsoup.clean(Jsoup.parse(authorValue.toString()).text(), Whitelist.simpleText());
        } catch (Exception e) {
            author = "";
        }

        BookListing listing = new BookListing(new URL(bookUrl),
                ScraperConstants.STORYGRAPH,
                new Book(title, author, null, null),
                rating,
                null,
                reviews,
                null,
                getPrice(bookDocument));

        List<BookListing> listings = new ArrayList<BookListing>();
        listings.add(listing);
        return listings;
    }

    /**
     * This method is used to get the first link that is a website containing information about a
     * single book. If not done, sometimes the author's page or a series page can be chosen rather
     * than one for a single book, which is hopefully the book the user is searching for
     *
     * @param links List of all of the elements seen by Google, usually the first 10
     * @return string of the first link that contains "/book" in the URL which is how Storygraph
     * designates that the link is about a book
     */
    private static String getTopBookLink(List<Result> links) {
        for (Result link : links) {
            if (link.getFormattedUrl().contains("/book")) {
                return link.getFormattedUrl();
            }
        }
        return null;
    }

    /**
     * Normally, this returns the price from the website but Storygraph does not provide pricing
     * information
     * @return price
     */
    private BigDecimal getPrice(Document bookDocument) {
        String priceString = null;
        return null;
    }
}
