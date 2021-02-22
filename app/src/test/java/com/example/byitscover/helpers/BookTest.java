package com.example.byitscover.helpers;

import org.junit.Test;

import static org.junit.Assert.*;

public class BookTest {
    @Test
    public void testGetAuthor() {
        Book book = new Book(null, ScraperConstants.TEMP_HARDCODED_AUTHOR, null, null);
        assertEquals(ScraperConstants.TEMP_HARDCODED_AUTHOR, book.getAuthor());
    }

    @Test
    public void testGetTitle() {
        Book book = new Book(ScraperConstants.TEMP_HARDCODED_TITLE, null, null, null);
        assertEquals(ScraperConstants.TEMP_HARDCODED_TITLE, book.getTitle());
    }

}
