/**
 * Team LastMinSub:
 * Abiodun Ojo
 * David Uche
 * Elias Sabbagh
 *
 * Model for the User. Needed for user profile
 *
 * */

package com.humber.bookmeup.models;

public class User {
    public String country;
    public String city;
    public String address;
    public int rating;

    public User(){}

    public User(String country, String city, String address, int rating){
        this.country = country;
        this.city = city;
        this.address = address;
        this.rating = rating;
    }

    public String getCountry(){
        return this.country;
    }

    public String getAddress(){
        return this.address;
    }

    public String getCity(){
        return this.city;
    }

    public int getRating(){
        return this.rating;
    }
}
