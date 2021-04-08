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
        BookListing firstListing = getListingFromElement(bookLinks, 0, query);
        listings.add(firstListing);

        if (bookLinks.size() > 1) {
            BookListing secondListing = getListingFromElement(bookLinks, 1, query);
            listings.add(secondListing);
        }
        if (bookLinks.size() > 2) {
            BookListing thirdListing = getListingFromElement(bookLinks, 2, query);
            listings.add(thirdListing);
        }

        return listings;
    }

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

    private String getUrlWithQuery(Query query) {
        String url = "https://app.thestorygraph.com/browse?utf8=%E2%9C%93&button=&search_term=";
        String toSearch = query.getTitle() + query.getAuthor();
        return url + toSearch.replaceAll(" ", "+");
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
