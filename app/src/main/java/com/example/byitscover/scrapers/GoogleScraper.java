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
import org.jsoup.safety.Whitelist;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The Class scrapes the Google books website to get the rating and review from their website
 *
 * @version 1.0
 * @author Marc
 */
public class GoogleScraper implements Scraper {

    //Empty constructor needed
    public GoogleScraper() {

    }

    /**
     * Standard method to get the rating and review from the website. This rating one was annoying
     * as the Google Books site does not present the rating in a decimal form, it just shows the
     * stars and a histogram of the number of ratings for each star. So had to get the values
     * for each row of the histogram and calculate the rating manually.
     *
     * @param query Object that holds information about the book being searched for
     * @return Information from the Google Books website in BookListing form
     * @throws IOException
     */
    public List<BookListing> scrape(Query query) throws IOException {
        List<Result> results = ScraperHelper.googleAPISearch(ScraperConstants.GOOGLE_BOOKS, query);
        String searchingUrl = ScraperHelper.getTopResultUrl(results);

        System.out.println("Google- " + searchingUrl);

        Document bookDocument;
        try {
            bookDocument = Jsoup.connect(searchingUrl).get();
        } catch (Exception e) {
            bookDocument = Jsoup.connect("https://google.com").get();
        }

        Double rating;
        String reviewValueString;

        try {
            //find rating info and calculate
            Element rating5stars = (Element) bookDocument.getElementById("histogram-container").childNode(1).childNode(0)
                    .childNode(0).childNode(1).childNode(0).childNode(0).childNode(0).childNode(1);
            Element rating4stars = (Element) bookDocument.getElementById("histogram-container").childNode(1).childNode(0)
                    .childNode(1).childNode(1).childNode(0).childNode(0).childNode(0).childNode(1);
            Element rating3stars = (Element) bookDocument.getElementById("histogram-container").childNode(1).childNode(0)
                    .childNode(2).childNode(1).childNode(0).childNode(0).childNode(0).childNode(1);
            Element rating2stars = (Element) bookDocument.getElementById("histogram-container").childNode(1).childNode(0)
                    .childNode(3).childNode(1).childNode(0).childNode(0).childNode(0).childNode(1);
            Element rating1stars = (Element) bookDocument.getElementById("histogram-container").childNode(1).childNode(0)
                    .childNode(4).childNode(1).childNode(0).childNode(0).childNode(0).childNode(1);

            Double fiveStars = Double.valueOf(Jsoup.clean(rating5stars.toString(), Whitelist.none()));
            Double fourStars = Double.valueOf(Jsoup.clean(rating4stars.toString(), Whitelist.none()));
            Double threeStars = Double.valueOf(Jsoup.clean(rating3stars.toString(), Whitelist.none()));
            Double twoStars = Double.valueOf(Jsoup.clean(rating2stars.toString(), Whitelist.none()));
            Double oneStars = Double.valueOf(Jsoup.clean(rating1stars.toString(), Whitelist.none()));

            //calculate rating
            rating = (((fiveStars * 5) + (fourStars * 4) + (threeStars * 3) + (twoStars * 2) +
                    (oneStars * 1)) / (fiveStars + fourStars + threeStars + twoStars + oneStars));
        }
        catch (Exception e) {
            rating = 0.0;
        }

        DecimalFormat df = new DecimalFormat("#.##");

        //get review value
        try {
            //get review
            Element reviewValue = (Element) bookDocument.getElementById("synopsistext").childNode(0);
            reviewValueString = Jsoup.clean(reviewValue.toString(), Whitelist.none());
        } catch (Exception ex) {
            reviewValueString = "";
        }

        Review review = new Review(null, reviewValueString, null);
        List<Review> reviews = new ArrayList<Review>();
        reviews.add(review);

        BookListing listing = new BookListing(new URL(searchingUrl),
                ScraperConstants.GOOGLE_BOOKS,
                new Book(query.getTitle(), query.getAuthor(), null, null),
                Double.valueOf(df.format(rating)),
                null,
                reviews,
                null,
                getPrice(bookDocument));

        List<BookListing> listings = new ArrayList<BookListing>();
        listings.add(listing);
        return listings;
    }

    /**
     * This method returns the price found on the website
     * @return price
     */
    private BigDecimal getPrice(Document bookDocument) {
        String priceString;
        try {
            Element priceElement = bookDocument.getElementById("gb-get-book-container");
            priceString = priceElement.childNode(0).childNode(0).toString();
            priceString = priceString.substring(priceString.lastIndexOf("$") + 1);
            return new BigDecimal(priceString);
            //return BigDecimal.valueOf(Double.valueOf(priceString));
        } catch (Exception e) {
            priceString = null;
        }
        return null;
    }
}
