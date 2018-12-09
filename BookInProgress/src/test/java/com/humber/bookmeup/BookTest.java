package com.humber.bookmeup;


import com.humber.bookmeup.models.Book;

import junit.framework.TestCase;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertNotEquals;
public class BookTest extends TestCase {
    private Book book;

    @Before
    public void setUp() throws Exception{
        super.setUp();
        book = new Book();
    }
    @Test
    public void testGetPrice(){
        assertEquals(0d,book.getBookPrice(),2);
    }
    @Test
    public void testGetPriceNegative(){
        assertNotEquals("hey",book.getBookPrice(),2);
    }

}
