package com.example.contactapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

public class DetailContact extends AppCompatActivity {
    private Contact curContact;
    private TextView tvName;
    private TextView tvPhone;
    private TextView tvAddress;
    private TextView tvEmail;
    private ImageView ivFavorite;
    private ImageView ivEdit;
    private ImageButton btCall;
    private ImageButton btMess;
    private ImageButton btEmail;
    private ImageView ivAvatar;
    private ImageView ivBackground;

    private Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_contact);

        mapComponents();
        initialEvents();

        intent = getIntent();
        if(intent != null){
            curContact = (Contact) intent.getSerializableExtra("contact");

            tvName.setText(curContact.getName());
            tvPhone.setText("SĐT: " + curContact.getPhone());
            tvAddress.setText("Địa chỉ: " + curContact.getAddress());
            tvEmail.setText("Email: " + curContact.getEmail());
        }

        if(curContact.isFavourite()){
            ivFavorite.setColorFilter(Color.argb(255, 255, 0, 0));
        }
    }

    private void initialEvents() {
        btCall.setOnClickListener(view -> {
            int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE);

            if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(
                        this,
                        new String[]{Manifest.permission.CALL_PHONE}, 1
                        );
            } else {
                startActivity(new Intent(Intent.ACTION_CALL).setData(Uri.parse("tel:" + curContact.getPhone())));
            }
        });

        btMess.setOnClickListener(view -> {
            Intent sendIntent = new Intent(Intent.ACTION_VIEW);
            sendIntent.setType("vnd.android-dir/mms-sms");
            sendIntent.putExtra("address", curContact.getPhone());
            startActivity(sendIntent);
        });

        btEmail.setOnClickListener(view -> {
            String to = curContact.getEmail();
            String subject= "Subject";
            String body= "Hello " + curContact.getName() + "! I'm Huy";
            String mailTo = "mailto:" + to +
                    "?&subject=" + Uri.encode(subject) +
                    "&body=" + Uri.encode(body);
            Intent emailIntent = new Intent(Intent.ACTION_VIEW);
            emailIntent.setData(Uri.parse(mailTo));
            startActivity(emailIntent);
        });
    }

    private void mapComponents() {
        tvName = findViewById(R.id.tv_name);
        tvPhone = findViewById(R.id.tv_phone);
        tvAddress = findViewById(R.id.tv_address);
        tvEmail = findViewById(R.id.tv_email);
        ivFavorite = findViewById(R.id.iv_favourite);
        ivEdit = findViewById(R.id.iv_edit);
        btCall = findViewById(R.id.bt_call);
        btMess = findViewById(R.id.bt_text);
        btEmail = findViewById(R.id.bt_email);
        ivAvatar = findViewById(R.id.iv_avatar);
        ivBackground = findViewById(R.id.iv_background);
    }
}