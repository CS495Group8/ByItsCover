package com.example.byitscover.helpers;

import java.math.BigDecimal;
import java.net.URL;
import java.util.List;

/**
 * This class represents a listing of a book on a website.
 * It includes information specific to a book listing such as reviews, the website, and aggregate ratings.
 * The scraper should return one of these for each relevant listing it finds in a search.
 *
 * @author Jack
 * @version 1.0
 */

public class BookListing {
    private final URL url;
    private final String website; // TODO: find something to use a substitute for enum instead of strings
    private final Book book;
    private final Double aggregateRating;
    private final Integer ratingCount;
    private final List<Review> reviews;
    private final URL coverUrl;
    private final BigDecimal price;

    public BookListing(URL url, String website, Book book, Double aggregateRating,
                       Integer ratingCount, List<Review> reviews, URL coverUrl, BigDecimal price) {
        if (url == null || website == null || book == null)
            throw new IllegalArgumentException("BookListing requires non-null URL, website, and Book for initialization");

        this.url = url;
        this.website = website;
        this.book = book;
        this.aggregateRating = aggregateRating;
        this.ratingCount = ratingCount;
        this.reviews = reviews;
        this.coverUrl = coverUrl;
        this.price = price;
    }

    /**
     * Getter for the url
     * @return url
     */
    public URL getUrl() {
        return url;
    }

    /**
     * Getter for website
     * @return website
     */
    public String getWebsite() {
        return website;
    }

    /**
     * Getter for book
     * @return book
     */
    public Book getBook() {
        return book;
    }

    /**
     * Getter for aggregate rating
     * @return aggregate rating
     */
    public Double getAggregateRating() {
        return aggregateRating;
    }

    /**
     * Getter for rating count
     * @return rating count
     */
    public Integer getRatingCount() {
        return ratingCount;
    }

    /**
     * Getter for reviews
     * @return reviews
     */
    public List<Review> getReviews() {
        return reviews;
    }

    /**
     * Getter for cover URL
     * @return cover URL
     */
    public URL getCoverUrl() {
        return coverUrl;
    }

    /**
     * Getter for price
     * @return price
     */
    public BigDecimal getPrice() { return price; }

    public BookListing clone() {
        return new BookListing(url, website, book, aggregateRating, ratingCount, reviews, coverUrl, price);
    }
}
