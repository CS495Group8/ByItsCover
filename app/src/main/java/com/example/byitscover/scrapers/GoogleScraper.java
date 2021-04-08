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
import org.jsoup.safety.Whitelist;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.MalformedURLException;
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
        String searchingUrl = ScraperHelper.getGoogleUrlNoAPI(ScraperConstants.GOOGLE_BOOKS, query);
        searchingUrl.replaceAll("[\\n]", "");
        Document document = Jsoup.connect(searchingUrl).get();

        Elements links = (Elements) document.select("div.g");

        List<BookListing> listings = new ArrayList<BookListing>();
        BookListing firstListing = getListingFromElement(links, 0, query);
        listings.add(firstListing);

        if (links.size() > 1) {
            BookListing secondListing = getListingFromElement(links, 1, query);
            listings.add(secondListing);
        }
        if (links.size() > 2) {
            BookListing thirdListing = getListingFromElement(links, 2, query);
            listings.add(thirdListing);
        }

        return listings;
    }

    /**
     * This method grabs the information for the specified book and returns a BookListing object
     * based on the information found on the specific website
     *
     * @param links this is a list of all of the books found on the website search
     * @param i specifies which listing you want to get the information of
     * @param query
     * @return Book listing based off of the information found on the website
     * @throws MalformedURLException Raised if the new URL() call produces an error due to bad input
     */
    private BookListing getListingFromElement(Elements links, int i, Query query) throws MalformedURLException {
        Element linkElement;
        if (i == 0) {
            linkElement = (Element) links.get(i).childNode(1).childNode(0).childNode(0);
        }
        else {
            linkElement = (Element) links.get(i).childNode(0).childNode(0).childNode(0);
        }

        String bookUrl = linkElement.attr("abs:href");
        System.out.println(bookUrl);
        Document bookDocument = new Document(null);
        try {
            bookDocument = Jsoup.connect(bookUrl).get();
        } catch (Exception e) {
            System.out.println("Book number " + (i+1) + " is not available");
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

        String title = "";
        String author = "";
        //get title value
        try {
            //get title
            Element titleElement = (Element) bookDocument.getElementById("bookinfo")
                    .childNode(0).childNode(0).childNode(0);
            title = Jsoup.clean(titleElement.toString(), Whitelist.none());
        } catch (Exception ex) {
            title = "";
        }

        //get author value
        try {
            //get author
            Element authorElement = (Element) bookDocument.getElementById("bookinfo")
                    .childNode(2).childNode(0).childNode(0).childNode(0);
            author = Jsoup.clean(authorElement.toString(), Whitelist.none());
        } catch (Exception ex) {
            author = "";
        }

        BookListing listing = new BookListing(new URL(bookUrl),
                ScraperConstants.GOOGLE_BOOKS,
                new Book(title, author, null, null),
                Double.valueOf(df.format(rating)),
                null,
                reviews,
                null,
                getPrice(bookDocument));

        return listing;
    }

    /**
     * This method returns the price found on the website
     * @return price
     */
    BigDecimal getPrice(Document bookDocument) {
        String priceString;
        try {
            Element priceElement = bookDocument.getElementById("gb-get-book-container");
            priceString = priceElement.childNode(0).childNode(0).toString();
            priceString = priceString.substring(priceString.lastIndexOf("$") + 1);
            return new BigDecimal(priceString);
        } catch (Exception e) {
            priceString = null;
        }
        return null;
    }
}
