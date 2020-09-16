package com.example.contactapp.supportclass;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.contactapp.R;
import com.example.contactapp.entities.Contact;
import com.example.contactapp.view.DetailContactActivity;
import com.example.contactapp.view.MainActivity;

import java.util.ArrayList;
import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {
    private List<CardView> cardViews;
    private List<Contact> contacts;
    private Contact curTouchedContact;

    private List<Integer> deleteContactIndexes;
    private boolean isOnDeleteMode;

    private final int HIGHLIGHT_DELETE_COLOR = Color.rgb(255, 153, 153);
    private final int NORMAL_COLOR = Color.WHITE;

    public MyAdapter(List<Contact> contacts) {
        this.cardViews = new ArrayList<>();
        this.contacts = contacts;
        isOnDeleteMode = false;
        deleteContactIndexes = new ArrayList<>();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public View view;
        public TextView tvAcc;
        public ImageView ivAvatar;
        public Context context;
        public CardView cardView;

        public MyViewHolder(@NonNull View itemView, Context context) {
            super(itemView);
            this.view = itemView;
            this.context = context;

            tvAcc = itemView.findViewById(R.id.tv_name);
            ivAvatar = itemView.findViewById(R.id.iv_avatar);
            cardView = itemView.findViewById(R.id.cv_item);

            view.setOnClickListener(view -> {
                curTouchedContact = contacts.get(getLayoutPosition());
                if(!isOnDeleteMode){
                    // open detail contact activity and send a contact, it can be edited after
                    MainActivity mainActivity = (MainActivity) context;
                    int pos = getLayoutPosition();

                    Intent intent = new Intent(mainActivity, DetailContactActivity.class);
                    intent.putExtra("contact", contacts.get(pos));
                    mainActivity.startActivityForResult(intent, 123);
                } else {
                    Integer position = getLayoutPosition();
                    /* contact selected will be change background color to prepare for deleting soon */
                    int itemColor = cardView.getCardBackgroundColor().getDefaultColor();
                    if(itemColor != HIGHLIGHT_DELETE_COLOR){
                        cardView.setCardBackgroundColor(HIGHLIGHT_DELETE_COLOR);
                        deleteContactIndexes.add(position);
                    } else {
                        System.out.println("hightlight-------------------");
                        cardView.setCardBackgroundColor(NORMAL_COLOR);
                        deleteContactIndexes.remove(position);
                    }
                }
            });
        }
    }

    public Contact getCurTouchedContact(){
        return curTouchedContact;
    }

    public List<Integer> getDeleteContactIndexes(){
        return this.deleteContactIndexes;
    }

    @NonNull
    @Override
    public MyAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.contact_item, parent, false);
        return new MyViewHolder(v, parent.getContext());
    }

    public void setOnDeleteMode(boolean isOnDeleteMode){
        this.isOnDeleteMode = isOnDeleteMode;
        if(!isOnDeleteMode){
            for(CardView cardView : cardViews){
                cardView.setCardBackgroundColor(NORMAL_COLOR);
            }
            deleteContactIndexes.clear();
        }
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        if(!cardViews.contains(holder.cardView)){
            cardViews.add(holder.cardView);
        }

        holder.tvAcc.setText(contacts.get(position).getName());
        int avatarId = holder.context.getResources()
                .getIdentifier("com.example.contactapp:drawable/" + contacts.get(position).getAvatar(), null, null);
        holder.ivAvatar.setImageResource(avatarId);

        if(isOnDeleteMode && deleteContactIndexes.contains(position)){
            holder.cardView.setBackgroundColor(HIGHLIGHT_DELETE_COLOR);
        }
    }

    @Override
    public int getItemCount() {
        return contacts.size();
    }

    @Override
    public void onViewAttachedToWindow(@NonNull MyViewHolder holder) {
        holder.setIsRecyclable(false);
    }

    @Override
    public void onViewDetachedFromWindow(@NonNull MyViewHolder holder) {
        holder.setIsRecyclable(false);
    }
}
