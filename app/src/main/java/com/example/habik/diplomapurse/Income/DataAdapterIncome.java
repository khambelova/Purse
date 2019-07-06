package com.example.habik.diplomapurse.Income;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.habik.diplomapurse.Category;
import com.example.habik.diplomapurse.R;

import java.util.List;

public class DataAdapterIncome extends RecyclerView.Adapter<DataAdapterIncome.ViewHolder> {

    public interface OnItemClickListener{
        void onItemClick(Category item);
    }

    private OnItemClickListener listener;
    private LayoutInflater inflater;
    private List<Category> categoriesIncome;

    DataAdapterIncome(Context context, List<Category> categoriesIncome,OnItemClickListener listener) {
        this.categoriesIncome = categoriesIncome;
        this.inflater = LayoutInflater.from(context);
        this.listener = listener;
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        this.listener = listener;
    }

    @Override
    public DataAdapterIncome.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.list_item_category_income, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(DataAdapterIncome.ViewHolder holder, int position) {
        final Category category = categoriesIncome.get(position);
        holder.nameView.setText(category.getNameCategory());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClick(category);
            }
        });
    }

    @Override
    public int getItemCount() {
        return categoriesIncome.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        final TextView nameView;
            ViewHolder(View view){
            super(view);
            nameView = (TextView) view.findViewById(R.id.name_category_income);
        }
    }
}

