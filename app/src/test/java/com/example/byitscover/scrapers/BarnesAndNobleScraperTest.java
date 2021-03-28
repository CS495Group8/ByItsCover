package com.example.byitscover.scrapers;

import com.example.byitscover.helpers.BookListing;
import com.example.byitscover.helpers.Query;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.customsearch.Customsearch;
import com.google.api.services.customsearch.model.Result;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class BarnesAndNobleScraperTest {
    private BarnesAndNobleScraper barnesAndNobleScraper = new BarnesAndNobleScraper();
    private static String title = "The Sellout";
    private static String author = "Paul Beatty";
    private static Double rating = 3.8;
    private static String review = "Winner of the Man Booker PrizeWinner of the National Book " +
            "Critics Circle Award in FictionWinner of the John Dos Passos Prize for LiteratureNew " +
            "York Times BestsellerLos Angeles Times Bestseller Named One of the 10 Best Books of " +
            "the Year by The New York Times Book ReviewNamed a Best Book of the Year by Newsweek, " +
            "The Denver Post, BuzzFeed, Kirkus Reviews, and Publishers Weekly";

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
        List<BookListing> results = barnesAndNobleScraper.scrape(testQuery);

        assertTrue(results.get(0).getBook().getTitle().contains(title));
        assertEquals(author, results.get(0).getBook().getAuthor());
        assertEquals(rating, results.get(0).getAggregateRating());
        assertEquals(review, results.get(0).getReviews().get(0).getComment());
    }

    @Test
    public void testGetActualBookResult() throws IOException {
        List<Result> results = new ArrayList<Result>();
        Result r1 = new Result();
        Result r2 = new Result();
        Result r3 = new Result();

        r1.setFormattedUrl("https://www.barnesandnoble.com/b/4-hour-workweek-expanded-and-updated-timothy-ferriss/1100290322");
        r2.setFormattedUrl("https://www.barnesandnoble.com/v/4-hour-workweek-expanded-and-updated-timothy-ferriss/1100290322");
        r3.setFormattedUrl("https://www.barnesandnoble.com/w/4-hour-workweek-expanded-and-updated-timothy-ferriss/1100290322");

        results.add(r1);
        results.add(r2);
        results.add(r3);

        String returned = barnesAndNobleScraper.getActualBookResult(results);

        assertEquals("https://www.barnesandnoble.com/w/4-hour-workweek-expanded-and-updated-timothy-ferriss/1100290322", returned);
    }
}
