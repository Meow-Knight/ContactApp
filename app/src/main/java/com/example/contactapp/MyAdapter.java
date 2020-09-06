package com.example.contactapp;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {
    private List<Contact> accountNames;

    public MyAdapter(List<Contact> accountNames) {
        this.accountNames = accountNames;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public View view;
        public TextView tvAcc;
        public Context context;

        public MyViewHolder(@NonNull View itemView, Context context) {
            super(itemView);
            this.view = itemView;
            this.context = context;
            tvAcc = this.view.findViewById(R.id.tv_name);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int pos = getLayoutPosition();
                    Intent intent = new Intent(context, DetailContact.class);
                    intent.putExtra("name", accountNames.get(pos).getName());
                    intent.putExtra("phone", accountNames.get(pos).getPhone());
                    intent.putExtra("address", accountNames.get(pos).getAddress());
                    intent.putExtra("email", accountNames.get(pos).getEmail());

                    context.startActivity(intent);
                }
            });
        }
    }

    @NonNull
    @Override
    public MyAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = (View) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.account_item, parent, false);
        MyViewHolder vh = new MyViewHolder(v, parent.getContext());
        return vh;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.tvAcc.setText(accountNames.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return accountNames.size();
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
