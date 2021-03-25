package com.example.byitscover.scrapers;

import com.example.byitscover.helpers.BookListing;
import com.example.byitscover.helpers.Query;
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

public class GoogleScraperTest {
    private GoogleScraper googleScraper = new GoogleScraper();
    private static String title = "The Sellout";
    private static String author = "Paul Beatty";
    private static Double rating = 3.67;
    private static String review = "Winner of the Man Booker PrizeWinner of the National Book " +
            "Critics Circle Award in FictionWinner of the John Dos Passos Prize for LiteratureNew " +
            "York Times BestsellerLos Angeles Times Bestseller Named One of the 10 Best Books of " +
            "the Year by The New York Times Book ReviewNamed a Best Book of the Year by Newsweek, " +
            "The Denver Post, BuzzFeed, Kirkus Reviews, and Publishers WeeklyNamed a \"Must-Read\" " +
            "by Flavorwire and New York Magazine's \"Vulture\" BlogA biting satire about a young " +
            "man's isolated upbringing and the race trial that sends him to the Supreme Court, " +
            "Paul Beatty's The Sellout showcases a comic genius at the top of his game. It " +
            "challenges the sacred tenets of the United States Constitution, urban life, the " +
            "civil rights movement, the father-son relationship, and the holy grail of racial " +
            "equality—the black Chinese restaurant.Born in the \"agrarian ghetto\" of Dickens—on " +
            "the southern outskirts of Los Angeles—the narrator of The Sellout resigns himself to " +
            "the fate of lower-middle-class Californians: \"I'd die in the same bedroom I'd grown " +
            "up in, looking up at the cracks in the stucco ceiling that've been there since " +
            "'68 quake.\" Raised by a single father, a controversial sociologist, he spent his " +
            "childhood as the subject in racially charged psychological studies. He is led to " +
            "believe that his father's pioneering work will result in a memoir that will solve " +
            "his family's financial woes. But when his father is killed in a police shoot-out, " +
            "he realizes there never was a memoir. All that's left is the bill for a drive-thru " +
            "funeral.Fueled by this deceit and the general disrepair of his hometown, the narrator " +
            "sets out to right another wrong: Dickens has literally been removed from the map to " +
            "save California from further embarrassment. Enlisting the help of the town's most " +
            "famous resident—the last surviving Little Rascal, Hominy Jenkins—he initiates the " +
            "most outrageous action conceivable: reinstating slavery and segregating the local " +
            "high school, which lands him in the Supreme Court.";

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
        List<BookListing> results = googleScraper.scrape(testQuery);

        assertTrue(results.get(0).getBook().getTitle().contains(title));
        assertEquals(author, results.get(0).getBook().getAuthor());
        assertEquals(rating, results.get(0).getAggregateRating());
        assertEquals(review, results.get(0).getReviews().get(0).getComment());
    }

}
