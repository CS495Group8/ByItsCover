package com.example.byitscover.helpers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class AggregateScraper implements Scraper {
    private static final ExecutorService executor = Executors.newCachedThreadPool(Executors.defaultThreadFactory());
    List<Scraper> scrapers;

    public AggregateScraper(List<Scraper> scrapers) {
        this.scrapers = scrapers;
    }

    public List<BookListing> scrape(Query query) throws IOException {
        List<Future<List<BookListing>>> futures = new ArrayList<Future<List<BookListing>>>();

        for (Scraper scraper : scrapers) {
            futures.add(executor.submit(new Callable<List<BookListing>>() {
                @Override
                public List<BookListing> call() throws Exception {
                    return scraper.scrape(query);
                }
            }));
        }

        List<BookListing> aggregate = new ArrayList<BookListing>();

        for (Future<List<BookListing>> future : futures) {
            try {
                aggregate.addAll(future.get());
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        return aggregate;
    }
}
