package in.thoughtleaf.grocerytracker.ui.adapter;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.RecyclerView;

import com.highloop.homemaker.R;
import in.thoughtleaf.grocerytracker.data.event.NewCategoryAddedEvent;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

public class CategoriesRvAdapter extends RecyclerView.Adapter<CategoriesRvAdapter.Viewholder> {

    private ArrayList<String> data;
    private String model;
    private DialogFragment fragment;

    public CategoriesRvAdapter(ArrayList<String> data, DialogFragment fragment) {
        this.data = data;
        this.fragment = fragment;
    }

    public static class Viewholder extends RecyclerView.ViewHolder {

        // 1. Declare your Views here

        public TextView tvCategoryName;


        public Viewholder(View itemView) {
            super(itemView);

            // 2. Define your Views here

            tvCategoryName = (TextView)itemView.findViewById(R.id.tv_category_name);

        }
    }

    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.rv_item_categories, parent, false);

        return new Viewholder(itemView);
    }

    @Override
    public void onBindViewHolder(Viewholder holder, int position) {

        model = data.get(position);

        holder.tvCategoryName.setText(model);

        holder.tvCategoryName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EventBus.getDefault().postSticky(new NewCategoryAddedEvent(holder.tvCategoryName.getText().toString()));
                fragment.dismiss();
            }
        });

    }

    @Override
    public int getItemCount() {
        return data.size();
    }


}

