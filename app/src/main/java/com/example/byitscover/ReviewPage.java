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

import com.example.byitscover.helpers.AsyncScrape;
import com.example.byitscover.helpers.CurrentBook;
import com.example.byitscover.helpers.ScraperConstants;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

/**
 * This class is the logic behind the page that shows all the review and rating information to
 * the user. An accompanying .xml can be found in src/res/layout which holds all the front end, UI
 * information. The website icons are automatically loaded.
 *
 * @author Marc
 * @version 1.0
 */
public class ReviewPage extends Fragment {

    /**
     * Constructor. Empty because it is only really used in testing due to Android organization.
     */
    public ReviewPage() {
        //required empty constructor
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
        //TODO: change values from hardcoded to ones from ML alg once integrated
        CurrentBook info = CurrentBook.getInstance();

        if (info.getAuthor() == null && info.getTitle() == null) {
            info.setAuthor(ScraperConstants.TEMP_HARDCODED_AUTHOR);
            info.setTitle(ScraperConstants.TEMP_HARDCODED_TITLE);
        }

        //Create view and call scrapers
        View view = inflater.inflate(R.layout.review_page, container, false);
        //TODO: Uncomment all once able to after updating singleton issues
        new AsyncScrape(ScraperConstants.GOODREADS).execute();
        //new AsyncScrape(ScraperConstants.BARNES_AND_NOBLE).execute();
        //new AsyncScrape(ScraperConstants.GOOGLE_BOOKS).execute();
        //new AsyncScrape(ScraperConstants.AMAZON).execute();

        //TODO: Make a fancy loading screen for this while waiting for scraping to happen
        try {
            //This controls how long the app waits for the scraping to be done
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        CurrentBook instance = CurrentBook.getInstance();

        //populate UI
        setCoverImage(view, instance);
        setAuthorAndTitle(view, instance);
        setGoodreadsInfo(view, instance);
        setBarnesAndNobleInfo(view, instance);
        setGoogleBooksInfo(view, instance);
        setAmazonInfo(view, instance);
        setAverageRatingValue(view);

        // Inflate the layout for this fragment
        return view;
    }

    /**
     * Called after the view is created in <code>onCreateView()</code>. Handles last minute set up
     * things such as defining click event action.
     * @param view view created in onCreateView()
     * @param savedInstanceState saved state from between screens
     */
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        view.findViewById(R.id.buttonFromReviewToMain).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(ReviewPage.this)
                        .navigate(R.id.action_from_review_to_first);
            }
        });
    }

    /**
     * This sets the image of the book cover on the review page to be the picutre taken from the
     * camera (once implemented), the cover from the goodreads website, or a hardcoded stand in
     * @param view the UI with all the connecting logic
     * @param instance the singleton with all the information about the current book
     */
    public void setCoverImage(View view, CurrentBook instance) {
        ImageView bookCover = (ImageView) view.findViewById(R.id.cover);
        if (instance.getBookCoverUrl() != null) {
            Picasso.get().load(instance.getBookCoverUrl()).into(bookCover);
        }
        else {
            //if no internet link to get book cover image
            bookCover.setImageResource(R.drawable.the_glass_hotel);
        }
    }

    /**
     * Sets the title and author text at the top of the UI.
     *
     * @param view is the UI with all of the connecting logic
     * @param instance is the singleton with the information about the current book
     */
    private void setAuthorAndTitle(View view, CurrentBook instance) {
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
     * @param instance is the singleton with the information about the current book
     */
    private void setGoodreadsInfo(View view, CurrentBook instance) {
        //set goodreads rating
        TextView goodReadsResultRating = (TextView) view.findViewById(R.id.goodreadsRating);
        try {
            goodReadsResultRating.setText(instance.getReviewRatingValues()
                    .get(ScraperConstants.GOODREADS_RATING_KEY));
        }
        catch (Exception e) {
            System.out.println(e.toString());
        }
        //set goodreads review
        TextView goodReadsResultReview = (TextView) view.findViewById(R.id.goodreadsReview);
        try {
            goodReadsResultReview.setText(instance.getReviewRatingValues()
                    .get(ScraperConstants.GOODREADS_REVIEW_KEY));
        }
        catch (Exception e) {
            System.out.println(e.toString());
        }
    }

    /**
     * Sets the rating and review information from the Barnes and Noble website. The review
     * is from the overview and the rating it the average across all ratings.
     * Sets the rating and review information from the Amazon website. The review is taken to be
     * the paragraph in bold just underneath the rating. The rating taken is the average across all
     * Amazon users.
     *
     * @param view is the UI with all of the connecting logic
     * @param instance is the singleton with the information about the current book
     */
    private void setBarnesAndNobleInfo(View view, CurrentBook instance) {
        //set BaN rating
        TextView banResultRating = (TextView) view.findViewById(R.id.banRating);
        try {
            banResultRating.setText(instance.getReviewRatingValues()
                    .get(ScraperConstants.BAN_RATING_KEY));
        } catch (Exception e) {
            System.out.println(e.toString());
        }
        //set BaN review
        TextView banResultReview = (TextView) view.findViewById(R.id.banReview);
        try {
            banResultReview.setText(instance.getReviewRatingValues()
                    .get(ScraperConstants.BAN_REVIEW_KEY));
        } catch (Exception e) {
            System.out.println(e.toString());
        }

        return;
    }

     /** Sets the rating and review information from the Amazon website. The review is taken to be
     * the paragraph in bold just underneath the rating. The rating taken is the average across all
     * Amazon users.
     *
     * @param view is the UI with all of the connecting logic
     * @param instance is the singleton with the information about the current book
     */
    private void setAmazonInfo(View view, CurrentBook instance) {
        //set amazon rating
        TextView amazonResultRating = (TextView) view.findViewById(R.id.amazonRating);
        try {
            amazonResultRating.setText(instance.getReviewRatingValues()
                    .get(ScraperConstants.AMAZON_RATING_KEY));
        }
        catch (Exception e) {
            System.out.println(e.toString());
        }
        //set amazon review
        TextView amazonResultReview = (TextView) view.findViewById(R.id.amazonReview);
        try {
            amazonResultReview.setText(instance.getReviewRatingValues()
                    .get(ScraperConstants.AMAZON_REVIEW_KEY));
        }
        catch (Exception e) {
            System.out.println(e.toString());
        }
    }

    /**
     * Sets the rating and review information from the Google website. 
     *
     * @param view is the UI with all of the connecting logic
     * @param instance is the singleton with the information about the current book
     */
    private void setGoogleBooksInfo(View view, CurrentBook instance) {
        //set google rating
        TextView googleResultRating = (TextView) view.findViewById(R.id.googleRating);
        try {
            googleResultRating.setText(instance.getReviewRatingValues()
                    .get(ScraperConstants.GOOGLE_RATING_KEY));
        }
        catch (Exception e) {
            System.out.println(e.toString());
        }
        //set google review
        TextView googleResultReview = (TextView) view.findViewById(R.id.googleReview);
        try {
            googleResultReview.setText(instance.getReviewRatingValues()
                    .get(ScraperConstants.GOOGLE_REVIEW_KEY));
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
        TextView banResultRating = (TextView) view.findViewById(R.id.banRating);
        TextView googleResultRating = (TextView) view.findViewById(R.id.googleRating);
        TextView amazonResultRating = (TextView) view.findViewById(R.id.amazonRating);
        TextView averageRating = (TextView) view.findViewById(R.id.averageRatingText);

        //Update these once other scrapers in place
        Double average = 0.0;
        try {
            average = (Double.valueOf(goodReadsResultRating.getText().toString())
                    + Double.valueOf(amazonResultRating.getText().toString())
                    + Double.valueOf(banResultRating.getText().toString())
                    + Double.valueOf(googleResultRating.getText().toString())) / 4.0;
        } catch (Exception E) {
            System.out.println(E.toString());
        }

        averageRating.setText(average.toString());
    }
}

