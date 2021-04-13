package com.example.byitscover.scrapers;

import com.example.byitscover.helpers.Book;
import com.example.byitscover.helpers.BookListing;
import com.example.byitscover.helpers.Query;
import com.example.byitscover.helpers.Review;
import com.example.byitscover.helpers.Scraper;
import com.example.byitscover.helpers.ScraperConstants;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.safety.Whitelist;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.MalformedURLException;
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
        String bookUrl = getUrlWithQuery(query);
        System.out.println(bookUrl);

        Document bookDocument = null;
        try {
            bookDocument = Jsoup.connect(bookUrl).get();
        } catch (Exception ex) {
            System.out.println("Book not found by Storygraph");
        }

        Elements bookLinks = bookDocument.select("div.ml-4");

        List<BookListing> listings = new ArrayList<BookListing>();
        for (int i = 0; i < Math.min(3, bookLinks.size()); i++) {
            listings.add(getListingFromElement(bookLinks, i, query));
        }

        return listings;
    }

    /**
     * This method grabs the information for the specified book and returns a BookListing object
     * based on the information found on the specific website
     *
     * @param bookLinks this is a list of all of the books found on the website search
     * @param i specifies which listing you want to get the information of
     * @param query
     * @return Book listing based off of the information found on the website
     * @throws MalformedURLException Raised if the new URL() call produces an error due to bad input
     */
    private BookListing getListingFromElement(Elements bookLinks, int i, Query query) throws IOException {
        Double rating;
        String reviewValueString;
        String author = null, title = null;

        String bookUrl = "https://app.thestorygraph.com/" +
                bookLinks.get(i).childNode(1).childNode(0).attr("href");
        Document bookDocument = Jsoup.connect(bookUrl).get();

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

        return listing;
    }

    /**
     * This method forms the url based on the base Url that is hardcoded and the title and
     * author values that are inputted and a part of the query
     * @param query
     * @return the url as a String
     */
    String getUrlWithQuery(Query query) {
        String url = "https://app.thestorygraph.com/browse?utf8=%E2%9C%93&button=&search_term=";
        String toSearch = query.getQuery();
        return url + toSearch.replaceAll(" ", "+");
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
