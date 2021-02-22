package com.example.byitscover.helpers;

import com.example.byitscover.helpers.Isbn;

/**
 * This class is represents a book.
 * If a field is null, then the corresponding property of the book is unknown
 *
 * @author Marc
 * @author Jack
 * @version 1.1
 */
public class Book {
    private final String title;
    private final String author;
    private final String publisher;
    private final Isbn isbn;

    public Book(String title, String author, String publisher, Isbn isbn) {
        this.title = title;
        this.author = author;
        this.publisher = publisher;
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
     * Getter for publisher
     * @return publisher
     */
    public String getPublisher() {
        return publisher;
    }

    /**
     * Getter for ISBN
     * @return ISBN
     */
    public Isbn getIsbn() {
        return isbn;
    }
}
