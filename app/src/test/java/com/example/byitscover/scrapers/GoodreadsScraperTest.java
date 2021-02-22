package com.example.byitscover.scrapers;

import com.example.byitscover.helpers.Book;
import com.example.byitscover.helpers.ScraperConstants;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.Map;

import static org.junit.Assert.*;

public class GoodreadsScraperTest {

    private static final String REVIEW_TEXT = "From the award-winning author of Station Eleven, an exhilarating novel set at the glittering intersection of two seemingly disparate events–a massive Ponzi scheme collapse and the mysterious disappearance of a woman from a ship at sea.";
    private static final Double RATING_VALUE = 3.71;

    @Before
    public void setUp() {
        Book instance = Book.getInstance();
        instance.setTitle(null);
        instance.setAuthor(null);
    }

    @Test
    public void testGetInfo() throws IOException {
        Book insance = Book.getInstance();
        insance.setAuthor(ScraperConstants.TEMP_HARDCODED_AUTHOR);
        insance.setTitle(ScraperConstants.TEMP_HARDCODED_TITLE);
        Map<String, String> results = GoodreadsScraper.getInfo();

        assertEquals(REVIEW_TEXT, results.get(ScraperConstants.GOODREADS_REVIEW_KEY));
        assertEquals(RATING_VALUE, Double.valueOf(results.get(ScraperConstants.GOODREADS_RATING_KEY)));
    }
}
