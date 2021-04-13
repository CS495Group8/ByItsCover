package com.example.byitscover.review_list_page;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.byitscover.R;
import com.example.byitscover.helpers.BookListing;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;

/**
 * This activity is used to display a list of search results.
 * It may be launched with an Intent in which the the key `ReviewListPage.KEY_BOOK_LISTINGS`
 * is a serialized value of type `List<BookListing>`.
 *
 * A list of search results is displayed, and clicking a search result opens it in the user's browser.
 *
 * @author Jack
 * @version 1.0
 */

public class ReviewListPage extends AppCompatActivity {
    public static final String KEY_BOOK_LISTINGS = "BOOK_LISTINGS";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        List<BookListing> books;

        try {
            books = readBooks();
        } catch (NullPointerException | ClassCastException ex) {
            throw new IllegalArgumentException(
                    "ReviewListPage requires a non-null List<BookListing> to be passed with intent key" + KEY_BOOK_LISTINGS);
        }

        setContentView(R.layout.activity_review_list_page);
        RecyclerView recyclerView = findViewById(R.id.rvReviewList);
        // Display the list of results as a vertically scrolling list
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new ReviewListAdapter(this, books));
    }

    private List<BookListing> readBooks() {
        Intent intent = getIntent();
        return (List<BookListing>)intent.getSerializableExtra(KEY_BOOK_LISTINGS);
    }
}

// This class is used to communicate which search results are at which list position to the RecyclerView
class ReviewListAdapter extends RecyclerView.Adapter<ReviewListAdapter.ViewHolder> {
    // This class is used to hold each view corresponding to a search result
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final View view;

        public ViewHolder(View view) {
            super(view);
            this.view = view;
        }

        public View getView() {
            return view;
        }
    }

    private final Activity activity;
    private final List<BookListing> listings;

    public ReviewListAdapter(Activity activity, List<BookListing> listings) {
        this.activity = activity;
        this.listings = listings;
    }

    @NotNull
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

        // TODO: Make string constants resources
        titleView.setText("Title: " + (title != null ? title : ""));
        authorView.setText("Author: " + (author != null ? author : ""));
        priceView.setText("Price: " + (price != null ? "$" + price.toString() : "Pricing Information Not Available"));
        ratingView.setText("Rating: " + (aggregateRating != null ? aggregateRating.toString() : "")
            + " " + (numRatings != null ? "(" + numRatings.toString() + " ratings)" : ""));
        websiteView.setText("Website: " + (website != null ? website  : ""));

        if (cover != null) {
            Picasso.get().load(listing.getCoverUrl().toString()).into(coverView);
        } else {
            coverView.setImageResource(R.drawable.unknown_book);
        }

        view.setOnClickListener(v -> {
            try {
                Intent browserLaunch = new Intent(Intent.ACTION_VIEW);
                browserLaunch.setData(Uri.parse(listing.getUrl().toURI().toString()));
                activity.startActivity(browserLaunch);
            } catch (URISyntaxException ex) {
                // TODO: notify user in event of failure
                ex.printStackTrace();
            }
        });
    }

    @Override
    public int getItemCount() {
        return listings.size();
    }
}
