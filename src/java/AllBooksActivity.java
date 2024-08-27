package com.systemtech.mylibrary;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class AllBooksActivity extends AppCompatActivity  {

    private RecyclerView booksList;
    private BookRecViewAdapter adapter;
    private MyDatabaseHelper db;
    private ArrayList<Book> books;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_all_books);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.allBooksActivity), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

//         changed the parent activity in manifest, no need for this
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        // db code needs to be at the top to initialize the db because we are using it in the adapter
        db = MyDatabaseHelper.getInstance(this);
        books = new ArrayList<>();
        books = displayAllBooks();

        String activity = "AllBooksActivity";
        booksList = findViewById(R.id.bookRecycler);
        adapter = new BookRecViewAdapter(AllBooksActivity.this, activity, new BookRecViewAdapter.OnListUpdatedListener() {
            @Override
            public void onListUpdated() {

            }
        });
        adapter.setBooks(books);


//        adapter.setBooks(Utils.getInstance(this).getAllBooks());
        booksList.setAdapter(adapter);
        booksList.setLayoutManager(new LinearLayoutManager(this));



    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home){
            Intent intent = new Intent(AllBooksActivity.this, MainActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private ArrayList<Book> displayAllBooks(){
        Cursor cursor = db.getAllBooks();
        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                // in Book Class definition, imageurl before shortDesc but in db, shortDesc before imageurl so we need to swap 4th and 3rd column
                Book book = new Book(cursor.getString(1), cursor.getString(2), cursor.getInt(4), cursor.getString(3));
                Log.d("AllBooksActivity", "displayAllBooks: " + book);
                books.add(book);
            }
        } else {
            Toast.makeText(this, "No books found", Toast.LENGTH_SHORT).show();
            Log.d("AllBooksActivity", "No books found");
        }
        Log.d("AllBooksActivity", "displayAllBooks:Full books list " + books);
        cursor.close();
        return books;
    }


}