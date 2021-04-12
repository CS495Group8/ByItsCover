package com.example.byitscover.review_list_page;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.byitscover.R;
import com.example.byitscover.helpers.Book;
import com.example.byitscover.helpers.BookListing;
import com.squareup.picasso.Picasso;

import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class ReviewListPage extends AppCompatActivity {
    private static final String KEY_BOOKS = "BOOKS";

    private List<BookListing> books;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        books = readBooks();
        setContentView(R.layout.activity_review_list_page);
        recyclerView = findViewById(R.id.rvReviewList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new ReviewListAdapter(books));
    }

    private List<BookListing> readBooks() {
        // TODO: replace with parameters from intent
        List<BookListing> listings = new ArrayList<BookListing>();

        try {
            listings.add(new BookListing(new URL("http://www.google.com"),
                    "something.com",
                    new Book("A Modest Proposal", "Jonathan Swift", "Someone", null),
                    null,
                    null,
                    null,
                    null,
                    BigDecimal.TEN));

            listings.add(new BookListing(new URL("http://www.google.com"),
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

        return listings;
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putSerializable(KEY_BOOKS, new ArrayList<BookListing>(books));
        super.onSaveInstanceState(savedInstanceState);
    }
}

class ReviewListAdapter extends RecyclerView.Adapter<ReviewListAdapter.ViewHolder> {
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private View view;

        public ViewHolder(View view) {
            super(view);
            this.view = view;
        }

        public View getView() {
            return view;
        }
    }

    private List<BookListing> listings;

    public ReviewListAdapter(List<BookListing> listings) {
        this.listings = listings;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.layout_book_listing_view, viewGroup, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        BookListing listing = listings.get(position);
        View view = viewHolder.getView();

        TextView titleView = view.findViewById(R.id.title);
        TextView authorView = view.findViewById(R.id.author);
        TextView priceView = view.findViewById(R.id.price);
        TextView ratingView = view.findViewById(R.id.rating);
        TextView websiteView = view.findViewById(R.id.website);
        ImageView coverView = view.findViewById(R.id.cover);

        String title = listing.getBook().getTitle();
        String author = listing.getBook().getAuthor();
        BigDecimal price = listing.getPrice();
        Double aggregateRating = listing.getAggregateRating();
        Integer numRatings = listing.getRatingCount();
        String website = listing.getWebsite();
        URL cover = listing.getCoverUrl();

        titleView.setText("Title: " + (title != null ? title : ""));
        authorView.setText("Author: " + (author != null ? author : ""));
        priceView.setText("Price: " + (price != null ? "$" + price.toString() : ""));
        ratingView.setText("Rating: " + (aggregateRating != null ? aggregateRating.toString() : "")
            + " " + (numRatings != null ? "(" + numRatings.toString() + " ratings)" : ""));
        websiteView.setText("Website: " + (website != null ? website  : ""));

        if (cover != null) {
            Picasso.get().load(listing.getCoverUrl().toString()).into(coverView);
        } else {
            coverView.setImageResource(R.drawable.the_glass_hotel);
        }
    }

    @Override
    public int getItemCount() {
        return listings.size();
    }
}