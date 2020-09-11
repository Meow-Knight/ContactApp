package com.example.contactapp.supportclass;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.contactapp.R;
import com.example.contactapp.entities.Contact;
import com.example.contactapp.view.DetailContactActivity;

import java.util.ArrayList;
import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {
    private List<CardView> cardViews;
    private List<Contact> contacts;

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
        public Context context;
        public CardView cardView;

        public MyViewHolder(@NonNull View itemView, Context context) {
            super(itemView);
            this.view = itemView;
            this.context = context;

            tvAcc = itemView.findViewById(R.id.tv_name);
            cardView = itemView.findViewById(R.id.cv_item);

            view.setOnClickListener(view -> {
                if(!isOnDeleteMode){
                    int pos = getLayoutPosition();
                    Intent intent = new Intent(context, DetailContactActivity.class);
                    intent.putExtra("contact", contacts.get(pos));

                    context.startActivity(intent);
                } else {
                    Integer position = getLayoutPosition();
                    /* contact selected will be change background color to prepare for deleting soon */
                    Drawable itemDrawable = itemView.getBackground();
                    int itemColor = -1;
                    if(itemDrawable instanceof ColorDrawable){
                        itemColor = ((ColorDrawable) itemDrawable).getColor();
                    }
                    if(itemColor != HIGHLIGHT_DELETE_COLOR){
                        cardView.setBackgroundColor(HIGHLIGHT_DELETE_COLOR);
                        deleteContactIndexes.add(position);
                    } else {
                        cardView.setBackgroundColor(NORMAL_COLOR);
                        deleteContactIndexes.remove(position);
                    }
                }
            });
        }
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
            for(Integer index : deleteContactIndexes){
                System.out.println(index + "))))");
                cardViews.get(index).setBackgroundColor(NORMAL_COLOR);
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
