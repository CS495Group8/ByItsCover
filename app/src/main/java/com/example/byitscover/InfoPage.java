package com.example.byitscover;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;


import java.util.ArrayList;

public class InfoPage extends AppCompatActivity {
    private EditText eTitle;
    private EditText eAuthor;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.enter_book_info);

        eTitle = (EditText) findViewById(R.id.enter_title);
        eAuthor = (EditText) findViewById(R.id.enter_author);

        Button next = findViewById(R.id.Search);
        next.setOnClickListener(new View.OnClickListener(){
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                String title = eTitle.getText().toString();
                String author = eAuthor.getText().toString();
                /*Bundle bundle = new Bundle();
                bundle.putString("title", title);
                bundle.putString("author", author);*/
                Intent intent = new Intent(InfoPage.this, ReviewPage.class);
                intent.putExtra("book_title", title);
                intent.putExtra("book_author", author);
                startActivity(intent);
                //finish();
            }
        });
        //return view;
    }

    /*public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
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
    }*/
}