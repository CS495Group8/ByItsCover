package com.example.byitscover;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.byitscover.helpers.AggregateScraper;
import com.example.byitscover.helpers.AsynchronousOperation;
import com.example.byitscover.helpers.Book;
import com.example.byitscover.helpers.BookListing;
import com.example.byitscover.helpers.Isbn;
import com.example.byitscover.helpers.Query;
import com.example.byitscover.helpers.Scraper;
import com.example.byitscover.helpers.ScraperConstants;
import com.example.byitscover.scrapers.BarnesAndNobleScraper;
import com.example.byitscover.scrapers.GoodreadsScraper;
import com.example.byitscover.scrapers.GoogleScraper;
import com.example.byitscover.scrapers.StorygraphScraper;
import com.squareup.picasso.Picasso;

import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DecimalFormat;
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
                    null,
                    new BigDecimal("0.00"));
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
                }
                else if (listing.getWebsite().equals(ScraperConstants.BARNES_AND_NOBLE)) {
                    setBarnesAndNobleInfo(view, listing);
                }
                else if (listing.getWebsite().equals(ScraperConstants.GOOGLE_BOOKS)) {
                    setGoogleBooksInfo(view, listing);
                }
                else if (listing.getWebsite().equals(ScraperConstants.STORYGRAPH)) {
                    setStorygraphInfo(view, listing);
                }
            }
            setAverageRatingValue(view);
        } catch (ExecutionException ex) {
            ex.printStackTrace();
            throw (RuntimeException)ex.getCause();
        } catch (CancellationException ex) {
            throw new AssertionError("onScraperComplete should never be called if the operation is cancelled");
        } catch (InterruptedException ex) {
            throw new AssertionError("The current thread should never be interrupted while getting the result from the scraper");
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
        Bundle arguments = getArguments();

        String title = null;
        String author = null;

        if (arguments == null) {
            title = ScraperConstants.TEMP_HARDCODED_TITLE;
            author = ScraperConstants.TEMP_HARDCODED_AUTHOR;
        }

        else {
            title = arguments.getString("title");
            author = arguments.getString("author");

            title = title == null ? "" : title;
            author =  author == null ? "" : author;
        }

        final Query query = new Query(title, author, null);

        //Create view and call scrapers
        view = inflater.inflate(R.layout.review_page, container, false);
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

                        return aggregate.scrape(query);
                    }
                },
                this::onScraperCompletion);

        setAuthorAndTitle(view, defaultListing.getBook());
        setGoodreadsInfo(view, defaultListing);
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
    private void setCoverImage(View view, BookListing listing) {
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
        //set goodreads price
        TextView goodReadsResultPrice = (TextView) view.findViewById(R.id.goodreadsPrice);
        try {
            goodReadsResultPrice.setText(listing.getPrice().toString());
        }
        catch (Exception e) {
            System.out.println(e.toString());
        }
    }

    /**
     * Sets the rating and review information from the Barnes and Noble website. The review is taken to be
     * the paragraph in bold just underneath the rating. The rating taken is the average across all
     * BaN users.
     *
     * @param view is the UI with all of the connecting logic
     * @param listing is the listing from Barnes and Noble
     */
    private void setBarnesAndNobleInfo(View view, BookListing listing) {
        // TODO: Fix this, possibly unify with Goodreads
        //set BaN rating
        TextView banResultRating = (TextView) view.findViewById(R.id.banRating);
        try {
            banResultRating.setText(listing.getAggregateRating().toString());
        }
        catch (Exception e) {
            System.out.println(e.toString());
        }
        //set BaN review
        TextView banResultReview = (TextView) view.findViewById(R.id.banReview);
        try {
            banResultReview.setText(listing.getReviews().get(0).getComment());
        }
        catch (Exception e) {
            System.out.println(e.toString());
        }
        //set BaN price
        TextView banResultPrice = (TextView) view.findViewById(R.id.banPrice);
        try {
            banResultPrice.setText(listing.getPrice().toString());
        }
        catch (Exception e) {
            System.out.println(e.toString());
        }
    }

    /**
     * Sets the rating and review information from the Google Books website. The review is taken to be
     * the paragraph in bold just underneath the rating.
     *
     * @param view is the UI with all of the connecting logic
     * @param listing is the listing from Google Books
     */
    private void setGoogleBooksInfo(View view, BookListing listing) {
        //set Google rating
        TextView googleResultRating = (TextView) view.findViewById(R.id.googleRating);
        try {
            googleResultRating.setText(listing.getAggregateRating().toString());
        }
        catch (Exception e) {
            System.out.println(e.toString());
        }
        //set Google review
        TextView googleResultReview = (TextView) view.findViewById(R.id.googleReview);
        try {
            googleResultReview.setText(listing.getReviews().get(0).getComment());
        }
        catch (Exception e) {
            System.out.println(e.toString());
        }
        //set google price
        TextView googleResultPrice = (TextView) view.findViewById(R.id.googlePrice);
        try {
            googleResultPrice.setText(listing.getPrice().toString());
        }
        catch (Exception e) {
            System.out.println(e.toString());
        }
    }

    /**
     * Sets the rating and review information from the Storygraph website. The review is taken to be
     * the paragraph in bold just underneath the rating.
     *
     * @param view is the UI with all of the connecting logic
     * @param listing is the listing from Storygraph
     */
    private void setStorygraphInfo(View view, BookListing listing) {
        //set Google rating
        TextView storygraphResultRating = (TextView) view.findViewById(R.id.storygraphRating);
        try {
            storygraphResultRating.setText(listing.getAggregateRating().toString());
        }
        catch (Exception e) {
            System.out.println(e.toString());
        }
        //set Google review
        TextView storygraphResultReview = (TextView) view.findViewById(R.id.storyGraphReview);
        try {
            storygraphResultReview.setText(listing.getReviews().get(0).getComment());
        }
        catch (Exception e) {
            System.out.println(e.toString());
        }
        //set storygraph price
        TextView storygraphResultPrice = (TextView) view.findViewById(R.id.storygraphPrice1);
        try {
            storygraphResultPrice.setText(listing.getPrice().toString());
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
    private void setAverageRatingValue(View view) {
        TextView goodReadsResultRating = (TextView) view.findViewById(R.id.goodreadsRating);
        TextView banResultRating = (TextView) view.findViewById(R.id.banRating);
        TextView googleResultRating = (TextView) view.findViewById(R.id.googleRating);
        TextView sgResultRating = (TextView) view.findViewById(R.id.storygraphRating);
        TextView averageRating = (TextView) view.findViewById(R.id.averageRatingText);

        //Update these once other scrapers in place
        Double average = 0.0;
        try {
            average = (Double.valueOf(goodReadsResultRating.getText().toString())
                    + Double.valueOf(banResultRating.getText().toString())
                    + Double.valueOf(googleResultRating.getText().toString())
                    + Double.valueOf(sgResultRating.getText().toString())) / 4.0;
        } catch (Exception E) {
            System.out.println(E.toString());
        }
        
        DecimalFormat df = new DecimalFormat("#.##");
        averageRating.setText(Double.valueOf(df.format(average)).toString());
    }
}
