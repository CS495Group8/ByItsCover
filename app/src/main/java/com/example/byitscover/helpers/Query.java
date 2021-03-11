package com.example.byitscover.helpers;

/**
 * This class is used to represent queries from the image processor to scrapers
 * Null elements indicate that the scraper cannot determine what the element should be
 *
 * Note: this class will likely change as we determine what information we can extract from images
 * For example, instead of fitting data to a schema as in this first version,
 * we may decide to only treat text from images as keywords for search queries.
 *
 * @author Jack
 * @version 1.1
 */

public class Query {
    private final String title;
    private final String author;
    private final Isbn isbn;

    public Query(String title, String author, Isbn isbn) {
        this.title = title;
        this.author = author;
        this.isbn = isbn;
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
     * Getter for ISBN
     * @return ISBN
     */
    public Isbn getIsbn() {
        return isbn;
    }

    public Query clone() {
        return new Query(title, author, isbn);
    }
}
