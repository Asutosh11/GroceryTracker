package in.thoughtleaf.grocerytracker.ui.adapter;


import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.thoughtleaf.grocerytracker.R;
import in.thoughtleaf.grocerytracker.data.dao.BuyingListDAO;
import in.thoughtleaf.grocerytracker.data.dao.ItemsListDAO;
import in.thoughtleaf.grocerytracker.data.pojo.Product;

import java.util.ArrayList;

public class ProductRvAdapter extends RecyclerView.Adapter<ProductRvAdapter.Viewholder> {

    private ArrayList<Product> data;
    private ArrayList<Product> existingBuyingList = new ArrayList<>();
    private Context context;

    public ProductRvAdapter(ArrayList<Product> data, Context context) {
        this.data = data;
        this.context = context;
    }

    public static class Viewholder extends RecyclerView.ViewHolder {

        // 1. Declare your Views here

        public TextView imgMinus;
        public TextView tvQuantity, tvItemName;
        public TextView imgPlus;
        public ImageView btnAddToBuyingList;
        public ImageView btnDelete;
        public ImageView btnIcon;
        public Spinner sprUnit;


        public Viewholder(View itemView) {
            super(itemView);

            // 2. Define your Views here

            imgMinus = (TextView)itemView.findViewById(R.id.imgMinus);
            btnIcon = (ImageView)itemView.findViewById(R.id.iconImv);
            tvQuantity = (TextView)itemView.findViewById(R.id.tvQuantity);
            tvItemName = (TextView)itemView.findViewById(R.id.tvItemName);
            imgPlus = (TextView)itemView.findViewById(R.id.imgPlus);
            btnAddToBuyingList = (ImageView)itemView.findViewById(R.id.btn_add_to_buying_list);
            btnDelete = (ImageView)itemView.findViewById(R.id.btn_delete);
            sprUnit = (Spinner)itemView.findViewById(R.id.sprUnit);

        }
    }

    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.rv_item_product_list, parent, false);

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

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(context, R.array.quantity_unit_values, R.layout.spinner_item_dark);
        adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        holder.sprUnit.setAdapter(adapter);
        String[] unitArray = context.getResources().getStringArray(R.array.quantity_unit_values);

        for(int i = 0; i < unitArray.length; i++) {
            if(unitArray[i].equalsIgnoreCase(data.get(holder.getAdapterPosition()).getUnit())){
                holder.sprUnit.setSelection(i);
                break;
            }
        }

        setIcon(holder, data.get(holder.getAdapterPosition()).getListName());

        holder.tvQuantity.setText(data.get(holder.getAdapterPosition()).getQuantity());
        holder.tvItemName.setText(data.get(holder.getAdapterPosition()).getItem());

        holder.imgMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                double qty = Double.parseDouble(holder.tvQuantity.getText().toString());
                if(qty > 0){
                    qty = qty - 0.5;
                    holder.tvQuantity.setText(String.valueOf(qty));
                    ItemsListDAO.Companion.updateItemByName(holder.tvItemName.getText().toString(), String.valueOf(qty));
                }
            }
        });

        holder.imgPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                double qty = Double.parseDouble(holder.tvQuantity.getText().toString());
                qty = qty + 0.5;
                holder.tvQuantity.setText(String.valueOf(qty));
                ItemsListDAO.Companion.updateItemByName(holder.tvItemName.getText().toString(), String.valueOf(qty));
            }
        });

        holder.btnAddToBuyingList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                existingBuyingList = BuyingListDAO.Companion.getAllOfflineData();
                for(Product product : existingBuyingList){
                    if(product.getItem().equalsIgnoreCase(data.get(holder.getAdapterPosition()).getItem())){
                        Toast.makeText(context, R.string.item_already_added, Toast.LENGTH_LONG).show();
                        return;
                    }
                }
                BuyingListDAO.Companion.addProduct(data.get(holder.getAdapterPosition()));
                Toast.makeText(context, R.string.added_to_buying_list, Toast.LENGTH_LONG).show();

            }
        });

        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                new AlertDialog.Builder(context)
                        .setTitle("")
                        .setMessage(R.string.delete_grocey_confirmation)
                        .setCancelable(true)
                        .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        })
                        .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                ItemsListDAO.Companion.deleteItemByName(holder.tvItemName.getText().toString());
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

