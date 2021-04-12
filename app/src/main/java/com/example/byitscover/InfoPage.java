package com.example.byitscover;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;


import com.example.byitscover.helpers.AggregateScraper;
import com.example.byitscover.helpers.AsynchronousOperation;
import com.example.byitscover.helpers.Book;
import com.example.byitscover.helpers.BookListing;
import com.example.byitscover.helpers.Query;
import com.example.byitscover.helpers.Scraper;
import com.example.byitscover.helpers.ScraperConstants;
import com.example.byitscover.review_list_page.ReviewListPage;
import com.example.byitscover.scrapers.BarnesAndNobleScraper;
import com.example.byitscover.scrapers.GoodreadsScraper;
import com.example.byitscover.scrapers.GoogleScraper;
import com.example.byitscover.scrapers.StorygraphScraper;

import java.io.Serializable;
import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;

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
        eTitle = (EditText) view.findViewById(R.id.enter_title); //text field to enter title
        eAuthor = (EditText) view.findViewById(R.id.enter_author); //text field to enter author
        return view;
    }

    private void onScraperCompletion() {
        if (scraperOperation.isCancelled())
            return;

        try {
            List<BookListing> listings;
            listings = scraperOperation.get();

            listings = new ArrayList<BookListing>();

            try {
                listings.add(new BookListing(new URL("http://www.google.com/"),
                        "something.com",
                        new Book("A Modest Proposal", "Jonathan Swift", "Someone", null),
                        null,
                        null,
                        null,
                        null,
                        BigDecimal.TEN));

                listings.add(new BookListing(new URL("http://www.google.com/"),
                        "b.com",
                        new Book("Generic Book", "Generic Author", "Someone", null),
                        null,
                        null,
                        null,
                        null,
                        BigDecimal.ONE));
            } catch (MalformedURLException ex) {
                ex.printStackTrace();
            }

            for (int i = 0; i < 10; i++) {
                listings.add(listings.get(i % 2));
            }

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

        view.findViewById(R.id.Search).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Cancel previous operation if user presses button multiple times
                if (scraperOperation != null) {
                    scraperOperation.cancel();

                    // Check if operation completed before we cancelled it
                    if (!scraperOperation.isCancelled()) {
                        return;
                    }
                }

                String title = eTitle.getText().toString();
                String author = eAuthor.getText().toString();

                Query query = new Query(title, author, null);

                scraperOperation = new AsynchronousOperation<List<BookListing>>(
                        new Callable<List<BookListing>>() {
                            @Override
                            public List<BookListing> call() throws Exception {
                                List<Scraper> scrapers = new ArrayList<Scraper>();

                                scrapers.add(new BarnesAndNobleScraper());
                                scrapers.add(new GoodreadsScraper());
                                scrapers.add(new GoogleScraper());
                                scrapers.add(new StorygraphScraper());
                                Scraper aggregate = new AggregateScraper(scrapers);

                                return null;
                            }
                        },
                        InfoPage.this::onScraperCompletion);
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        // avoid leaking threads
        if (scraperOperation != null) {
            scraperOperation.cancel();
        }
    }
}