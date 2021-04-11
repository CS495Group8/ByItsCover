package com.example.byitscover;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.byitscover.helpers.Book;
import com.example.byitscover.helpers.BookListing;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class ReviewListPage extends AppCompatActivity {
    private List<BookListing> books;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_list_page);
        books = loadBooks();
    }

    private List<BookListing> loadBooks() {
        // TODO: replace with parameters from intent
        List<BookListing> listings = new ArrayList<BookListing>();
        listings.add(new BookListing(null,
                "something.com",
                new Book("A Modest Proposal", "Jonathan Swift", "Someone", null),
                null,
                null,
                null,
                null,
                BigDecimal.TEN));

        listings.add(new BookListing(null,
                "b.com",
                new Book("Generic Book", "Generic Author", "Someone", null),
                null,
                null,
                null,
                null,
                BigDecimal.ONE));

        for (int i = 0; i < 10; i++) {
            listings.add(listings.get(i % 2));
        }

        return listings;
    }
}