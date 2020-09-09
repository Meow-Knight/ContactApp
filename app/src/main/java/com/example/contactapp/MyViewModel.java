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
    private final String DEFAULT_CONTACT_AVATAR = "ic_baseline_person_24";
    private final String DEFAULT_CONTACT_BACKGROUND = "default_contact_background";
    private MutableLiveData<List<Contact>> contactData;
    private Application application;

    private ContactDatabase contactDatabase;
    private ContactDao contactDao;

    public MyViewModel(Application application) {
        super(application);
        this.application = application;
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

    List<Contact> dbContacts = new ArrayList<>();
    private void initialContacts() {
        List<Contact> tmp = new ArrayList<>();

        AsyncTask.execute(() -> {
            contactDatabase = ContactDatabase.getInstance(application.getApplicationContext());
            contactDao = contactDatabase.contactDao();
            dbContacts = contactDao.getAll();
            if(dbContacts.size() == 0){
                dbContacts.add(new Contact("Châu Thế Hân", "Đại Hiệp, Đại Lộc, Quảng Nam", "han@gmail.com", "099475237", DEFAULT_CONTACT_AVATAR, DEFAULT_CONTACT_BACKGROUND, true));
                dbContacts.add(new Contact("Võ Hồng Pha", "Đại Hiệp, Đại Lộc, Quảng Nam", "pha@gmail.com", "093402375", DEFAULT_CONTACT_AVATAR, DEFAULT_CONTACT_BACKGROUND, false));
                dbContacts.add(new Contact("Nguyễn Lê Anh Nguyên", "Hội An, Quảng Nam", "nguyen@gmail.com", "040377457", DEFAULT_CONTACT_AVATAR, DEFAULT_CONTACT_BACKGROUND, true));
                dbContacts.add(new Contact("Lê Ngọc Minh", "Điện Ngọc, Điện Bàn, Quảng Nam", "minh@gmail.com", "070745737", DEFAULT_CONTACT_AVATAR, DEFAULT_CONTACT_BACKGROUND, false));
                dbContacts.add(new Contact("Nguyễn Đình Chính", "Thanh Hà, Hội An, Quảng Nam", "chinh@gmail.com", "0976870878", DEFAULT_CONTACT_AVATAR, DEFAULT_CONTACT_BACKGROUND, true));
                dbContacts.add(new Contact("Võ Văn Phúc", "Điện Thọ, Điện Bàn, Quảng Nam", "phuc@gmail.com", "0762584365", DEFAULT_CONTACT_AVATAR, DEFAULT_CONTACT_BACKGROUND, true));
                dbContacts.add(new Contact("Phan Ngọc Quang", "Bình Lãnh, Thăng Bình, Quảng Nam", "quangpn@gmail.com", "0938206254", DEFAULT_CONTACT_AVATAR, DEFAULT_CONTACT_BACKGROUND, true));
                dbContacts.add(new Contact("Đinh Gia Bảo", "Tam Kỳ, Quảng Nam", "dinhgiabao@gmail.com", "0915181914", "contact_avatar_8", "contact_background_8", true));
                dbContacts.add(new Contact("Nguyễn Thanh Mai", "Nam Phước, Duy Xuyên, Quảng Nam", "mai@gmail.com", "012345678", DEFAULT_CONTACT_AVATAR, DEFAULT_CONTACT_BACKGROUND, false));
                dbContacts.add(new Contact("Nguyễn Duy An", "Nam Phước, Duy Xuyên, Quảng Nam", "binhlanduyan@gmail.com", "0945243973", DEFAULT_CONTACT_AVATAR, DEFAULT_CONTACT_BACKGROUND, true));
                dbContacts.add(new Contact("Trịnh Nhật Hưng", "Quế Sơn, Quảng Nam", "hung@gmail.com", "0942357197", DEFAULT_CONTACT_AVATAR, DEFAULT_CONTACT_BACKGROUND, false));
                dbContacts.add(new Contact("Nguyễn Thị Tiên", "Tam Kỳ, Quảng Nam", "tien@gmail.com", "0356112087", DEFAULT_CONTACT_AVATAR, DEFAULT_CONTACT_BACKGROUND, false));
                dbContacts.add(new Contact("Nguyễn Thị Lan", "Triệu Đại, Triệu Phong, Quảng Trị", "nl@gmail.com", "0966759343", "contact_avatar_2", "contact_background_2", true));
                dbContacts.add(new Contact("Phạm Hữu Hoàng", "Điện Phương, Điện Bàn, Quảng Nam", "hwang@gmail.com", "0705213385", DEFAULT_CONTACT_AVATAR, DEFAULT_CONTACT_BACKGROUND, false));
                dbContacts.add(new Contact("Thầy Miên", "Điện Dương, Điện Ngọc, Quảng Nam", "mien@gmail.com", "0905161114", DEFAULT_CONTACT_AVATAR, DEFAULT_CONTACT_BACKGROUND, false));
                dbContacts.add(new Contact("Lớp phó Như", "Hoa Khanh Bac, Lien Chieu, Da Nang", "nhu@gmail.com", "0949723582", DEFAULT_CONTACT_AVATAR, DEFAULT_CONTACT_BACKGROUND, false));

                for (Contact contact : dbContacts){
                    insert(contact);
                }
            }

            for (Contact contact : dbContacts){
                tmp.add(contact);
            }
            Collections.sort(tmp, (contact, t1) -> contact.getFirstName().compareToIgnoreCase(t1.getFirstName()));
        });

        for (Contact contact : tmp){
            System.out.println(contact);
        }
        contactData.setValue(tmp);
    }
}
