package com.example.contactapp.view;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.Toast;

import com.example.contactapp.supportclass.LinearLayoutManagerWithSmoothScroller;
import com.example.contactapp.supportclass.MyAdapter;
import com.example.contactapp.supportclass.MyViewModel;
import com.example.contactapp.R;
import com.example.contactapp.entities.Contact;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

import java.util.List;
import java.util.Objects;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private DrawerLayout drawerLayout;
    private ImageView ivMenu;
    private NavigationView navigationView;
    private LinearLayoutManagerWithSmoothScroller rvLayout;

    private ImageView ivAvatar;
    private SearchView svName;
    private RecyclerView rvContacts;
    private MyAdapter allContactAdapter;
    private MyAdapter favouriteContactAdapter;
    private MyViewModel viewModel;
    private FloatingActionButton fabAdd;
    private FloatingActionButton fabMoreAction;
    private FloatingActionButton fabFavourite;
    private FloatingActionButton fabDeleteContact;

    private List<Contact> contacts;
    private int curHighlightIndex = -1;
    private final int highlightColor = Color.LTGRAY;
    private final int normalColor = Color.WHITE;
    private final int animationTime = 400;
    private boolean isOpenedMoreAction = false;
    private boolean isShowingFavouriteContacts = false;
    private boolean isOnDeleteMode = false;
    private boolean isShowingAddContactMode = false;

    // Default Value
    private final int DEFAULT_MOREACTION_BUTTON_COLOR = Color.argb(255, 55, 0, 179);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        drawerLayout = findViewById(R.id.drawerlayout);

        mapComponents();
        initialEvents();
    }

    /* Get contact returned from DetailContactActivity
        Delete old contact got by getCurTouchedContact method
        if returned contact has not null value, replace deleted contact
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == 123 && resultCode == RESULT_OK) {
            MyAdapter adapter = (MyAdapter) rvContacts.getAdapter();
            viewModel.deleteContact(adapter.getCurTouchedContact());

            if(data.getSerializableExtra("returnedContact") != null){
                Contact returnedContact = (Contact) data.getSerializableExtra("returnedContact");
                returnedContact.setId(adapter.getCurTouchedContact().getId());
                returnedContact.setAvatar(adapter.getCurTouchedContact().getAvatar());
                returnedContact.setBackground(adapter.getCurTouchedContact().getBackground());
                viewModel.insertContact(returnedContact);
            } else {
                Toast.makeText(MainActivity.this, "Đã xóa liên hệ", Toast.LENGTH_SHORT).show();
            }

            if(isShowingFavouriteContacts){
                favouriteContactAdapter.notifyDataSetChanged();
            } else {
                allContactAdapter.notifyDataSetChanged();
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    private void setBackgroundColorForMoreActionButton(int resourceColor){
        int color = ContextCompat.getColor(this, resourceColor);
        fabMoreAction.getDrawable().setColorFilter(color, PorterDuff.Mode.SRC_IN);
    }

    private void initialEvents() {
        ivMenu.setOnClickListener(view -> drawerLayout.openDrawer(GravityCompat.START));

        fabFavourite.setOnClickListener(view -> {
            // if delete add contact button haven't close yet -> close it and favorite button
            if(fabAdd.getVisibility() == View.VISIBLE){
                closeAnimation(false, true, false, true);
            }

            fabMoreAction.setBackgroundTintList(ColorStateList.valueOf(Color.RED));

            rvContacts.setAdapter(favouriteContactAdapter);
            isShowingFavouriteContacts = true;
        });

        fabDeleteContact.setOnClickListener(view -> {
            // if user click on this button in second time, it will delete marked contacts

            if(isOnDeleteMode){
                DialogInterface.OnClickListener dialogClickListener = (dialogInterface, i) -> {
                    switch (i){
                        case DialogInterface.BUTTON_POSITIVE:
                            List<Integer> deleteContactIndexes = allContactAdapter.getDeleteContactIndexes();
                            viewModel.deleteContactsByIndexes(deleteContactIndexes);
                            allContactAdapter.notifyDataSetChanged();
                            deleteContactIndexes.clear();
                            break;
                        case  DialogInterface.BUTTON_NEGATIVE:
                    }
                };

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
                alertDialogBuilder.setMessage("Xóa những liên hệ này?")
                        .setPositiveButton("Đồng ý", dialogClickListener)
                        .setNegativeButton("Hủy", dialogClickListener)
                        .show();
            } else {
            // if delete add contact button haven't close yet -> close it and favorite button
                if(fabAdd.getVisibility() == View.VISIBLE) {
                    fabMoreAction.setBackgroundTintList(ColorStateList.valueOf(Color.RED));
                    closeAnimation(false, true, true, false);
                }

                Toast.makeText(MainActivity.this, "Chọn các liên hệ bạn muốn xóa", Toast.LENGTH_SHORT).show();
                isOnDeleteMode = true;
                allContactAdapter.setOnDeleteMode(isOnDeleteMode);
            }
        });

        fabMoreAction.setOnClickListener(view -> {
            fabMoreAction.setBackgroundTintList(ColorStateList.valueOf(DEFAULT_MOREACTION_BUTTON_COLOR));

            if(isShowingFavouriteContacts){
                rvContacts.setAdapter(allContactAdapter);
                closeAnimation(true, false, true, false);
                isShowingFavouriteContacts = false;
                isOpenedMoreAction = !isOpenedMoreAction;
                return;
            }

            if (isOnDeleteMode){
                closeAnimation(true, false, false, true);
                isOnDeleteMode = false;
                allContactAdapter.setOnDeleteMode(isOnDeleteMode);
                isOpenedMoreAction = !isOpenedMoreAction;
                return;
            }

            if(isShowingAddContactMode){
                closeAnimation(true, true, false, false);
                isShowingAddContactMode = false;
                isOpenedMoreAction = !isOpenedMoreAction;
                return;
            }

            if(isOpenedMoreAction){
                closeAnimation(true, true, true, true);
            } else{
                openAnimation();
            }
            isOpenedMoreAction = !isOpenedMoreAction;
        });

        fabAdd.setOnClickListener(view -> {
            isShowingAddContactMode = true;
            fabMoreAction.setBackgroundTintList(ColorStateList.valueOf(Color.RED));

            // if delete contact button haven't close yet -> close it and favorite button
            if(fabDeleteContact.getVisibility() == View.VISIBLE){
                closeAnimation(false, false, true, true);
            }

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
                    Contact contact = new Contact(viewModel.getAllContacts().getValue().size() + 1,
                            name, address, email, phone,
                            Contact.DEFAULT_CONTACT_AVATAR,
                            Contact.DEFAULT_CONTACT_BACKGROUND,
                            Contact.DEFAULT_CONTACT_FAVOURITE);
                    int i = viewModel.insertContactIntoContactListByOrder(contact, viewModel.getAllContacts().getValue());
                    viewModel.insertToDatabase(contact);

                    allContactAdapter.notifyDataSetChanged();
                    rvContacts.smoothScrollToPosition(i);
                    curHighlightIndex = i;
                    new Handler().postDelayed(() -> {
                        if(rvLayout.findViewByPosition(curHighlightIndex) != null){
                            Objects.requireNonNull(rvLayout.findViewByPosition(curHighlightIndex)).setBackgroundColor(highlightColor);
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
                if(!isOnDeleteMode){
                    for(int j = rvLayout.findFirstVisibleItemPosition(); j <= rvLayout.findLastVisibleItemPosition(); j++){
                        CardView tmpCardView = (CardView) rvLayout.findViewByPosition(j);
                        tmpCardView.setCardBackgroundColor(normalColor);
                    }
                    if(curHighlightIndex != -1 && rvLayout.findViewByPosition(curHighlightIndex) != null){
                        CardView tmpCardView = (CardView) rvLayout.findViewByPosition(curHighlightIndex);
                        tmpCardView.setCardBackgroundColor(highlightColor);
                    }
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
                                    CardView tmpCardView = (CardView) rvLayout.findViewByPosition(curHighlightIndex);
                                    tmpCardView.setCardBackgroundColor(highlightColor);
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

        navigationView.setNavigationItemSelectedListener(this);
    }

    // set animation for all floating action buttons when touch it to open
    private void openAnimation() {
        Animation openMoreActionAnimation = AnimationUtils.loadAnimation(this, R.anim.rotate_zoomin_open_anim);
        openMoreActionAnimation.setDuration(animationTime);
        fabMoreAction.startAnimation(openMoreActionAnimation);

        fabAdd.setVisibility(View.VISIBLE);
        Animation openAddAnimation = AnimationUtils.loadAnimation(this, R.anim.float_diagonal_up);
        openAddAnimation.setDuration(animationTime);
        fabAdd.startAnimation(openAddAnimation);
        fabAdd.setClickable(true);

        fabFavourite.setVisibility(View.VISIBLE);
        Animation openFavouriteAnimation = AnimationUtils.loadAnimation(this, R.anim.float_up);
        openFavouriteAnimation.setDuration(animationTime);
        fabFavourite.startAnimation(openFavouriteAnimation);
        fabFavourite.setClickable(true);

        fabDeleteContact.setVisibility(View.VISIBLE);
        Animation openDeleteAnimation = AnimationUtils.loadAnimation(this, R.anim.float_to_left);
        openDeleteAnimation.setDuration(animationTime);
        fabDeleteContact.startAnimation(openDeleteAnimation);
        fabDeleteContact.setClickable(true);
    }

    private void closeAnimation(boolean isCloseMoreActionButton,
                                boolean isCloseAddContactButton,
                                boolean isCloseFavouriteContactButton,
                                boolean isCloseDeleteContactButton) {

        if(isCloseMoreActionButton){
            Animation closeMoreActionAnimation = AnimationUtils.loadAnimation(this, R.anim.rotate_zoomout_close_anim);
            closeMoreActionAnimation.setDuration(animationTime);
            fabMoreAction.startAnimation(closeMoreActionAnimation);
        }

        if(isCloseAddContactButton){
            Animation closeAddAnimation = AnimationUtils.loadAnimation(this, R.anim.float_diagonal_down);
            closeAddAnimation.setDuration(animationTime);
            fabAdd.startAnimation(closeAddAnimation);
            fabAdd.setVisibility(View.GONE);
            fabAdd.setClickable(false);
        }

        if (isCloseFavouriteContactButton){
            Animation closeFavouriteAnimation = AnimationUtils.loadAnimation(this, R.anim.float_down);
            closeFavouriteAnimation.setDuration(animationTime);
            fabFavourite.startAnimation(closeFavouriteAnimation);
            fabFavourite.setVisibility(View.GONE);
            fabFavourite.setClickable(false);
        }

        if(isCloseDeleteContactButton){
            Animation closeDeleteAnimation = AnimationUtils.loadAnimation(this, R.anim.float_to_right);
            closeDeleteAnimation.setDuration(animationTime);
            fabDeleteContact.startAnimation(closeDeleteAnimation);
            fabDeleteContact.setVisibility(View.GONE);
            fabDeleteContact.setClickable(false);
        }
    }

    private void mapComponents() {
        viewModel = ViewModelProviders.of(MainActivity.this).get(MyViewModel.class);
        contacts = viewModel.getAllContacts().getValue();

        ivAvatar = findViewById(R.id.iv_avatar);
        svName = findViewById(R.id.sv_name);
        rvContacts = findViewById(R.id.rv_contacts);
        fabAdd = findViewById(R.id.fab_add);
        fabMoreAction = findViewById(R.id.fab_more_action);
        fabFavourite = findViewById(R.id.fab_favourite);
        fabDeleteContact = findViewById(R.id.fab_delete_contact);
        rvLayout = new LinearLayoutManagerWithSmoothScroller(this);
        rvContacts.setLayoutManager(rvLayout);
        allContactAdapter = new MyAdapter(contacts);
        favouriteContactAdapter = new MyAdapter(viewModel.getFavouriteContacts().getValue());
        rvContacts.setAdapter(allContactAdapter);

        ivMenu = findViewById(R.id.iv_menu);
        navigationView = findViewById(R.id.navigation_view);
        navigationView.setItemIconTintList(null);

        // default display
        fabMoreAction.setBackgroundTintList(ColorStateList.valueOf(DEFAULT_MOREACTION_BUTTON_COLOR));
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();
        switch (itemId){
            case R.id.item_home:
                drawerLayout.closeDrawer(GravityCompat.START);
                break;
            case R.id.item_personal:
                Intent intent = new Intent(MainActivity.this, MyAccountInformationActivity.class);
                startActivity(intent);
                break;
            case R.id.item_assistance:
                break;
            case R.id.item_version:
                break;
        }

        return false;
    }
}