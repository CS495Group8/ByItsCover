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
import org.jsoup.nodes.TextNode;
import org.jsoup.safety.Whitelist;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

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
     * from the query and then forms the url that needs to be scraped. Once the title and author
     * are searched for, the scraper then gets the link to the top search result and then connects
     * to that link to get the rating and review information.
     *
     * @return the information from the website in the form of BookListings
     * @throws IOException if the text is not found to be returned from the site
     */
    public List<BookListing> scrape(Query query) throws IOException {
        String url = getUrlWithQuery(query);
        System.out.println(url);
        Document document = Jsoup.connect(url).get();
        Elements bookLinks = document.getElementsByClass("bookTitle");

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
    private BookListing getListingFromElement(Elements bookLinks, int i, Query query) throws MalformedURLException {
        Document document = new Document(null);
        String url = "";
        try {
            url = "https://www.goodreads.com/" + bookLinks.get(i).attr("href");
            System.out.println(url);
            document = Jsoup.connect(url).get();
        } catch (Exception e) {
            System.out.println("Book number " + (i+1) + " is not available");
        }
        //get rating value
        Element ratingElement = document.selectFirst("[itemprop=ratingValue]");
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
            Element reviewElement = document.selectFirst("div#description").selectFirst("span");
            reviewString = Jsoup.clean(reviewElement.childNode(0).toString(), Whitelist.none());
        } catch (Exception ex) {
            reviewString = "";
        }

        Review review = new Review(null, reviewString, null);

        //get author of book from Goodreads
        try {
            Element authorElement = document.selectFirst("div#bookAuthors");
            author = authorElement.childNode(3).childNode(1).childNode(1).childNode(0).childNode(0).toString();
        } catch (Exception ex) {
            author = "";
        }

        //get title of book from Goodreads
        try {
            Element titleElement = document.selectFirst("h1#bookTitle");
            title = titleElement.childNode(0).toString();
        } catch (Exception ex) {
            title = "";
        }

        //get picture url
        try {
            Element coverUrlElement = (Element) document.selectFirst("div.bookCoverPrimary")
                    .childNode(1).childNode(0);
            coverUrl = new URL(coverUrlElement.absUrl("src"));
        } catch (Exception ex) {

        }

        List<Review> reviews = new ArrayList<Review>();
        reviews.add(review);

        BookListing listing = new BookListing(new URL(url),
                ScraperConstants.GOODREADS,
                new Book(title, author, null, null),
                rating,
                null,
                reviews,
                coverUrl,
                getPrice(document));

        return listing;
    }

    /**
     * This method forms the url based on the base Url that is hardcoded and the title and
     * author values that are inputted and a part of the query
     * @param query
     * @return the url as a String
     */
    String getUrlWithQuery(Query query) {
        String url = "https://www.goodreads.com/search?q="
                + query.getQuery().replaceAll(" ", "+");
        return url;
    }

    /**
     * This method returns the price found on the website
     * @return price
     */
    private BigDecimal getPrice(Document document) {
        String priceString;
        try {
            Element priceValue = document.getElementById("buyButtonContainer");
            priceString = priceValue.childNode(3).childNode(0).childNode(0).childNode(0).toString();
            priceString = priceString.substring(priceString.lastIndexOf('$') + 1)
                    .replaceAll("\\s+", "");
            if (!priceString.contains(".")) {
                priceString = priceString.substring(0, priceString.length() - 2)
                        + "." + priceString.substring(priceString.length() - 2);
            }
            return new BigDecimal(priceString);
        } catch (Exception ex) {
        }
        return null;
    }
}