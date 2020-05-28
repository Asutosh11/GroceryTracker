package com.highloop.homemaker.ui.adapter;


import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.recyclerview.widget.RecyclerView;

import com.highloop.homemaker.R;
import com.highloop.homemaker.data.dao.BuyingListDAO;
import com.highloop.homemaker.data.dao.ItemsListDAO;
import com.highloop.homemaker.data.pojo.Product;

import java.util.ArrayList;

public class BuyingListRvAdapter extends RecyclerView.Adapter<BuyingListRvAdapter.Viewholder> {

    private ArrayList<Product> data;
    private Context context;

    public BuyingListRvAdapter(ArrayList<Product> data, Context context) {
        this.data = data;
        this.context = context;
    }

    public static class Viewholder extends RecyclerView.ViewHolder {

        // 1. Declare your Views here

        public TextView tvItemName;
        public ImageView btnDelete;
        public ImageView btnIcon;


        public Viewholder(View itemView) {
            super(itemView);

            // 2. Define your Views here

            btnIcon = (ImageView)itemView.findViewById(R.id.iconImv);
            tvItemName = (TextView)itemView.findViewById(R.id.tvItemName);
            btnDelete = (ImageView)itemView.findViewById(R.id.btn_delete);

        }
    }

    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.rv_item_buying_list, parent, false);

        return new Viewholder(itemView);
    }

    void setIcon(Viewholder holder, String category){
        switch(category) {
            case "Essential":
                holder.btnIcon.setBackgroundResource(R.drawable.ic_essential);
                break;
            case "Groceries":
                holder.btnIcon.setBackgroundResource(R.drawable.ic_groceries);
                break;
            case "Masala":
                holder.btnIcon.setBackgroundResource(R.drawable.ic_masala);
                break;
            case "Beverages":
                holder.btnIcon.setBackgroundResource(R.drawable.ic_beverages);
                break;
            case "Cleaning Products":
                holder.btnIcon.setBackgroundResource(R.drawable.ic_cleaning_products);
                break;
            case "Dry Fruits":
                holder.btnIcon.setBackgroundResource(R.drawable.ic_dry_fruits);
                break;
            case "Vegetables":
                holder.btnIcon.setBackgroundResource(R.drawable.ic_vegetables);
                break;
            default:
                holder.btnIcon.setBackgroundResource(R.mipmap.ic_launcher);
        }
    }

    @Override
    public void onBindViewHolder(Viewholder holder, int position) {

        setIcon(holder, data.get(holder.getAdapterPosition()).getListName());

        holder.tvItemName.setText(data.get(holder.getAdapterPosition()).getItem());

        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                new AlertDialog.Builder(context)
                        .setTitle("")
                        .setMessage("Are you sure you want to delete this item from your buying list?")
                        .setCancelable(true)
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        })
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                BuyingListDAO.Companion.deleteItemByName(holder.tvItemName.getText().toString());
                                data.remove(data.get(holder.getAdapterPosition()));
                                notifyDataSetChanged();
                            }
                        }).show();

            }
        });

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

}

