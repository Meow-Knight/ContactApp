package com.example.contactapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private DrawerLayout drawerLayout;
    private ImageView ivMenu;
    private NavigationView navigationView;

    private SearchView svName;
    private List<Contact> contacts;
    private RecyclerView rvContacts;
    private MyAdapter adapter;
    private MyViewModel viewModel;
    private FloatingActionButton fabAdd;
    private LinearLayoutManagerWithSmoothScroller rvLayout;

    private int curHighlightIndex = -1;
    private final int highlightColor = Color.LTGRAY;
    private final int normalColor = Color.WHITE;

    // Database
    private ContactDatabase database;
    private ContactDao contactDao;

    // Default Value
    private final String DEFAULT_IMAGE_DIRECTORY = "";
    private final String DEFAULT_CONTACT_AVATAR = "ic_baseline_person_24";
    private final String DEFAULT_CONTACT_BACKGROUND = "defaultContactBackground";
    private final boolean DEFAULT_CONTACT_FAVOURITE = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        drawerLayout = findViewById(R.id.drawerlayout);

        mapComponents();
        initialDatabase();
        initialEvents();

        viewModel.getContacts().observe(this, integer -> {
            contacts.clear();
            for(Contact x : viewModel.getContacts().getValue()){
                contacts.add(x);
            }
            adapter.notifyDataSetChanged();
        });

    }

    private void initialDatabase() {
        for (Contact contact : contacts){
            viewModel.insert(contact);
        }
//        AsyncTask.execute(() -> {
//            database = ContactDatabase.getInstance(getApplicationContext());
//            contactDao = database.contactDao();
//            List<Contact> dbContacts = contactDao.getAll();
//
//            for (Contact contact : contacts){
//                contactDao.insertContacts(contact);
//            }
//        });
    }

    private void initialEvents() {
        ivMenu.setOnClickListener(view -> drawerLayout.openDrawer(GravityCompat.START));

        fabAdd.setOnClickListener(view -> {
            Dialog dialog = new Dialog(this);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.add_contact);
            dialog.setCanceledOnTouchOutside(true);

            EditText etName = dialog.findViewById(R.id.et_name);
            EditText etPhone = dialog.findViewById(R.id.et_phone);
            EditText etAddress = dialog.findViewById(R.id.et_address);
            EditText etEmail = dialog.findViewById(R.id.et_email);
            Button btCancel = dialog.findViewById(R.id.bt_cancel);
            Button btAdd = dialog.findViewById(R.id.bt_add);

            btAdd.setOnClickListener(view12 -> {
                String name = etName.getText().toString().trim();
                String phone = etPhone.getText().toString().trim();
                String address = etAddress.getText().toString().trim();
                String email = etEmail.getText().toString().trim();

                if(name.isEmpty() || phone.isEmpty() || address.isEmpty() || email.isEmpty()){
                    Toast.makeText(MainActivity.this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                } else {
                    Contact contact = new Contact(name, phone, address, email, DEFAULT_CONTACT_AVATAR, DEFAULT_CONTACT_BACKGROUND, DEFAULT_CONTACT_FAVOURITE);

                    boolean isAdded = false;
                    int i;
                    for(i = 0; i < contacts.size(); i++){
                        if (contacts.get(i).getFirstName().compareToIgnoreCase(contact.getFirstName()) > 0){
                            contacts.add(i, contact);
                            viewModel.getContacts().getValue().add(i, contact);
                            isAdded = true;
                            break;
                        }
                    }

                    if(!isAdded) {
                        contacts.add(contact);
                        viewModel.getContacts().getValue().add(contact);
                    }

                    viewModel.insert(contact);
                    adapter.notifyDataSetChanged();
                    rvContacts.smoothScrollToPosition(i);
                    curHighlightIndex = i;
                    new Handler().postDelayed(() -> {
                        if(rvLayout.findViewByPosition(curHighlightIndex) != null){
                            rvLayout.findViewByPosition(curHighlightIndex).setBackgroundColor(highlightColor);
                            curHighlightIndex  = -1;
                        }
                    }, 500);

                    dialog.cancel();
                }
            });
            btCancel.setOnClickListener(view1 -> dialog.cancel());
            dialog.show();
        });

        rvContacts.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                for(int j = rvLayout.findFirstVisibleItemPosition(); j <= rvLayout.findLastVisibleItemPosition(); j++){
                    rvLayout.findViewByPosition(j).setBackgroundColor(normalColor);
                }
                if(curHighlightIndex != -1 && rvLayout.findViewByPosition(curHighlightIndex) != null){
                    rvLayout.findViewByPosition(curHighlightIndex).setBackgroundColor(highlightColor);
                }
            }
        });

        svName.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                String input = svName.getQuery().toString().toLowerCase();
                if(!input.isEmpty()){
                    for(int index = 0; index < contacts.size(); index++){
                        if(contacts.get(index).getName().toLowerCase().contains(input)){
                            rvContacts.smoothScrollToPosition(index);
                            curHighlightIndex = index;

                            new Handler().postDelayed(() -> {
                                // set màu nổi cho phần tử đang tìm, thêm vào danh sách nổi
                                if(rvLayout.findViewByPosition(curHighlightIndex) != null){
                                    rvLayout.findViewByPosition(curHighlightIndex).setBackgroundColor(highlightColor);
                                }
                            }, 500);

                            return false;
                        }
                    }
                } else {
                    curHighlightIndex = -1;
                }

                return false;
            }
        });
        svName.setOnCloseListener(() -> {
            curHighlightIndex = -1;
            return false;
        });
    }

    private void mapComponents() {
        viewModel = ViewModelProviders.of(MainActivity.this).get(MyViewModel.class);
        contacts = new ArrayList<>(viewModel.getContacts().getValue());

        svName = findViewById(R.id.sv_name);
        rvContacts = findViewById(R.id.rv_contacts);
        fabAdd = findViewById(R.id.fab_add);
        rvLayout = new LinearLayoutManagerWithSmoothScroller(this);
        rvContacts.setLayoutManager(rvLayout);
        adapter = new MyAdapter(contacts);
        rvContacts.setAdapter(adapter);

        ivMenu = findViewById(R.id.iv_menu);
        navigationView = findViewById(R.id.navigation_view);
        navigationView.setItemIconTintList(null);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();
        switch (itemId){
            case R.id.item_home:
                drawerLayout.closeDrawer(GravityCompat.START);
                break;
            case R.id.item_personal:

                break;
            case R.id.item_assistance:
                break;
            case R.id.item_version:
                break;
        }

        return false;
    }
}