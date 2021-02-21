package com.example.byitscover;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

public class InfoPage extends Fragment {

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.enter_book_info, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        view.findViewById(R.id.button_prev).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(InfoPage.this)
                        .navigate(R.id.action_from_info_to_first);
            }
        });

        view.findViewById(R.id.Search).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText eTitle = (EditText) view.findViewById(R.id.enter_title);   //returns NULL
                EditText eAuthor = (EditText) view.findViewById(R.id.enter_author); //returns NULL
                NavHostFragment.findNavController(InfoPage.this)
                        .navigate(R.id.action_from_info_to_review);
            }
        });
    }
}