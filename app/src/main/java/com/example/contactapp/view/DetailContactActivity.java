package com.example.contactapp.view;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.contactapp.R;
import com.example.contactapp.entities.Contact;

public class DetailContactActivity extends AppCompatActivity {
    private Contact curContact;

    // components
    private TextView tvName;
    private TextView tvLeftSupportName;
    private TextView tvBottomSupportName;
    private TextView tvPhone;
    private TextView tvAddress;
    private TextView tvEmail;
    private ImageView ivBackToMain;
    private ImageView ivYesFavorite;
    private ImageView ivNoFavorite;
    private ImageView ivEdit;
    private ImageButton btCall;
    private ImageButton btMess;
    private ImageButton btEmail;
    private ImageView ivAvatar;
    private ImageView ivBackground;

    // properties
    private boolean isChangedContact = false;
    private boolean originalFavourite;
    private AnimatorSet frontToBackAnimator;
    private AnimatorSet backToFrontAnimator;

    private Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_contact);
         frontToBackAnimator = (AnimatorSet) AnimatorInflater.loadAnimator(DetailContactActivity.this,
                R.animator.flip_front_to_back);
        backToFrontAnimator = (AnimatorSet) AnimatorInflater.loadAnimator(DetailContactActivity.this,
                R.animator.flip_back_to_front);

        mapComponents();
        initialEvents();

        intent = getIntent();
        if(intent != null){
            curContact = (Contact) intent.getSerializableExtra("contact");
            setNameView(curContact.getName());

            tvPhone.setText(curContact.getPhone());
            tvAddress.setText(curContact.getAddress());
            tvEmail.setText(curContact.getEmail());

            int avatarId = getResources().getIdentifier("com.example.contactapp:drawable/" + curContact.getAvatar(), null, null);
            int backgroundId = getResources().getIdentifier("com.example.contactapp:drawable/" + curContact.getBackground(), null, null);
            ivAvatar.setImageResource(avatarId);
            ivBackground.setImageResource(backgroundId);

            if(curContact.isFavourite()){
                originalFavourite = true;
                ivYesFavorite.setVisibility(View.VISIBLE);
                ivYesFavorite.setClickable(true);
                ivNoFavorite.setVisibility(View.GONE);
                ivNoFavorite.setClickable(false);
            } else {
                originalFavourite = false;
                ivYesFavorite.setVisibility(View.GONE);
                ivYesFavorite.setClickable(false);
                ivNoFavorite.setVisibility(View.VISIBLE);
                ivNoFavorite.setClickable(true);
            }
        }

        float scale = getApplicationContext().getResources().getDisplayMetrics().density;
        ivYesFavorite.setCameraDistance(scale * 8000);
        ivNoFavorite.setCameraDistance(scale * 8000);
    }

    private void setNameView(String name) {
        tvName.setText(name);
        tvLeftSupportName.setText(name);
        tvBottomSupportName.setText(name);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode == 456 && resultCode == RESULT_OK){
            isChangedContact = true;
            if(data.getSerializableExtra("changedContact") == null){
                curContact = null;
                Intent returnedIntent = new Intent();
                returnedIntent.putExtra("returnedContact", curContact);
                setResult(RESULT_OK, returnedIntent);
                finish();
            } else {
                Toast.makeText(DetailContactActivity.this, "Đã thay đổi dữ liệu", Toast.LENGTH_SHORT).show();
                curContact = (Contact) data.getSerializableExtra("changedContact");
                // display new data
                setNameView(curContact.getName());
                tvAddress.setText(curContact.getAddress());
                tvEmail.setText(curContact.getEmail());
                tvPhone.setText(curContact.getPhone());
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    private void initialEvents() {
        ivYesFavorite.setOnClickListener(view -> {
            curContact.setFavourite(false);
            ivNoFavorite.setVisibility(View.VISIBLE);
            ivNoFavorite.setClickable(true);
            frontToBackAnimator.setTarget(ivYesFavorite);
            backToFrontAnimator.setTarget(ivNoFavorite);
            frontToBackAnimator.start();
            backToFrontAnimator.start();
            ivYesFavorite.setClickable(false);
        });

        ivNoFavorite.setOnClickListener(view -> {
            curContact.setFavourite(true);
            ivYesFavorite.setVisibility(View.VISIBLE);
            ivYesFavorite.setClickable(true);
            frontToBackAnimator.setTarget(ivNoFavorite);
            backToFrontAnimator.setTarget(ivYesFavorite);
            frontToBackAnimator.start();
            backToFrontAnimator.start();
            ivNoFavorite.setClickable(false);
        });

        ivBackToMain.setOnClickListener(view -> {
            if(isChangedContact || curContact.isFavourite() != originalFavourite){
                Intent returnedIntent = new Intent();
                returnedIntent.putExtra("returnedContact", curContact);
                setResult(RESULT_OK, returnedIntent);
            }
            finish();
        });

        ivEdit.setOnClickListener(view -> {
            Intent intent = new Intent(DetailContactActivity.this, EditContactActivity.class);
            intent.putExtra("contact_need_edit", curContact);
            startActivityForResult(intent, 456);
        });

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
        tvLeftSupportName = findViewById(R.id.tv_left_support_name);
        tvBottomSupportName = findViewById(R.id.tv_bottom_support_name);
        tvPhone = findViewById(R.id.tv_phone);
        tvAddress = findViewById(R.id.tv_address);
        tvEmail = findViewById(R.id.tv_email);
        ivBackToMain = findViewById(R.id.iv_back_to_main);
        ivYesFavorite = findViewById(R.id.iv_yes_favourite);
        ivNoFavorite = findViewById(R.id.iv_no_favourite);
        ivEdit = findViewById(R.id.iv_edit);
        btCall = findViewById(R.id.bt_call);
        btMess = findViewById(R.id.bt_text);
        btEmail = findViewById(R.id.bt_email);
        ivAvatar = findViewById(R.id.iv_avatar);
        ivBackground = findViewById(R.id.iv_background);
    }
}