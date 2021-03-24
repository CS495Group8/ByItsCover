package com.example.byitscover;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;


import java.util.ArrayList;

public class InfoPage extends Fragment {
    private EditText eTitle;
    private EditText eAuthor;

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

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        view.findViewById(R.id.Search).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String title = eTitle.getText().toString();
                String author = eAuthor.getText().toString();

                Bundle bundle = new Bundle();
                bundle.putString("title", title);
                bundle.putString("author", author);

                NavHostFragment.findNavController(InfoPage.this)
                        .navigate(R.id.action_from_info_to_review, bundle);
            }
        });
    }
}