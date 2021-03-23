package com.example.byitscover;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.byitscover.helpers.AsynchronousOperation;
import com.example.byitscover.helpers.Book;
import com.example.byitscover.helpers.BookListing;
import com.example.byitscover.helpers.Isbn;
import com.example.byitscover.helpers.Query;
import com.example.byitscover.helpers.Scraper;
import com.example.byitscover.helpers.ScraperConstants;
import com.example.byitscover.scrapers.BarnesAndNobleScraper;
import com.example.byitscover.scrapers.GoodreadsScraper;
import com.squareup.picasso.Picasso;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;


/**
 * This class is the logic behind the page that shows all the review and rating information to
 * the user. An accompanying .xml can be found in src/res/layout which holds all the front end, UI
 * information. The website icons are automatically loaded.
 *
 * @author Marc
 * @version 1.0
 */
public class ReviewPage extends Fragment {
    private View view;
    private AsynchronousOperation<List<BookListing>> scraperOperation;
    private BookListing barnesAndNobleResult;
    private BookListing goodreadsResult;

    static BookListing defaultListing;

    static {
        try {
            defaultListing = new BookListing(new URL("https://www.example.com"),
                        "IDK Books",
                        new Book("A Pickle For The Knowing Ones Or Plain Truths In A Homespun Dress",
                                                     "Lord Timothy Dexter",
                                                     "Kessinger Publishing, LLC",
                                                     new Isbn("978-1162744308")),
                        1e10,
                        0,
                        null,
                        null);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Constructor. Empty because it is only really used in testing due to Android organization.
     */
    public ReviewPage() {
        //required empty constructor
    }

    private void onScraperCompletion() {
        if (scraperOperation.isCancelled())
            return;

        List<BookListing> listings;

        try {
            listings = scraperOperation.get();

            for (BookListing listing : listings) {
                if (listing.getWebsite().equals(ScraperConstants.GOODREADS)) {
                    setAuthorAndTitle(view, listing.getBook());
                    setCoverImage(view, listing);
                    setGoodreadsInfo(view, listing);
                    setAverageRatingValue(view);
                }
                else if (listing.getWebsite().equals(ScraperConstants.BARNES_AND_NOBLE)) {
                    setBarnesAndNobleInfo(view, listing);
                }
            }

            for (BookListing listing : listings) {
                if (listing.getWebsite().equals(ScraperConstants.BARNES_AND_NOBLE)) {
                    setBarnesAndNobleInfo(view, listing);
                    break;
                }
            }
        } catch (ExecutionException ex) {
            throw (RuntimeException)ex.getCause();
        } catch (CancellationException | InterruptedException ex) {
            // Display
        }
    }

    /**
     * Called immediately once the page is going to be loaded. All params are from the FirstFragment,
     * and before that from MainActivity.
     *
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return The UI that is created with all of the logic behind it
     */
    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        barnesAndNobleResult = defaultListing;
        goodreadsResult = defaultListing;

        //Create view and call scrapers
        view = inflater.inflate(R.layout.review_page, container, false);
        scraperOperation = new AsynchronousOperation<List<BookListing>>(
                new Callable<List<BookListing>>() {
                    @Override
                    public List<BookListing> call() throws Exception {
                        Query query = new Query(ScraperConstants.TEMP_HARDCODED_TITLE,
                                ScraperConstants.TEMP_HARDCODED_AUTHOR,
                                null);
                        Scraper barnesAndNoble = new BarnesAndNobleScraper();
                        // Goodreads isn't scraping correctly
                        Scraper goodreads = new GoodreadsScraper();

                        List<BookListing> barnesAndNobleResult = barnesAndNoble.scrape(query);
                        List<BookListing> goodreadsResult = goodreads.scrape(query);

                        List<BookListing> aggregate = new ArrayList<BookListing>();
                        aggregate.addAll(barnesAndNobleResult);
                        aggregate.addAll(goodreadsResult);

                        return aggregate;
                    }
                },
        this::onScraperCompletion);

        setAuthorAndTitle(view, goodreadsResult.getBook());
        setGoodreadsInfo(view, goodreadsResult);
        setAverageRatingValue(view);

        // Inflate the layout for this fragment
        return view;
    }

    /**
     * This sets the image of the book cover on the review page to be the picutre taken from the
     * camera (once implemented), the cover from the goodreads website, or a hardcoded stand in
     * @param view the UI with all the connecting logic
     * @param listing listing containing image
     */
    public void setCoverImage(View view, BookListing listing) {
        ImageView bookCover = (ImageView) view.findViewById(R.id.cover);
        if (listing.getCoverUrl() != null) {
            Picasso.get().load(listing.getCoverUrl().toString()).into(bookCover);
        }
        else {
            //if no internet link to get book cover image
            bookCover.setImageResource(R.drawable.the_glass_hotel);
        }
    }

    /**
     * Called after the view is created in <code>onCreateView()</code>. Handles last minute set up
     * things such as defining click event action.
     * @param view view created in onCreateView()
     * @param savedInstanceState saved state from between screens
     */
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        /*view.findViewById(R.id.searchByTitleButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(ReviewPage.this)
                        .navigate(R.id.action_from_review_to_first);
            }
        });*/
    }

    /**
     * Sets the title and author text at the top of the UI.
     *
     * @param view is the UI with all of the connecting logic
     * @param instance is the singleton with the information about the current book
     */
    private void setAuthorAndTitle(View view, Book instance) {
        //set author
        TextView authorText = (TextView) view.findViewById(R.id.authorText);
        authorText.setText(instance.getAuthor());
        //set title
        TextView titleText = (TextView) view.findViewById(R.id.titleText);
        titleText.setText(instance.getTitle());
    }

    /**
     * Sets the rating and review information from the Goodreads website. The review is taken to be
     * the paragraph in bold just underneath the rating. The rating taken is the average across all
     * Goodreads users.
     *
     * @param view is the UI with all of the connecting logic
     * @param listing is the listing from Goodreads
     */
    private void setGoodreadsInfo(View view, BookListing listing) {
        //set goodreads rating
        TextView goodReadsResultRating = (TextView) view.findViewById(R.id.goodreadsRating);
        try {
            goodReadsResultRating.setText(listing.getAggregateRating().toString());
        }
        catch (Exception e) {
            System.out.println(e.toString());
        }
        //set goodreads review
        TextView goodReadsResultReview = (TextView) view.findViewById(R.id.goodreadsReview);
        try {
            goodReadsResultReview.setText(listing.getReviews().get(0).getComment());
        }
        catch (Exception e) {
            System.out.println(e.toString());
        }
    }

    /**
     * Sets the rating and review information from the Barnes and Noble website. The review is taken to be
     * the paragraph in bold just underneath the rating. The rating taken is the average across all
     * Goodreads users.
     *
     * @param view is the UI with all of the connecting logic
     * @param listing is the listing from Goodreads
     */
    private void setBarnesAndNobleInfo(View view, BookListing listing) {
        // TODO: Fix this, possibly unify with Goodreads
        //set goodreads rating
        TextView banResultRating = (TextView) view.findViewById(R.id.banRating);
        try {
            goodReadsResultRating.setText(listing.getAggregateRating().toString());
        }
        catch (Exception e) {
            System.out.println(e.toString());
        }
        //set goodreads review
        TextView goodReadsResultReview = (TextView) view.findViewById(R.id.goodreadsReview);
        try {
            goodReadsResultReview.setText(listing.getReviews().get(0).getComment());
        }
        catch (Exception e) {
            System.out.println(e.toString());
        }
    }

    /**
     * Takes the four ratings from the different sites and then averages them. Once calculated,
     * it sets the value to be displayed just under the Title and Author.
     * @param view is the view created with the UI and logic
     */
    public void setAverageRatingValue(View view) {
        TextView goodReadsResultRating = (TextView) view.findViewById(R.id.goodreadsRating);
        TextView averageRating = (TextView) view.findViewById(R.id.averageRatingText);

        //Update these once other scrapers in place
        Double average = 0.0;
        try {
            average = (Double.valueOf(goodReadsResultRating.getText().toString())
                    + Double.valueOf(goodReadsResultRating.getText().toString())
                    + Double.valueOf(goodReadsResultRating.getText().toString())
                    + Double.valueOf(goodReadsResultRating.getText().toString())) / 4.0;
        } catch (Exception E) {
            System.out.println(E.toString());
        }

        averageRating.setText(average.toString());
    }
}

