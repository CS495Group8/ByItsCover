package com.example.byitscover.helpers;

import java.util.Map;

/**
 * This class is designed as a singleton to hold all of the info globally across the classes for
 * the current book. Title is the title, Author is the author, both of which come from the ML output
 * from the camera, or the search boxes when searching via text over image. The Map contains the data
 * scraped from the various websites.
 *
 * @author Marc
 * @version 1.0
 * @see <a href="https://www.geeksforgeeks.org/singleton-class-java/#:~:text=In%20object%2Doriented%20programming%2C%20a,to%20the%20first%20instance%20created.&text=To%20design%20a%20singleton%20class,Make%20constructor%20as%20private.">Info On Singleton</a>
 */
public class CurrentBook {
    private String title;
    private String author;
    private String bookCoverUrl;
    private Map<String, String> reviewRatingValues;

    private static CurrentBook instance = new CurrentBook();

    /**
     * Private constructor. This way so the only instance it the one above that is a class variable
     */
    private CurrentBook() {
        //private to prevent anyone else from instantiating
    }

    /**
     * How to get the current instance in the other classes
     *
     * @return The single instance
     */
    public static CurrentBook getInstance() {
        return instance;
    }

    /**
     * Getter for the title
     * @return title
     */
    public String getTitle() {
        return title;
    }

    /**
     * Getter for author
     * @return author
     */
    public String getAuthor() {
        return author;
    }

    /**
     * Getter for map of scraped information
     * @return ratings and reviews from websites
     */
    public Map<String, String> getReviewRatingValues() {
        return reviewRatingValues;
    }

    /**
     * Getter for the url where the book cover is hosted
     * @return the url
     */
    public String getBookCoverUrl() { return bookCoverUrl; }

    /**
     * Setter for the title
     * @param titleIn is the title you want to set it to
     */
    public void setTitle(String titleIn) {
        title = titleIn;
    }

    /**
     * Setter for the author
     * @param authorIn is the author you want to set it to
     */
    public void setAuthor(String authorIn) {
        author = authorIn;
    }

    /**
     * Setter for the map
     * @param reviewValuesIn is what you want to update the map to
     */
    public void setReviewRatingValues(Map<String, String> reviewValuesIn) {
        reviewRatingValues = reviewValuesIn;
    }

    /**
     * Setter for book cover url. Comes from goodreads.com
     * @param url website url where image is hosted
     */
    public void setBookCoverUrl(String url) {
        bookCoverUrl = url;
    }
}
