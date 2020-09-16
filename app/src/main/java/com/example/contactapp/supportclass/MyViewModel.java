package com.example.contactapp.supportclass;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.contactapp.dao.ContactDao;
import com.example.contactapp.dao.ContactDatabase;
import com.example.contactapp.entities.Contact;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class MyViewModel extends AndroidViewModel {
    private final String DEFAULT_CONTACT_AVATAR = "ic_baseline_person_24";
    private final String DEFAULT_CONTACT_BACKGROUND = "default_contact_background";
    private MutableLiveData<List<Contact>> allContactData;
    private MutableLiveData<List<Contact>> favouriteContactData;

    private Application application;

    private ContactDao contactDao;
    List<Contact> dbContacts = new ArrayList<>();

    public MyViewModel(Application application) {
        super(application);
        this.application = application;
        ContactDatabase contactDatabase = ContactDatabase.getInstance(application.getApplicationContext());
        contactDao = contactDatabase.contactDao();
    }

    public LiveData<List<Contact>> getAllContacts(){
        if(allContactData == null){
            allContactData = new MutableLiveData<>();
            initialContacts();
        }
        return allContactData;
    }

    public LiveData<List<Contact>> getFavouriteContacts(){
        if(favouriteContactData == null){
            initialFavouriteContacts();
        }
        return favouriteContactData;
    }

    public void deleteContact(Contact contact){
        if(contact != null){
            AsyncTask.execute(() -> contactDao.delete(contact));
            allContactData.getValue().remove(contact);
            favouriteContactData.getValue().remove(contact);
        }
    }

    public void insertToDatabase(Contact contact){
        if(contact != null){
            AsyncTask.execute(() -> {
                contactDao.insertContacts(contact);
            });
        }
    }

    public void insertContact(Contact contact){
        if(contact != null){
            AsyncTask.execute(() -> {
                contactDao.insertContacts(contact);
            });
            insertContactIntoContactListByOrder(contact, Objects.requireNonNull(allContactData.getValue()));
            if(contact.isFavourite()){
                insertContactIntoContactListByOrder(contact, Objects.requireNonNull(favouriteContactData.getValue()));
            }
        }
    }

    public int insertContactIntoContactListByOrder(Contact contact, List<Contact> contactListNeedAdd){
        int i;
        for(i = 0; i < contactListNeedAdd.size(); i++){
            if (contactListNeedAdd.get(i).getFirstName().compareToIgnoreCase(contact.getFirstName()) > 0){
                contactListNeedAdd.add(i, contact);
                break;
            }
        }
        if(i == contactListNeedAdd.size()){
            contactListNeedAdd.add(contact);
        }
        return i;
    }

    // this method called to delete contacts which is highlighted before
    public void deleteContactsByIndexes(List<Integer> deleteContactIndexes){
        for (int index : deleteContactIndexes){
            final Contact deleteContact = allContactData.getValue().get(index);
            AsyncTask.execute(() -> {
                contactDao.delete(deleteContact);
            });
            favouriteContactData.getValue().remove(deleteContact);
            allContactData.getValue().remove(deleteContact);
        }
    }

    private void initialFavouriteContacts() {
        favouriteContactData = new MutableLiveData<>();
        List<Contact> tmp = new ArrayList<>();

        AsyncTask.execute(() -> {
            dbContacts = contactDao.getFavouriteContacts();
            for (Contact contact : dbContacts){
                tmp.add(contact);
            }
            Collections.sort(tmp, (contact, t1) -> contact.getFirstName().compareToIgnoreCase(t1.getFirstName()));
        });

        favouriteContactData.setValue(tmp);
    }

    private void initialContacts() {
        List<Contact> tmp = new ArrayList<>();

        AsyncTask.execute(() -> {
            dbContacts = contactDao.getAll();
            if(dbContacts.size() == 0){
                dbContacts.add(new Contact("Châu Thế Hân", "Đại Hiệp, Đại Lộc, Quảng Nam", "han@gmail.com", "099475237", "contact_avatar_1", Contact.DEFAULT_CONTACT_BACKGROUND, true));
                dbContacts.add(new Contact("Võ Hồng Pha", "Đại Hiệp, Đại Lộc, Quảng Nam", "pha@gmail.com", "093402375", "contact_avatar_2", "contact_background_2", false));
                dbContacts.add(new Contact("Nguyễn Lê Anh Nguyên", "Hội An, Quảng Nam", "nguyen@gmail.com", "040377457", "contact_avatar_3", "contact_background_3", true));
                dbContacts.add(new Contact("Lê Ngọc Minh", "Điện Ngọc, Điện Bàn, Quảng Nam", "minh@gmail.com", "070745737", "contact_avatar_4", "contact_background_4", false));
                dbContacts.add(new Contact("Nguyễn Đình Chính", "Thanh Hà, Hội An, Quảng Nam", "chinh@gmail.com", "0976870878", "contact_avatar_5", "contact_background_5", true));
                dbContacts.add(new Contact("Võ Văn Phúc", "Điện Thọ, Điện Bàn, Quảng Nam", "phuc@gmail.com", "0762584365", "contact_avatar_6", Contact.DEFAULT_CONTACT_BACKGROUND, true));
                dbContacts.add(new Contact("Phan Ngọc Quang", "Bình Lãnh, Thăng Bình, Quảng Nam", "quangpn@gmail.com", "0938206254", "contact_avatar_7", "contact_background_7", true));
                dbContacts.add(new Contact("Đinh Gia Bảo", "Tam Kỳ, Quảng Nam", "dinhgiabao@gmail.com", "0915181914", "contact_avatar_8", "contact_background_8", true));
                dbContacts.add(new Contact("Nguyễn Thanh Mai", "Nam Phước, Duy Xuyên, Quảng Nam", "mai@gmail.com", "012345678", "contact_avatar_9", "contact_background_9", false));
                dbContacts.add(new Contact("Nguyễn Duy An", "Nam Phước, Duy Xuyên, Quảng Nam", "binhlanduyan@gmail.com", "0945243973", "contact_avatar_10", "contact_background_10", true));
                dbContacts.add(new Contact("Trịnh Nhật Hưng", "Quế Sơn, Quảng Nam", "hung@gmail.com", "0942357197", "contact_avatar_11", "contact_background_11", false));
                dbContacts.add(new Contact("Nguyễn Thị Tiên", "Tam Kỳ, Quảng Nam", "tien@gmail.com", "0356112087", "contact_avatar_12", "contact_background_12", false));
                dbContacts.add(new Contact("Nguyễn Thị Lan", "Triệu Đại, Triệu Phong, Quảng Trị", "nl@gmail.com", "0966759343", "contact_avatar_13", "contact_background_13", true));
                dbContacts.add(new Contact("Phạm Hữu Hoàng", "Điện Phương, Điện Bàn, Quảng Nam", "hwang@gmail.com", "0705213385", "contact_avatar_14", "contact_background_14", false));
                dbContacts.add(new Contact("Thầy Miên", "Điện Dương, Điện Ngọc, Quảng Nam", "mien@gmail.com", "0905161114", "contact_avatar_15", "contact_background_15", false));
                dbContacts.add(new Contact("Lớp phó Như", "Hoa Khanh Bac, Lien Chieu, Da Nang", "nhu@gmail.com", "0949723582", "contact_avatar_16", "contact_background_16", false));

                for (Contact contact : dbContacts){
                    insertToDatabase(contact);
                }
            }

            for (Contact contact : dbContacts){
                tmp.add(contact);
            }
            Collections.sort(tmp, (contact, t1) -> contact.getFirstName().compareToIgnoreCase(t1.getFirstName()));
        });

        allContactData.setValue(tmp);
    }
}
