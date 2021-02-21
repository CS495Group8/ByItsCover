package com.example.byitscover;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.byitscover.helpers.CurrentBook;

public class InfoPage extends Fragment {

    EditText eTitle;
    EditText eAuthor;


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
                CurrentBook instance = CurrentBook.getInstance();
                instance.setTitle(eTitle.getText().toString()); //saves title on click
                instance.setAuthor(eAuthor.getText().toString()); //saves author on click
                NavHostFragment.findNavController(InfoPage.this)
                        .navigate(R.id.action_from_info_to_review);
            }
        });
    }
}