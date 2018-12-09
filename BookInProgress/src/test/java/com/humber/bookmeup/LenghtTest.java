package com.humber.bookmeup;

import com.google.firebase.storage.FirebaseStorage;
import com.humber.bookmeup.views.NewListingFragment;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class LenghtTest {
    NewListingFragment newListingFragment;

    @Before
    public void setUp() throws Exception {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        newListingFragment = new NewListingFragment();
    }

    @Test
    public void testLength() throws Exception {
        assertEquals(false, newListingFragment.textLength("ab"));
    }
}
