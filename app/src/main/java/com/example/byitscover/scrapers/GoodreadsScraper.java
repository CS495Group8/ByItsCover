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
import org.jsoup.nodes.TextNode;
import org.jsoup.safety.Whitelist;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * As the name implies, this is the scraper to get information from goodreads.com. Consists of only
 * the one method that gets the info from the website
 *
 * @author Marc
 * @version 1.0
 * @see <a href="goodreads.com">goodreads.com</a>
 */
public class GoodreadsScraper implements Scraper {
    public GoodreadsScraper() {

    }

    /**
     * This class is the main driver of the class. Starts by getting the author and title info
     * from the singleton and then forms the url that needs to be scraped. Once the title and author
     * are searched for, the scraper then gets the link to the top search result and then connects
     * to that link to get the rating and review information.
     *
     * @return the information from the website
     * @throws IOException if the text is not found to be returned from the site
     */
    public List<BookListing> scrape(Query query) throws IOException {
        //Get google url, make sure there are no newlines, and connect to it
        List<Result> results = ScraperHelper.googleAPISearch(ScraperConstants.GOODREADS, query);
        String searchingUrl = ScraperHelper.getTopResultUrl(results);
        searchingUrl.replaceAll("[\\n]", "");
        Document bookDocument = Jsoup.connect(searchingUrl).get();
        System.out.println(searchingUrl);

        //get rating value
        Element ratingElement = bookDocument.selectFirst("[itemprop=ratingValue]");
        Double rating = null;
        String reviewString;
        String author;
        String title;
        URL coverUrl = null;

        try {
            rating = Double.parseDouble(((TextNode) ratingElement.childNode(0)).getWholeText());
        } catch (Exception ex) {
            rating = 0.0;
        }

        //get review text
        try {
            Element reviewElement = bookDocument.selectFirst("div#description").selectFirst("span");
            reviewString = Jsoup.clean(reviewElement.childNode(0).toString(), Whitelist.none());
        } catch (Exception ex) {
            reviewString = "";
        }

        Review review = new Review(null, reviewString, null);

        //get author of book from Goodreads
        try {
            Element authorElement = bookDocument.selectFirst("div#bookAuthors");
            author = authorElement.childNode(3).childNode(1).childNode(1).childNode(0).childNode(0).toString();
        } catch (Exception ex) {
            author = "";
        }

        //get title of book from Goodreads
        try {
            Element titleElement = bookDocument.selectFirst("h1#bookTitle");
            title = titleElement.childNode(0).toString();
        } catch (Exception ex) {
            title = "";
        }

        //get picture url
        try {
            Element coverUrlElement = (Element) bookDocument.selectFirst("div.bookCoverPrimary")
                    .childNode(1).childNode(0);
            coverUrl = new URL(coverUrlElement.absUrl("src"));
        } catch (Exception ex) {

        }

        List<Review> reviews = new ArrayList<Review>();
        reviews.add(review);

        BookListing listing = new BookListing(new URL(ScraperHelper.getTopResultUrl(results)),
                ScraperConstants.GOODREADS,
                new Book(title, author, null, null),
                rating,
                null,
                reviews,
                coverUrl);

        List<BookListing> listings = new ArrayList<BookListing>();
        listings.add(listing);
        return listings;
    }
}
