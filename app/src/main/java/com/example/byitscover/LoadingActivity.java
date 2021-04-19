package com.example.byitscover;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ProgressBar;

import com.example.byitscover.helpers.AggregateScraper;
import com.example.byitscover.helpers.AsynchronousOperation;
import com.example.byitscover.helpers.BookListing;
import com.example.byitscover.helpers.ImageAnalyzer;
import com.example.byitscover.helpers.Query;
import com.example.byitscover.helpers.Scraper;
import com.example.byitscover.review_list_page.ReviewListPage;
import com.example.byitscover.scrapers.BarnesAndNobleScraper;
import com.example.byitscover.scrapers.GoodreadsScraper;
import com.example.byitscover.scrapers.GoogleScraper;
import com.example.byitscover.scrapers.StorygraphScraper;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;

public class LoadingActivity extends AppCompatActivity {
    private AsynchronousOperation<List<BookListing>> scraperOperation;
    private AsynchronousOperation<Query> imageAnalysisOperation;

    private void onScraperCompletion() {
        if (scraperOperation.isCancelled())
            return;

        List<BookListing> listings;

        try {
            listings = scraperOperation.get();
        } catch (ExecutionException ex) {
            ex.printStackTrace();
            throw (RuntimeException)ex.getCause();
        } catch (CancellationException ex) {
            throw new AssertionError("onScraperCompletion should never be called if the operation is cancelled");
        } catch (InterruptedException ex) {
            throw new AssertionError("The current thread should never be interrupted while getting the result from the scraper");
        }

        Intent showReviewPage = new Intent(this, ReviewListPage.class);
        showReviewPage.putExtra(ReviewListPage.KEY_BOOK_LISTINGS, (Serializable)listings);
        finish();
        startActivity(showReviewPage);
    }

    private void onImageAnalysisCompletion() {
        Query query;

        try {
            query = imageAnalysisOperation.get();
        } catch (ExecutionException ex) {
            ex.printStackTrace();
            throw (RuntimeException)ex.getCause();
        } catch (CancellationException ex) {
            throw new AssertionError("onImageAnalysisCompletion should never be called if the operation is cancelled");
        } catch (InterruptedException ex) {
            throw new AssertionError("The current thread should never be interrupted while getting the result from the image analysis");
        }

        sendQuery(query);
    }

    private void sendQuery(Query query) {
        scraperOperation = new AsynchronousOperation<>(
                () -> {
                    List<Scraper> scrapers = new ArrayList<>();

                    scrapers.add(new BarnesAndNobleScraper());
                    scrapers.add(new GoodreadsScraper());
                    scrapers.add(new GoogleScraper());
                    scrapers.add(new StorygraphScraper());
                    Scraper aggregate = new AggregateScraper(scrapers);

                    return aggregate.scrape(query);
                },
                this::onScraperCompletion);
    }

    private void analyzeImage(String path) {
        imageAnalysisOperation = new AsynchronousOperation<>(
                () -> {
                    return new ImageAnalyzer().analyze(path);
                },
                this::onImageAnalysisCompletion);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);

        ProgressBar spinner = (ProgressBar)findViewById(R.id.progressBar);

        Intent prevIntent = getIntent();

        if (prevIntent.hasExtra("book_title") && prevIntent.hasExtra("book_author")) {
            String title = prevIntent.getStringExtra("book_title");
            String author = prevIntent.getStringExtra("book_author");

            title = title == null ? "" : title;
            author = author == null ? "" : author;
            final Query query = new Query(title, author, null);
            sendQuery(query);
        }

        else {
            analyzeImage(prevIntent.getStringExtra("image_path"));
        }
    }

    // Clean up resources created by activity
    @Override
    public void onDestroy() {
        super.onDestroy();

        // avoid leaking threads
        if (scraperOperation != null) {
            scraperOperation.cancel();
        }
    }
}