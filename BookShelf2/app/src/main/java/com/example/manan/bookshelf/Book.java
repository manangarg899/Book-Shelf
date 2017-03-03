package com.example.manan.bookshelf;

/**
 * Created by Manan on 14-02-2017.
 */

public class Book {

    private String mAuthor;

    private String mTitle;

    private String mUrl;

    private String mImageUrl;

    public Book(String author, String title, String url, String imageUrl) {
        mAuthor = author;
        mTitle = title;
        mUrl = url;
        mImageUrl = imageUrl;
    }

    public String getAuthor() {
        return mAuthor;
    }

    public String getTitle() {
        return mTitle;
    }

    public String getUrl() {
        return mUrl;
    }

    public String getImageUrl() {
        return mImageUrl;
    }
}
