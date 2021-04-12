package com.example.byitscover;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.byitscover.helpers.AggregateScraper;
import com.example.byitscover.helpers.AsynchronousOperation;
import com.example.byitscover.helpers.BookListing;
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

/**
 * Fragment which is used to search for reviews by text input
 *
 * @author ???
 * @version 1.1
 */
public class InfoPage extends Fragment {
    private EditText eTitle;
    private EditText eAuthor;
    private AsynchronousOperation<List<BookListing>> scraperOperation;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.enter_book_info, container, false);
        eTitle = view.findViewById(R.id.enter_title); //text field to enter title
        eAuthor = view.findViewById(R.id.enter_author); //text field to enter author
        return view;
    }

    // Display scraper results once scraper completes task
    private void onScraperCompletion() {
        if (scraperOperation.isCancelled())
            return;

        try {
            List<BookListing> listings;
            listings = scraperOperation.get();

            Intent showReviewPage = new Intent(getActivity(), ReviewListPage.class);
            showReviewPage.putExtra(ReviewListPage.KEY_BOOK_LISTINGS, (Serializable)listings);
            startActivity(showReviewPage);
        } catch (ExecutionException ex) {
            ex.printStackTrace();
            throw (RuntimeException) ex.getCause();
        } catch (CancellationException ex) {
            throw new AssertionError("onScraperComplete should never be called if the operation is cancelled");
        } catch (InterruptedException ex) {
            throw new AssertionError("The current thread should never be interrupted while getting the result from the scraper");
        }

        scraperOperation = null;
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        view.findViewById(R.id.Search).setOnClickListener(view1 -> {
            // Cancel previous operation if user presses button multiple times
            if (scraperOperation != null) {
                scraperOperation.cancel();

                // If operation has already completed, then display the previous results
                if (!scraperOperation.isCancelled()) {
                    return;
                }
            }

            String title = eTitle.getText().toString();
            String author = eAuthor.getText().toString();

            Query query = new Query(title, author, null);

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
                    InfoPage.this::onScraperCompletion);
        });
    }

    // Clean up resources created by fragment
    @Override
    public void onDestroy() {
        super.onDestroy();

        // avoid leaking threads
        if (scraperOperation != null) {
            scraperOperation.cancel();
        }
    }
}