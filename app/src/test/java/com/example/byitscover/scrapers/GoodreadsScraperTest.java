package com.example.byitscover.scrapers;

import com.example.byitscover.helpers.BookListing;
import com.example.byitscover.helpers.Query;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.customsearch.Customsearch;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class GoodreadsScraperTest {

    private GoodreadsScraper goodreadsScraper = new GoodreadsScraper();
    private static String title = "The Giver";
    private static String author = "Lois Lowry";
    private static Double rating = 4.13;
    private static String review = "The Giver, the 1994 Newbery Medal winner, has become one of the " +
            "most influential novels of our time. The haunting story centers on twelve-year-old " +
            "Jonas, who lives in a seemingly ideal, if colorless, world of conformity and " +
            "contentment. Not until he is given his life assignment as the Receiver of Memory does " +
            "he begin to understand the dark, complex secrets behind his fragi";

    private static String badTitle = "The Giveb";
    private static String badAuthor = "Loas Lawry";

    @Before
    public void setup() throws GeneralSecurityException, IOException {
        HttpTransport httpTransport = new NetHttpTransport();
        JsonFactory jsonFactory = new JacksonFactory();

        Customsearch customsearch = new Customsearch.Builder(httpTransport, jsonFactory, null)
                .setApplicationName("By Its Cover").build();
    }

    @Test
    public void testScrapeTitleAndAuthor() throws IOException {
        Query testQuery = new Query(title, author, null);
        List<BookListing> results = goodreadsScraper.scrape(testQuery);

        assertTrue(results.get(0).getBook().getTitle().contains(title));
        assertEquals(author, results.get(0).getBook().getAuthor());
        assertEquals(rating, results.get(0).getAggregateRating());
        assertEquals(review, results.get(0).getReviews().get(0).getComment());
    }

    @Test
    public void testScrapeTitleOnly() throws IOException {
        Query testQuery = new Query(title, null, null);
        List<BookListing> results = goodreadsScraper.scrape(testQuery);

        assertTrue(results.get(0).getBook().getTitle().contains(title));
        assertEquals(author, results.get(0).getBook().getAuthor());
        assertEquals(rating, results.get(0).getAggregateRating());
        assertEquals(review, results.get(0).getReviews().get(0).getComment());
    }

    @Test
    public void testScrapeSpellingError() throws IOException {
        Query testQuery = new Query(badTitle, badAuthor, null);
        List<BookListing> results = goodreadsScraper.scrape(testQuery);

        assertTrue(results.get(0).getBook().getTitle().contains(title));
        assertEquals(author, results.get(0).getBook().getAuthor());
        assertEquals(rating, results.get(0).getAggregateRating());
        assertEquals(review, results.get(0).getReviews().get(0).getComment());
    }

    @Test
    public void testGetUrlWithQuery() {
        Query testQuery = new Query(title, author, null);
        assertEquals("https://www.goodreads.com/search?q=The+Giver+Lois+Lowry",
                goodreadsScraper.getUrlWithQuery(testQuery));
    }

}
