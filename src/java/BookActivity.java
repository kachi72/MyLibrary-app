package com.systemtech.mylibrary;



import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class BookActivity extends AppCompatActivity {

    private Button btnCurrentlyReading,btnAlreadyRead,btnWishlist,btnFavourite;
    private TextView author,bookTitle, shortDesc;
    private ImageView bookImage;

    private final OnBackPressedCallback callback = new OnBackPressedCallback(true) {
        @Override
        public void handleOnBackPressed() {
            Intent intent = new Intent(BookActivity.this, AllBooksActivity.class);
            startActivity(intent);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_book);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        getOnBackPressedDispatcher().addCallback(callback);

//        ActionBar actionBar = getSupportActionBar();
//        if (actionBar != null){
//            actionBar.setDisplayHomeAsUpEnabled(true);
//        }

        initView();
        Intent intent = getIntent();
        Book incomingBook = null;
        if (null != intent){
            String title = intent.getStringExtra("title");
            Log.d("BookActivity", "Book title: " + title);
            ArrayList<Book> books = MyDatabaseHelper.getInstance(this).displayAllBooksList();
            for (Book b: books){
                if (b.getTitle().equals(title)){
                    incomingBook = b;
                }
            }
            Log.d("BookActivity", "onCreate: " + incomingBook);
            if (null != incomingBook){
                setData(incomingBook);
                handleAlreadyRead(incomingBook);
                handleWishlist(incomingBook);
                handleFavourite(incomingBook);
                handleCurrentlyReading(incomingBook);
            }
        }





    }

    private void handleWishlist(Book book) {
        ArrayList<Book> wishList = Utils.getInstance(this).getWishList();

        boolean existsInWishlist = false;

        for (Book b: wishList){
            if (b.getTitle().equals(book.getTitle())){
                existsInWishlist = true;
                break;
            }
        }

        if (existsInWishlist){
            btnWishlist.setEnabled(false);
        }else{
            btnWishlist.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (Utils.getInstance(BookActivity.this).addToWishlist(book)){
                        Toast.makeText(BookActivity.this, book.getTitle() + " has been added to Wishlist", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(BookActivity.this, WishListActivity.class);
                        startActivity(intent);
                    }
                    else{
                        Toast.makeText(BookActivity.this, "An error has occurred", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }

    }

    private void handleFavourite(Book book) {
        ArrayList<Book> favourite = Utils.getInstance(this).getFavourite();

        boolean existsInFavourite = false;

        for (Book b: favourite){
            if (b.getTitle().equals(book.getTitle())){
                existsInFavourite = true;
                break;
            }
        }

        if (existsInFavourite){
            btnFavourite.setEnabled(false);
        }else{
            btnFavourite.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Utils.getInstance(BookActivity.this);
                    if (Utils.getInstance(BookActivity.this).addToFavourite(book)){
                        Toast.makeText(BookActivity.this, book.getTitle() + " has been added to Favourites List", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(BookActivity.this, FavouriteBooksActivity.class);
                        startActivity(intent);
                    }
                    else{
                        Toast.makeText(BookActivity.this, "An error has occurred", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    private void handleCurrentlyReading(Book book) {

        ArrayList<Book> currentRead = Utils.getInstance(this).getCurrentlyReading();

        boolean existsInCurrent = false;

        for (Book b: currentRead){
            if (b.getTitle().equals(book.getTitle())){
                existsInCurrent  = true;
                break;
            }
        }

        if (existsInCurrent){
            btnCurrentlyReading.setEnabled(false);
        }else{
            btnCurrentlyReading.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Utils.getInstance(BookActivity.this);
                    if (Utils.getInstance(BookActivity.this).addToCurrentRead(book)){
                        Toast.makeText(BookActivity.this, book.getTitle() + " has been added to Currently Reading List", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(BookActivity.this, CurrentlyReadingBooksActivity.class);
                        startActivity(intent);
                    }
                    else{
                        Toast.makeText(BookActivity.this, "An error has occurred", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    private void handleAlreadyRead(Book book)  {
        ArrayList<Book> alreadyRead = Utils.getInstance(this).getAlreadyRead();

        boolean existsInAlreadyRead = false;

        for (Book b: alreadyRead){
            if (b.getTitle().equals(book.getTitle())) {
                existsInAlreadyRead = true;
                break;
            }
        }

        if (existsInAlreadyRead){
            btnAlreadyRead.setEnabled(false);
        }
        else{
            btnAlreadyRead.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (Utils.getInstance(BookActivity.this).addToAlreadyRead(book)){
                        Toast.makeText(BookActivity.this, book.getTitle() + " has been added to Already Read List", Toast.LENGTH_SHORT).show();
                        Intent intent =  new Intent(BookActivity.this, AlreadyReadBooksActivity.class);
                        startActivity(intent);
                    }
                    else{
                        Toast.makeText(BookActivity.this, "An error has occurred", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    private void setData(Book book) {
        author.setText(book.getAuthor());
        bookTitle.setText(book.getTitle());
        shortDesc.setText(book.getShortDesc());

        Glide.with(this)
                .asBitmap()
                .load(book.getImageURL())
                .into(bookImage);
    }

    private void initView() {
        btnCurrentlyReading = findViewById(R.id.btnCurrently);
        btnAlreadyRead = findViewById(R.id.btnAlready);
        btnWishlist = findViewById(R.id.btnWishlist);
        btnFavourite = findViewById(R.id.btnFavourite);

        author = findViewById(R.id.author);
        bookTitle = findViewById(R.id.title);
        shortDesc = findViewById(R.id.shortDesc);

        bookImage = findViewById(R.id.bookImage);
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home){
            Intent intent = new Intent(BookActivity.this, MainActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}