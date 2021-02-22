package com.example.byitscover.helpers;

import org.junit.Test;

import static org.junit.Assert.*;

public class CurrentBookTest {
    @Test
    public void testSetAndGetAuthor() {
        CurrentBook instance = CurrentBook.getInstance();
        instance.setAuthor(ScraperConstants.TEMP_HARDCODED_AUTHOR);
        assertEquals(ScraperConstants.TEMP_HARDCODED_AUTHOR, instance.getAuthor());
    }

    @Test
    public void testSetAndGetTitle() {
        CurrentBook instance = CurrentBook.getInstance();
        instance.setTitle(ScraperConstants.TEMP_HARDCODED_TITLE);
        assertEquals(ScraperConstants.TEMP_HARDCODED_TITLE, instance.getTitle());
    }

}
