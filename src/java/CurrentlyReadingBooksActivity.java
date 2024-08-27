package com.systemtech.mylibrary;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class CurrentlyReadingBooksActivity extends AppCompatActivity {

    private RecyclerView recycler;
    private BookRecViewAdapter adapter;
    private ImageView noDataImage;

    private final OnBackPressedCallback callback = new OnBackPressedCallback(true) {
        @Override
        public void handleOnBackPressed() {
            Intent intent = new Intent(CurrentlyReadingBooksActivity.this, MainActivity.class);
            startActivity(intent);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_currently_reading_books);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        getOnBackPressedDispatcher().addCallback(callback);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        recycler = findViewById(R.id.bookRecycler);
        noDataImage = findViewById(R.id.noDataIcon);
        String activity = "CurrentlyReadingBooksActivity";
        adapter = new BookRecViewAdapter(this, activity, new BookRecViewAdapter.OnListUpdatedListener() {
            @Override
            public void onListUpdated() {
                updateEmptyView();
            }
        });
        adapter.setBooks(Utils.getInstance(this).getCurrentlyReading());
        recycler.setAdapter(adapter);
        recycler.setLayoutManager(new LinearLayoutManager(this));

        updateEmptyView();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home){
            Intent intent = new Intent(CurrentlyReadingBooksActivity.this, MainActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    private void updateEmptyView() {
        if (adapter == null){
            noDataImage.setVisibility(View.VISIBLE);
            recycler.setVisibility(View.GONE);
        }
        if (adapter.getItemCount() == 0){
            noDataImage.setVisibility(View.VISIBLE);
            recycler.setVisibility(View.GONE);
        }
        else {
            noDataImage.setVisibility(View.GONE);
            recycler.setVisibility(View.VISIBLE);
        }
    }



}
