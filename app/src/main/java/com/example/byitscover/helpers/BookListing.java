package com.example.byitscover.helpers;

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

    public BookListing(URL url, String website, Book book, Double aggregateRating, Integer ratingCount, List<Review> reviews) {
        if (url == null || website == null || book == null)
            throw new IllegalArgumentException("BookListing requires non-null URL, website, and Book for initialization");

        this.url = url;
        this.website = website;
        this.book = book;
        this.aggregateRating = aggregateRating;
        this.ratingCount = ratingCount;
        this.reviews = reviews;
    }

    public BookListing clone() {
        return new BookListing(url, website, book, aggregateRating, ratingCount, reviews);
    }
}
