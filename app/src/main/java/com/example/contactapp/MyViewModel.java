package com.example.contactapp;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MyViewModel extends ViewModel {
    private MutableLiveData<List<Contact>> contactData;

    public MyViewModel() {
    }

    public LiveData<List<Contact>> getContacts(){
        if(contactData == null){
            contactData = new MutableLiveData<>();
            initialContacts();
        }
        return contactData;
    }

    public void addContact(Contact contact){
        contactData.getValue().add(contact);
    }

    private void initialContacts() {
        contactData.setValue(new ArrayList<>());
        contactData.getValue().add(new Contact("Châu Thế Hân", "Đại Hiệp, Đại Lộc, Quảng Nam", "han@gmail.com", "099475237"));
        contactData.getValue().add(new Contact("Võ Hồng Pha", "Đại Hiệp, Đại Lộc, Quảng Nam", "pha@gmail.com", "093402375"));
        contactData.getValue().add(new Contact("Nguyễn Lê Anh Nguyên", "Hội An, Quảng Nam", "nguyen@gmail.com", "040377457"));
        contactData.getValue().add(new Contact("Lê Ngọc Minh", "Điện Ngọc, Điện Bàn, Quảng Nam", "minh@gmail.com", "070745737"));
        contactData.getValue().add(new Contact("Nguyễn Đình Chính", "Thanh Hà, Hội An, Quảng Nam", "chinh@gmail.com", "0976870878"));
        contactData.getValue().add(new Contact("Võ Văn Phúc", "Điện Thọ, Điện Bàn, Quảng Nam", "phuc@gmail.com", "0762584365"));
        contactData.getValue().add(new Contact("Phan Ngọc Quang", "Bình Lãnh, Thăng Bình, Quảng Nam", "quangpn@gmail.com", "0938206254"));
        contactData.getValue().add(new Contact("Đinh Gia Bảo", "Tam Kỳ, Quảng Nam", "dinhgiabao@gmail.com", "0915181914"));
        contactData.getValue().add(new Contact("Nguyễn Thanh Mai", "Nam Phước, Duy Xuyên, Quảng Nam", "mai@gmail.com", "012345678"));
        contactData.getValue().add(new Contact("Nguyễn Duy An", "Nam Phước, Duy Xuyên, Quảng Nam", "binhlanduyan@gmail.com", "0945243973"));
        contactData.getValue().add(new Contact("Trịnh Nhật Hưng", "Quế Sơn, Quảng Nam", "hung@gmail.com", "0942357197"));
        contactData.getValue().add(new Contact("Nguyễn Thị Tiên", "Tam Kỳ, Quảng Nam", "tien@gmail.com", "0356112087"));
        contactData.getValue().add(new Contact("Nguyễn Thị Lan", "Triệu Đại, Triệu Phong, Quảng Trị", "lan@gmail.com", "0966759343"));
        contactData.getValue().add(new Contact("Phạm Hữu Hoàng", "Điện Phương, Điện Bàn, Quảng Nam", "hwang@gmail.com", "0705213385"));
        contactData.getValue().add(new Contact("Thầy Miên", "Điện Dương, Điện Ngọc, Quảng Nam", "mien@gmail.com", "0905161114"));
        contactData.getValue().add(new Contact("Lớp phó Như", "Hoa Khanh Bac, Lien Chieu, Da Nang", "nhu@gmail.com", "0949723582"));

        Collections.sort(contactData.getValue(), (contact, t1) -> contact.getFirstName().compareToIgnoreCase(t1.getFirstName()));
    }
}
