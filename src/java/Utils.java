package com.systemtech.mylibrary;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class Utils {

    private static Utils instance;
    private static final String ALL_BOOKS_KEY = "all_books";
    private static final String ALREADY_READ_KEY = "already_read_books";
    private static final String WISHLIST = "wishlist_books";
    private static final String CURRENTLY_READING = "currently_reading_books";
    private static final String FAVOURITES = "favourite_books";






/*
* I was using static arrays to store data, changed to SharedPreferences
 */
//    private static ArrayList<Book> currentlyReading;
//    private static ArrayList<Book> wishList;
//    private static ArrayList<Book> alreadyRead;
//    private static ArrayList<Book> favourite;
//    private static ArrayList<Book> allBooks;
    // adding shared preferences but leaving the code for the static arraylists
    private  SharedPreferences sharedPreferences;


    public Utils(Context context) {

        sharedPreferences = context.getSharedPreferences("alternate_db",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();

        Log.d("Utils.java", "Creating sharedPreferences");

        if (null == getCurrentlyReading()){
            editor.putString(CURRENTLY_READING, gson.toJson(new ArrayList<Book>()));
            editor.apply();
        }
        if (null == getWishList()){
            editor.putString(WISHLIST, gson.toJson(new ArrayList<Book>()));
            editor.apply();
        }
        if (null == getAlreadyRead()){
            editor.putString(ALREADY_READ_KEY, gson.toJson(new ArrayList<Book>()));
            editor.apply();
        }
        if (null == getFavourite()){
            editor.putString(FAVOURITES, gson.toJson(new ArrayList<Book>()));
            editor.apply();
        }
        if (null == getAllBooks()){
            editor.putString(ALL_BOOKS_KEY, gson.toJson(new ArrayList<Book>()));
            editor.apply();
        }

        addAllBooksToSharedPreferencesList(context);


    }

    public  ArrayList<Book> getCurrentlyReading() {
        Gson gson = new Gson();
        Type type = new TypeToken<ArrayList<Book>>(){}.getType();
        return gson.fromJson(sharedPreferences.getString(CURRENTLY_READING, null),type);
    }

    public  ArrayList<Book> getWishList() {
        Gson gson = new Gson();
        Type type = new TypeToken<ArrayList<Book>>(){}.getType();
        return gson.fromJson(sharedPreferences.getString(WISHLIST, null),type);
    }

    public  ArrayList<Book> getAlreadyRead() {
        Gson gson = new Gson();
        Type type = new TypeToken<ArrayList<Book>>(){}.getType();
        return gson.fromJson(sharedPreferences.getString(ALREADY_READ_KEY, null),type);
    }

    public  ArrayList<Book> getFavourite() {
        Gson gson = new Gson();
        Type type = new TypeToken<ArrayList<Book>>(){}.getType();
        return gson.fromJson(sharedPreferences.getString(FAVOURITES, null),type);
    }

    public  ArrayList<Book> getAllBooks() {

        Gson gson = new Gson();
        Type type = new TypeToken<ArrayList<Book>>(){}.getType();
        return gson.fromJson(sharedPreferences.getString(ALL_BOOKS_KEY, null),type);
    }

    // initializing array to turn to json
    private void initAllBooks() {
        Gson gson = new Gson();
//        books.add(new Book("Percy Jackson", "Rick Riordan", "https://picsum.photos/200/321.jpg", "In the story, Percy Jackson is portrayed as a demigod, the son of the mortal Sally Jackson and the Greek god Poseidon. He has ADHD and dyslexia, allegedly because he is hardwired to read Ancient Greek and has inborn \"battlefield reflexes\"."));
//        books.add(new Book("Atomic Habits", "James Clear", "https://picsum.photos/200/300.jpg", "Atomic Habits offers a proven framework for improving--every day. James Clear, one of the world's leading experts on habit formation, reveals practical strategies that will teach you exactly how to form good habits, break bad ones, and master the tiny behaviors that lead to remarkable results."));
//        books.add(new Book("Think and Grow Rich", "James Clear", "https://picsum.photos/220/300.jpg", "Think and Grow Rich is a classic work on how to lead a successful life. It was written at the commission of Andrew Carnegie and is based on interviews with men such as Henry Ford, J.P. Morgan, and John D. Rockefeller, the business titans of the early 20th century."));
//        books.add(new Book("Things Fall Apart", "Chinua Achebe", "https://picsum.photos/250/300.jpg", "Things Fall Apart by Chinua Achebe is a novel that explores the clash between traditional Igbo culture and the arrival of British colonialism in Nigeria in the late 19th century."));
//        books.add(new Book("No Longer At Ease", "Chinua Achebe", "https://picsum.photos/120/300.jpg", "It is the story of an Igbo man, Obi Okonkwo, who leaves his village for an education in Britain and then a job in the Nigerian colonial civil service, but is conflicted between his African culture and Western lifestyle and ends up taking a bribe."));
//        books.add(new Book("Arrow of God", "Chinua Achebe", "https://picsum.photos/120/300.jpg", "Set in the Ibo heartland of eastern Nigeria, one of Africa's best-known writers describes the conflict between old and new in its most poignant aspect: the personal struggle between father and son. Ezeulu, headstrong chief priest of the god Ulu, is worshipped by the six villages of Umuaro."));

        ArrayList<Book> books = new ArrayList<>();
//
//
//
//
//
    }

    public static Utils getInstance(Context context) {
        if (null!=instance){
            return instance;
        }
        else{
            instance = new Utils(context);
            return instance;
        }
    }

    private void addAllBooksToSharedPreferencesList(Context context){
        Gson gson = new Gson();
        MyDatabaseHelper db = MyDatabaseHelper.getInstance(context);
        ArrayList<Book> books = new ArrayList<>();
        Cursor cursor = db.getAllBooks();
        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                // in Book Class definition, imageurl before shortDesc but in db, shortDesc before imageurl so we need to swap 4th and 3rd column
                Book book = new Book(cursor.getString(1), cursor.getString(2), cursor.getInt(4), cursor.getString(3));
                Log.d("Utils.java", "displayAllBooks: " + book);
                books.add(book);
            }
        }else{
            Log.d("Utils.java", "addAllBooksToPreferencesList: No books found");
        }
        Log.d("AllBooksActivity", "displayAllBooks:Full books list " + books);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(ALL_BOOKS_KEY, gson.toJson(books));
        editor.apply();
        Log.d("Utils.java", "addAllBooksToSharedPreferencesList:Books successfully added to shared preferences " );
    }

    public Book getBookByTitle(String title){
        ArrayList<Book> books;
        books = getAllBooks();
        Log.d("GetBookByTitle", "books array: " + books);

        if (books != null) {
            for (Book b : books) {
                Log.d("GetBookByTitle", "Comparing '" + b.getTitle() + "' with '" + title + "'");
                if (b.getTitle().equals(title)) {
                    return b;
                }
            }
        }
        else{
            Log.e("GetBookByTitle", "title: " + title);
        }

        return null;
    }

    public  boolean addToAlreadyRead(Book book){
        ArrayList<Book> books = getAlreadyRead();
        if (books != null){
            if (books.add(book)){
                Gson gson = new Gson();
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.remove(ALREADY_READ_KEY);
                editor.putString(ALREADY_READ_KEY, gson.toJson(books));
                editor.apply();
                return true;
            }
        }
        return false;
    }

    public  boolean addToCurrentRead(Book book){
        ArrayList<Book> books = getCurrentlyReading();
        if (books != null){
            if (books.add(book)){
                Gson gson = new Gson();
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.remove(CURRENTLY_READING);
                editor.putString(CURRENTLY_READING, gson.toJson(books));
                editor.apply();
                return true;
            }
        }
        return false;
    }

    public  boolean addToFavourite(Book book){
        ArrayList<Book> books = getFavourite();
        if (books != null){
            if (books.add(book)){
                Gson gson = new Gson();
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.remove(FAVOURITES);
                editor.putString(FAVOURITES, gson.toJson(books));
                editor.apply();
                return true;
            }
        }
        return false;
    }

    public  boolean addToWishlist(Book book){
        ArrayList<Book> books = getWishList();
        if (books != null){
            if (books.add(book)){
                Gson gson = new Gson();
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.remove(WISHLIST);
                editor.putString(WISHLIST, gson.toJson(books));
                editor.apply();
                return true;
            }
        }
        return false;
    }

    public  boolean deleteFromAlreadyRead(Book book){
        ArrayList<Book> books = getAlreadyRead();
        if (books != null){
            for (Book b: books){
                if (b.getTitle().equals(book.getTitle())){
                    books.remove(b);
                    Gson gson = new Gson();
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.remove(ALREADY_READ_KEY);
                    editor.putString(ALREADY_READ_KEY, gson.toJson(books));
                    editor.apply();
                    return true;
                }
            }
        }
        return false;

    }
    public  boolean deleteFromCurrentlyReading(Book book){
        ArrayList<Book> books = getCurrentlyReading();
        if (books != null){
            for (Book b: books){
                if (b.getTitle().equals(book.getTitle())){
                    books.remove(b);
                    Gson gson = new Gson();
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.remove(CURRENTLY_READING);
                    editor.putString(CURRENTLY_READING, gson.toJson(books));
                    editor.apply();
                    return true;
                }
            }
        }
        return false;
    }
    public  boolean deleteFromFavourites(Book book){
        ArrayList<Book> books = getFavourite();
        if (books != null){
            for (Book b: books){
                if (b.getTitle().equals(book.getTitle())){
                    books.remove(b);
                    Gson gson = new Gson();
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.remove(FAVOURITES);
                    editor.putString(FAVOURITES, gson.toJson(books));
                    editor.apply();
                    return true;
                }
            }
        }
        return false;
    }
    public  boolean deleteFromWishList(Book book) {
        ArrayList<Book> books = getWishList();
        if (books != null) {
            for (Book b : books) {
                if (b.getTitle().equals(book.getTitle())) {
                    books.remove(b);
                    Gson gson = new Gson();
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.remove(WISHLIST);
                    editor.putString(WISHLIST, gson.toJson(books));
                    editor.apply();
                    return true;
                }
            }
        }
        return false;
    }
}
