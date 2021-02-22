package com.example.byitscover;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

/**
 * This class is the logic main home screen of our application. There is an accompanying .xml under
 * the src/res/layout directory which declares and organizes the screen. Gets created by MainActivity
 *
 * @author Marc
 * @version 1.0
 */
public class FirstFragment extends Fragment {

    /**
     * This method is what is called immediately upon it being created, but before it is shown.
     * All that is done is taking the information from MainActivity and using it to create the home
     * screen.
     *
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_first, container, false);
    }

    /**
     * This method is called once all of the UI elements are in place. Can serve as last minute
     * connections/organization before the user sees the UI such as setting up click events on UI
     * elements.
     *
     * @param view the screen created by onCreateView
     * @param savedInstanceState leftover parameter from MainActivity
     */
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        view.findViewById(R.id.searchByCoverButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(FirstFragment.this)
                        .navigate(R.id.action_from_first_to_review);
            }
        });

//        view.findViewById(R.id.button_second).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                NavHostFragment.findNavController(FirstFragment.this)
//                        .navigate(R.id.action_from_first_to_info);
//            }
//        });
    }
}