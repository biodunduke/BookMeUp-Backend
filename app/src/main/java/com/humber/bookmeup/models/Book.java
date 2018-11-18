/**
 * David Uche
 * Abiodun Ojo
 * Elias
 *
 *
 * Model for listings. Needed when fetching all listings
 * */

package com.humber.bookmeup.models;

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



    public Book(String bookName, String bookAuthor, String bookISBN, String bookCondition, String bookPicUrl, String userId, int bookPrice){
        this.bookAuthor = bookAuthor;
        this.bookCondition = bookCondition;
        this.bookISBN = bookISBN;
        this.bookPrice = bookPrice;
        this.bookName = bookName;
        this.userId = userId;
        this.bookPicUrl = bookPicUrl;

    }



    public Book(String bookName, String bookAuthor,String bookPicUrl, int bookPrice){
        this.bookAuthor = bookAuthor;
        this.bookPrice = bookPrice;
        this.bookName = bookName;
        this.bookPicUrl = bookPicUrl;
    }

    /* NOTE: alt+insert */

    public String getBookPicUrl() {
        return bookPicUrl;
    }

    public String getBookName() {
        return this.bookName;
    }

    public String getBookAuthor() {
        return this.bookAuthor;
    }

    public String getBookISBN() {
        return this.bookISBN;
    }

    public String getBookCondition() {
        return this.bookCondition;
    }

    public String getUserId() {
        return this.userId;
    }

    public int getBookPrice() {

        return this.bookPrice;
    }
}
