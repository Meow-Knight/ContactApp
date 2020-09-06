package com.example.contactapp;

import java.util.Comparator;

public class Contact{
    private String name, address, email, phone;

    public Contact(String name, String address, String email, String phone){
        this.name = name;
        this.address = address;
        this.email = email;
        this.phone = phone;
    }

    public Contact() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getFirstName(){
        String[] s = name.split(" ");
        return s[s.length - 1];
    }
}
