package com.example.contactapp.view;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;

import com.example.contactapp.R;
import com.example.contactapp.entities.Contact;

import java.util.List;

public class EditContactActivity extends AppCompatActivity {

    // components
    private ImageView ivCancel;
    private ImageView ivChange;
    private EditText etName;
    private EditText etPhone;
    private EditText etAddress;
    private EditText etEmail;
    private Button btDeleteContact;
    private RadioButton rbYesFavourite;
    private RadioButton rbNoFavourite;

    // properties
    private Contact curContact;
    private Contact changedContact;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_contact);

        mapComponents();

        // get data from detail contact activity
        Intent intent = getIntent();
        if(intent != null){
            curContact = (Contact) intent.getSerializableExtra("contact_need_edit");
            etName.setText(curContact.getName());
            etPhone.setText(curContact.getPhone());
            etAddress.setText(curContact.getAddress());
            etEmail.setText(curContact.getEmail());
            if(curContact.isFavourite()){
                rbYesFavourite.setChecked(true);
            } else {
                rbNoFavourite.setChecked(true);
            }
        }

        initialEvents();
    }

    private void initialEvents() {
        btDeleteContact.setOnClickListener(view -> {
            DialogInterface.OnClickListener dialogClickListener = (dialogInterface, i) -> {
                switch (i){
                    case DialogInterface.BUTTON_POSITIVE:
                        changedContact = null;
                        Intent returnedIntent = new Intent();
                        returnedIntent.putExtra("changedContact", changedContact);
                        setResult(RESULT_OK, returnedIntent);
                        finish();
                        break;
                    case  DialogInterface.BUTTON_NEGATIVE:
                }
            };

            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
            alertDialogBuilder.setMessage("Xóa liên hệ này?")
                    .setPositiveButton("Đồng ý", dialogClickListener)
                    .setNegativeButton("Hủy", dialogClickListener)
                    .show();
        });

        ivCancel.setOnClickListener(view -> finish());

        ivChange.setOnClickListener(view -> {
            // consider if having any changes in this contact -> return new contact to detail activity
            String newName = etName.getText().toString().trim();
            String newPhone = etPhone.getText().toString().trim();
            String newAddress = etAddress.getText().toString().trim();
            String newEmail = etEmail.getText().toString().trim();
            boolean isFavourite = rbYesFavourite.isChecked();

            if (!newName.equalsIgnoreCase(curContact.getName())
                    || !newPhone.equalsIgnoreCase(curContact.getPhone())
                    || !newAddress.equalsIgnoreCase(curContact.getAddress())
                    || !newEmail.equalsIgnoreCase(curContact.getEmail())) {
                changedContact = new Contact(newName, newAddress, newEmail, newPhone, isFavourite);
                Intent returnedIntent = new Intent();
                returnedIntent.putExtra("changedContact", changedContact);
                setResult(RESULT_OK, returnedIntent);
            }
            finish();
        });
    }

    private void mapComponents() {
        ivCancel = findViewById(R.id.iv_cancel);
        ivChange = findViewById(R.id.iv_change_contact);
        etName = findViewById(R.id.et_edit_name);
        etPhone = findViewById(R.id.et_edit_phone);
        etAddress = findViewById(R.id.et_edit_address);
        etEmail = findViewById(R.id.et_edit_email);
        btDeleteContact = findViewById(R.id.bt_delete_contact);
        rbYesFavourite = findViewById(R.id.rb_yes_favourite_contact);
        rbNoFavourite = findViewById(R.id.rb_no_favourite_contact);
    }
}