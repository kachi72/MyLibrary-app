package com.systemtech.mylibrary;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    private Button allBooks, currentBooks, wishList, favourites, alreadyRead, aboutApp;
    private MyDatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.mainActivity), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        initViews();
        // this creates the database
        dbHelper = MyDatabaseHelper.getInstance(this);
        initializeDatabase();
        Utils.getInstance(this);




        allBooks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                allBooks();
            }
        });

        alreadyRead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {alreadyRead();
            }
        });
        wishList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                wishList();
            }
        });
        currentBooks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentlyReading();
            }
        });
        favourites.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                favourites();
            }
        });
        aboutApp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                aboutApp();
            }
        });


    }

    private void initializeDatabase() {
        new Thread(() -> {
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            if (isDatabaseEmpty(db)) {
                dbHelper.populateInitialData(db);
            }

            runOnUiThread(() -> {
                // Any UI updates after database initialization
            });
        }).start();
    }

    private boolean isDatabaseEmpty(SQLiteDatabase db) {
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM " + MyDatabaseHelper.TABLE_NAME, null);
        cursor.moveToFirst();
        int count = cursor.getInt(0);
        cursor.close();
        return count == 0;
    }

    private void aboutApp() {
        Intent intent = new Intent(MainActivity.this, AppInfoActivity.class);
        startActivity(intent);
    }

    private void favourites() {
        Intent intent = new Intent(MainActivity.this, FavouriteBooksActivity.class);
        startActivity(intent);
    }

    private void currentlyReading() {
        Intent intent = new Intent(MainActivity.this, CurrentlyReadingBooksActivity.class);
        startActivity(intent);
    }

    private void wishList() {
        Intent intent = new Intent(MainActivity.this, WishListActivity.class);
        startActivity(intent);
    }

    private void alreadyRead() {
        Intent intent = new Intent(MainActivity.this, AlreadyReadBooksActivity.class);
        startActivity(intent);
    }

    private void allBooks() {
        Intent intent = new Intent(MainActivity.this, AllBooksActivity.class);
        startActivity(intent);
    }

    public void initViews(){
        allBooks = findViewById(R.id.btnAllBooks);
        currentBooks = findViewById(R.id.btnReading);
        wishList = findViewById(R.id.btnWishlist);
        favourites = findViewById(R.id.btnFavourite);
        alreadyRead = findViewById(R.id.btnAlreadyRead);
        aboutApp = findViewById(R.id.btnAbout);
    }

    //TODO add an animation when the app is launched.
    //TODO add a menu in every list activity to select all books for deletion
}