package com.humber.bookmeup;

public class Advert {
    public String bookAuthor;
    public String bookCondition;
    public String bookName;
    public String bookPicUrl;
    public String bookPrice;

    public Advert(){}

    public Advert(String bookAuthor, String bookCondition, String bookName, String bookPicUrl, String bookPrice){
        this.bookAuthor = bookAuthor;
        this.bookCondition = bookCondition;
        this.bookPicUrl = bookPicUrl;
        this.bookName = bookName;
        this.bookPrice = bookPrice;
    }

    public String getBookAuthor() {
        return bookAuthor;
    }

    public String getBookCondition() {
        return bookCondition;
    }

    public String getBookName() {
        return bookName;
    }

    public String getBookPicUrl() {
        return bookPicUrl;
    }

    public String getBookPrice() {
        return bookPrice;
    }
}
