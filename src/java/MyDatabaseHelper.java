package com.systemtech.mylibrary;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;

import java.util.ArrayList;

public class MyDatabaseHelper extends SQLiteOpenHelper{
    private Context context;

    public static MyDatabaseHelper instance;

    private static final String DATABASE_NAME = "BookLibrary.db";
    public static final String TABLE_NAME = "my_library";
    private static final int  DATABASE_VERSION = 1;
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_TITLE = "title";
    private static final String COLUMN_AUTHOR = "author";
    private static final String COLUMN_SHORT_DESCRIPTION = "short_description";
    private static final String COLUMN_IMAGE_URL = "imageURL";


    private MyDatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    public static MyDatabaseHelper getInstance(Context context){
        if (null == instance) {
            instance = new MyDatabaseHelper(context);
        }
        return instance;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String create_query = "CREATE TABLE " + TABLE_NAME +
                "(" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_TITLE + " TEXT, " + COLUMN_AUTHOR +
                " TEXT, " + COLUMN_SHORT_DESCRIPTION + " TEXT, " + COLUMN_IMAGE_URL + " TEXT);" ;
        db.execSQL(create_query);


    }
    
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);

    }

    public void populateInitialData(SQLiteDatabase db) {
        //populate db with dummy data
        Book book1 = new Book("Percy Jackson", "Rick Riordan", R.drawable.image_percyjackson, "In the story, Percy Jackson is portrayed as a demigod, the son of the mortal Sally Jackson and the Greek god Poseidon. He has ADHD and dyslexia, allegedly because he is hardwired to read Ancient Greek and has inborn \"battlefield reflexes\".");
        Book book2 = new Book("Atomic Habits", "James Clear", R.drawable.image_atomichabits, "Atomic Habits offers a proven framework for improving--every day. James Clear, one of the world's leading experts on habit formation, reveals practical strategies that will teach you exactly how to form good habits, break bad ones, and master the tiny behaviors that lead to remarkable results.");
        Book book3 = new Book("Think and Grow Rich", "James Clear", R.drawable.image_thinkandgrowrich, "Think and Grow Rich is a classic work on how to lead a successful life. It was written at the commission of Andrew Carnegie and is based on interviews with men such as Henry Ford, J.P. Morgan, and John D. Rockefeller, the business titans of the early 20th century.");
        Book book4 = new Book("Things Fall Apart", "Chinua Achebe", R.drawable.image_thingsfallapart, "Things Fall Apart by Chinua Achebe is a novel that explores the clash between traditional Igbo culture and the arrival of British colonialism in Nigeria in the late 19th century.");
        Book book5 = new Book("No Longer At Ease", "Chinua Achebe", R.drawable.image_nolongeratease, "It is the story of an Igbo man, Obi Okonkwo, who leaves his village for an education in Britain and then a job in the Nigerian colonial civil service, but is conflicted between his African culture and Western lifestyle and ends up taking a bribe.");
        Book book6 = new Book("Arrow of God", "Chinua Achebe", R.drawable.image_arrowofgod, "Set in the Ibo heartland of eastern Nigeria, one of Africa's best-known writers describes the conflict between old and new in its most poignant aspect: the personal struggle between father and son. Ezeulu, headstrong chief priest of the god Ulu, is worshipped by the six villages of Umuaro.");
        Book book7 = new Book("The Rules Of Life", "Richard Templar",R.drawable.image_the_rules_of_life,"This books aims to redirect one's energy towards thing that really matter in life and not what we assume to matter");


        db.beginTransaction();
        try {
            // Your existing book creation code...

            addBook(book1.getTitle(), book1.getAuthor(), book1.getShortDesc(), book1.getImageURL());
            addBook(book2.getTitle(), book2.getAuthor(), book2.getShortDesc(), book2.getImageURL());
            addBook(book3.getTitle(), book3.getAuthor(), book3.getShortDesc(), book3.getImageURL());
            addBook(book4.getTitle(), book4.getAuthor(), book4.getShortDesc(), book4.getImageURL());
            addBook(book5.getTitle(), book5.getAuthor(), book5.getShortDesc(), book5.getImageURL());
            addBook(book6.getTitle(), book6.getAuthor(), book6.getShortDesc(), book6.getImageURL());
            addBook(book7.getTitle(), book7.getAuthor(), book7.getShortDesc(), book7.getImageURL());
            Log.d("MyDatabaseHelper", "populateInitialData: successfully added books");

            // ... add other books ...
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }

    }


    //add a book to app database
    public void addBook(String title, String author, String shortDesc, int imageURL){
        SQLiteDatabase db = MyDatabaseHelper.getInstance(context).getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_TITLE, title);
        values.put(COLUMN_AUTHOR, author);
        values.put(COLUMN_SHORT_DESCRIPTION, shortDesc);
        values.put(COLUMN_IMAGE_URL, imageURL);
        long result = db.insert(TABLE_NAME, null, values);
        if (result == -1){
            Log.e("DB Connection.java", "Failed to add book");
        }else{
            Log.e("DB Connection.java", "Book added successfully");
        }
    }
    //get all books from app database
    public Cursor getAllBooks(){
        SQLiteDatabase db = MyDatabaseHelper.getInstance(context).getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_NAME;
        Cursor cursor = null;
        if (db!= null){
            Log.e("DB Connection.java", "Books fetched successfully");
            cursor = db.rawQuery(query, null);

        }

        return cursor;
    }

    public ArrayList<Book> displayAllBooksList(){
        Cursor cursor = getAllBooks();
        ArrayList<Book> books = new ArrayList<>();
        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                // in Book Class definition, imageurl before shortDesc but in db, shortDesc before imageurl so we need to swap 4th and 3rd column
                Book book = new Book(cursor.getString(1), cursor.getString(2), cursor.getInt(4), cursor.getString(3));
                Log.d("DbHelper", "displayAllBooks: " + book);
                books.add(book);
            }
        } else {
            Log.d("DbHelper", "No books found");
        }
        Log.d("DbHelper", "displayAllBooks:Full books list " + books);
        cursor.close();
        return books;
    }


}
