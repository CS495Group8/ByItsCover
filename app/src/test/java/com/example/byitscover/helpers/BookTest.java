package com.example.byitscover.helpers;

import org.junit.Test;

import static org.junit.Assert.*;

public class BookTest {
    @Test
    public void testSetAndGetAuthor() {
        Book instance = Book.getInstance();
        instance.setAuthor(ScraperConstants.TEMP_HARDCODED_AUTHOR);
        assertEquals(ScraperConstants.TEMP_HARDCODED_AUTHOR, instance.getAuthor());
    }

    @Test
    public void testSetAndGetTitle() {
        Book instance = Book.getInstance();
        instance.setTitle(ScraperConstants.TEMP_HARDCODED_TITLE);
        assertEquals(ScraperConstants.TEMP_HARDCODED_TITLE, instance.getTitle());
    }

}
