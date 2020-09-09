package com.example.contactapp;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MyViewModel extends AndroidViewModel {
    private MutableLiveData<List<Contact>> contactData;

    private ContactDatabase contactDatabase;
    private ContactDao contactDao;

    public MyViewModel(Application application) {
        super(application);
        contactDatabase = ContactDatabase.getInstance(application);
        contactDao = contactDatabase.contactDao();
    }

    public LiveData<List<Contact>> getContacts(){
        if(contactData == null){
            contactData = new MutableLiveData<>();
            initialContacts();
        }
        return contactData;
    }

    public void insert(Contact contact){
        new InsertAsyncTask(contactDao).execute(contact);
    }

    private class InsertAsyncTask extends AsyncTask<Contact, Void, Void>{
        ContactDao contactDao;

        public InsertAsyncTask(ContactDao contactDao) {
            this.contactDao = contactDao;
        }

        @Override
        protected Void doInBackground(Contact... contacts) {
            contactDao.insertContacts(contacts[0]);
            return null;
        }
    }

    public void addContact(Contact contact){
        contactData.getValue().add(contact);
    }

    private void initialContacts() {
        contactData.setValue(new ArrayList<>());
        contactData.getValue().add(new Contact(1, "Châu Thế Hân", "Đại Hiệp, Đại Lộc, Quảng Nam", "han@gmail.com", "099475237", "contact_avatar1", "contact_background1", true));
        contactData.getValue().add(new Contact(2, "Võ Hồng Pha", "Đại Hiệp, Đại Lộc, Quảng Nam", "pha@gmail.com", "093402375", "contact_avatar1", "contact_background1", false));
        contactData.getValue().add(new Contact(3, "Nguyễn Lê Anh Nguyên", "Hội An, Quảng Nam", "nguyen@gmail.com", "040377457", "contact_avatar1", "contact_background1", true));
        contactData.getValue().add(new Contact(4, "Lê Ngọc Minh", "Điện Ngọc, Điện Bàn, Quảng Nam", "minh@gmail.com", "070745737", "contact_avatar1", "contact_background1", false));
        contactData.getValue().add(new Contact(5, "Nguyễn Đình Chính", "Thanh Hà, Hội An, Quảng Nam", "chinh@gmail.com", "0976870878", "contact_avatar1", "contact_background1", true));
        contactData.getValue().add(new Contact(6, "Võ Văn Phúc", "Điện Thọ, Điện Bàn, Quảng Nam", "phuc@gmail.com", "0762584365", "contact_avatar1", "contact_background1", true));
        contactData.getValue().add(new Contact(7, "Phan Ngọc Quang", "Bình Lãnh, Thăng Bình, Quảng Nam", "quangpn@gmail.com", "0938206254", "contact_avatar1", "contact_background1", true));
        contactData.getValue().add(new Contact(8, "Đinh Gia Bảo", "Tam Kỳ, Quảng Nam", "dinhgiabao@gmail.com", "0915181914", "contact_avatar1", "contact_background1", true));
        contactData.getValue().add(new Contact(9, "Nguyễn Thanh Mai", "Nam Phước, Duy Xuyên, Quảng Nam", "mai@gmail.com", "012345678", "contact_avatar1", "contact_background1", false));
        contactData.getValue().add(new Contact(10, "Nguyễn Duy An", "Nam Phước, Duy Xuyên, Quảng Nam", "binhlanduyan@gmail.com", "0945243973", "contact_avatar1", "contact_background1", true));
        contactData.getValue().add(new Contact(11, "Trịnh Nhật Hưng", "Quế Sơn, Quảng Nam", "hung@gmail.com", "0942357197", "contact_avatar1", "contact_background1", false));
        contactData.getValue().add(new Contact(12, "Nguyễn Thị Tiên", "Tam Kỳ, Quảng Nam", "tien@gmail.com", "0356112087", "contact_avatar1", "contact_background1", false));
        contactData.getValue().add(new Contact(13, "Nguyễn Thị Lan", "Triệu Đại, Triệu Phong, Quảng Trị", "lan@gmail.com", "0966759343", "contact_avatar1", "contact_background1", true));
        contactData.getValue().add(new Contact(14, "Phạm Hữu Hoàng", "Điện Phương, Điện Bàn, Quảng Nam", "hwang@gmail.com", "0705213385", "contact_avatar1", "contact_background1", false));
        contactData.getValue().add(new Contact(15, "Thầy Miên", "Điện Dương, Điện Ngọc, Quảng Nam", "mien@gmail.com", "0905161114", "contact_avatar1", "contact_background1", false));
        contactData.getValue().add(new Contact(16, "Lớp phó Như", "Hoa Khanh Bac, Lien Chieu, Da Nang", "nhu@gmail.com", "0949723582", "contact_avatar1", "contact_background1", false));

        Collections.sort(contactData.getValue(), (contact, t1) -> contact.getFirstName().compareToIgnoreCase(t1.getFirstName()));
    }
}
