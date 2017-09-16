package com.sundaydevblog.jsonparsingdemo.Models;


public class AddressModel {

    private final String street;
    private final String suite;
    private final String city;
    private final String postcode;

    public AddressModel(String street, String suite, String city, String postcode) {
        this.street = street;
        this.suite = suite;
        this.city = city;
        this.postcode = postcode;
    }

    public String getStreet() {
        return street;
    }

    public String getSuite() {
        return suite;
    }

    public String getCity() {
        return city;
    }

    public String getPostcode() {
        return postcode;
    }
}
