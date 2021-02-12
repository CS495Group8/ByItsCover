package com.example.byitscover;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

public class ReviewPage extends Fragment {

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.review_page, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //TODO: change values from hardcoded to ones from ML alg
        CurrentBook info = CurrentBook.getInstance();
        info.setAuthor("Emily St. John Mandel");
        info.setTitle("The Glass Hotel");

        view.findViewById(R.id.backButtonFromReviewToMain).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(ReviewPage.this)
                        .navigate(R.id.action_from_review_to_first);
            }
        });
    }
}

