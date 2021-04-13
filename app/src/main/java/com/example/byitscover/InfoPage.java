package com.example.byitscover;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

/**
 * This page is where the user can enter the title and author for the book they are looking for.
 * Once information is entered, it can be sent to the review page
 *
 * @author Emily Schroeder
 * @version 1.0
 */
public class InfoPage extends AppCompatActivity {
    private EditText eTitle;
    private EditText eAuthor;

    /**
     * Runs when page is started.
     * Creates text fields to enter title and author information
     * Sets listener for previous button to return to home page
     * Sets listener for next button to send title and author information to review page
     * @param savedInstanceState
     */
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
                Intent intent = new Intent(InfoPage.this, ReviewPage.class);
                intent.putExtra("book_title", title);
                intent.putExtra("book_author", author);
                startActivity(intent);
            }
        });
        Button prev = findViewById(R.id.previous);
        prev.setOnClickListener(new View.OnClickListener(){
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}