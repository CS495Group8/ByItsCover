package com.example.byitscover.scrapers;

import com.example.byitscover.helpers.BookListing;
import com.example.byitscover.helpers.Query;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.customsearch.Customsearch;
import com.google.api.services.customsearch.model.Result;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.math.BigDecimal;
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
    private static Document priceDoc = Jsoup.parse("<span id=\"pdp-cur-price\" class=\"price current-price ml-0\">" +
            "<sup>$</sup>15.50</span> <s class=\"old-price\">$17.00</s> " +
            "<span class=\"saved-percent discount-amount\">Save 9%</span> " +
            "<span id=\"adaLabel\" class=\"sr-only\">Current price is $15.5, " +
            "Original price is $17. You Save 9%.</span> ");

    @Before
    public void setup() throws GeneralSecurityException, IOException {
        HttpTransport httpTransport = new NetHttpTransport();
        JsonFactory jsonFactory = new JacksonFactory();

        Customsearch customsearch = new Customsearch.Builder(httpTransport, jsonFactory, null)
                .setApplicationName("By Its Cover").build();
    }

    @Test
    public void testScrapeTitleAndAuthorBook1() throws IOException {
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

    @Test
    public void testGetPrice() {
        assertEquals(new BigDecimal("15.50"), barnesAndNobleScraper.getPrice(priceDoc));
    }

    @Test
    public void testGetUrlWithQuery() {
        Query testQuery = new Query(title, author, null);
        assertEquals("https://www.barnesandnoble.com/s/The%20Sellout%20Paul%20Beatty",
                barnesAndNobleScraper.getUrlWithQuery(testQuery));
    }
}
