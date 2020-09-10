package com.example.contactapp.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.contactapp.entities.Contact;

import java.util.List;

@Dao
public interface ContactDao {
    @Query("SELECT * FROM contact")
    List<Contact> getAll();

    @Insert
    void insertContacts(Contact... contacts);

    @Delete
    void delete(Contact contact);

    @Query("SELECT * FROM contact WHERE isFavourite = 1")
    List<Contact> getFavouriteContacts();
}
