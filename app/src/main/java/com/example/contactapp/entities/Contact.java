package com.example.contactapp.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity (tableName = "contact")
public class Contact implements Serializable {
    @PrimaryKey (autoGenerate = true)
    @ColumnInfo (name = "id")
    private int id;

    @ColumnInfo (name = "name")
    private String name;

    @ColumnInfo (name = "address")
    private String address;

    @ColumnInfo (name = "email")
    private String email;

    @ColumnInfo (name = "phone")
    private String phone;

    @ColumnInfo (name = "avatar")
    private String avatar;

    @ColumnInfo (name = "background")
    private String background;

    @ColumnInfo (name = "isFavourite")
    private boolean isFavourite;

    public Contact(int id, String name, String address, String email, String phone, String avatar, String background, boolean isFavourite){
        this.id = id;
        this.name = name;
        this.address = address;
        this.email = email;
        this.phone = phone;
        this.avatar = avatar;
        this.background = background;
        this.isFavourite = isFavourite;
    }

    public Contact(String name, String address, String email, String phone, String avatar, String background, boolean isFavourite){
        this.id = id;
        this.name = name;
        this.address = address;
        this.email = email;
        this.phone = phone;
        this.avatar = avatar;
        this.background = background;
        this.isFavourite = isFavourite;
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

    public void setId(int id){
        this.id = id;
    }

    public int getId(){
        return id;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getBackground() {
        return background;
    }

    public void setBackground(String background) {
        this.background = background;
    }

    public boolean isFavourite() {
        return isFavourite;
    }

    public void setFavourite(boolean favourite) {
        isFavourite = favourite;
    }

    public String getFirstName(){
        name = name.trim();
        String[] s = name.split("[\\s]+");
        return s[s.length - 1];
    }
}
