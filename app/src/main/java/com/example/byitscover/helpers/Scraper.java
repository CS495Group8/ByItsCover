package com.example.byitscover.helpers;
import java.util.List;

// The only constraint imposed upon scrapers is that

public interface Scraper {
    List<BookListing> scrape(Query query);
}
