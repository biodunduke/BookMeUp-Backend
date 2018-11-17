package com.humber.bookmeup;

import android.net.Uri;

public class Book {
    public String bookName;
    public String bookAuthor;
    public String bookISBN;
    public String bookCondition;
    public int bookPrice;
    public String userId;
    public String bookPicUrl;

    public Book(){}

    public Book(String bookName, String bookAuthor, String bookISBN,String bookCondition, String bookPicUrl, String userId,int bookPrice){
        this.bookAuthor = bookAuthor;
        this.bookCondition = bookCondition;
        this.bookISBN = bookISBN;
        this.bookPrice = bookPrice;
        this.bookName = bookName;
        this.userId = userId;
        this.bookPicUrl = bookPicUrl;

    }

    /* NOTE: alt+insert */


    public String getBookName() {
        return bookName;
    }

    public String getBookAuthor() {
        return bookAuthor;
    }

    public String getBookISBN() {
        return bookISBN;
    }

    public String getBookCondition() {
        return bookCondition;
    }

    public String getUserId() {
        return userId;
    }

    public int getBookPrice() {

        return bookPrice;
    }
}
