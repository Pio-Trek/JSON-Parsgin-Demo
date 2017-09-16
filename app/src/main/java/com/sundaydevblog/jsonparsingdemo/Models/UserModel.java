package com.sundaydevblog.jsonparsingdemo.Models;

public class UserModel extends AddressModel {

    private final String name;
    private final String email;
    private final String phone;
    private final String website;

    public UserModel(String name, String email, String phone, String website, String street, String suite, String city, String postcode) {
        super(street, suite, city, postcode);
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.website = website;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public String getWebsite() {
        return website;
    }
}
