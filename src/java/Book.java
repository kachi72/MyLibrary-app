package com.systemtech.mylibrary;

public class Book {

    private String title;
    private String author;
    private int imageURL;
    private boolean isExpanded;
    private String shortDesc;

    public boolean isExpanded() {
        return isExpanded;
    }

    public void setExpanded(boolean expanded) {
        isExpanded = expanded;
    }

    public String getShortDesc() {
        return shortDesc;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public int getImageURL() {
        return imageURL;
    }

    public Book(String title, String author, int imageURL, String shortDesc) {
        this.title = title;
        this.author = author;
        this.imageURL = imageURL;
        this.shortDesc = shortDesc;
        this.isExpanded = false;
    }

    //TODO make book picture bigger in book activity and change app font from lemon to something more professional
}
