package com.example.byitscover;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.byitscover.helpers.AsyncScrape;
import com.example.byitscover.helpers.CurrentBook;

public class ReviewPage extends Fragment {

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        //TODO: change values from hardcoded to ones from ML alg
        CurrentBook info = CurrentBook.getInstance();
        info.setAuthor("Emily St. John Mandel");
        info.setTitle("The Glass Hotel");

        View view = inflater.inflate(R.layout.review_page, container, false);
        new AsyncScrape().execute();

        //TODO: Make a fancy loading screen for this while waiting for scraping to happen
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        CurrentBook instance = CurrentBook.getInstance();

        //populate UI
        //set author
        TextView authorText = (TextView) view.findViewById(R.id.bookAuthorResult);
        authorText.setText(instance.getAuthor());
        //set title
        TextView titleText = (TextView) view.findViewById(R.id.bookTitleResult);
        titleText.setText(instance.getTitle());
        //set goodreads
        TextView goodReadsResult = (TextView) view.findViewById(R.id.goodreadsScrapedValue);
        goodReadsResult.setText(instance.getReviewValues().get("Goodreads").toString());

        // Inflate the layout for this fragment
        return view;
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        view.findViewById(R.id.backButtonFromReviewToMain).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(ReviewPage.this)
                        .navigate(R.id.action_from_review_to_first);
            }
        });
    }
}

