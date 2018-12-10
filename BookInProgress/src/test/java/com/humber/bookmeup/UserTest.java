package com.humber.bookmeup;

import com.humber.bookmeup.models.User;

import junit.framework.TestCase;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertNotEquals;

public class UserTest extends TestCase {
    private User user;
    @Before
    public void setUp() throws Exception{
        super.setUp();
        user = new User();
    }
    public void testGetCountry(){
        assertNotEquals(null,user.getCountry(),"");
    }
}
