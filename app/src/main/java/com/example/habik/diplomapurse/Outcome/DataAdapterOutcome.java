package com.example.habik.diplomapurse.Outcome;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.habik.diplomapurse.Category;
import com.example.habik.diplomapurse.R;

import java.util.List;

public class DataAdapterOutcome extends RecyclerView.Adapter<DataAdapterOutcome.ViewHolder> {

    public interface OnItemClickListener{
        void onItemClick(Category item);
    }

    private OnItemClickListener listener;
    private LayoutInflater inflater;
    private List<Category> categoriesOutcome;

    public DataAdapterOutcome(Context context, List<Category> categoriesOutcome, OnItemClickListener listener) {
        this.categoriesOutcome = categoriesOutcome;
        this.inflater = LayoutInflater.from(context);
        this.listener = listener;
    }

    public void setOnItemClickListener(OnItemClickListener outcome_listener){
        outcome_listener = listener;
    }

    @Override
    public DataAdapterOutcome.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.list_item_category_outcome, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(DataAdapterOutcome.ViewHolder holder, int position) {
        final Category category = categoriesOutcome.get(position);
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
        return categoriesOutcome.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        final TextView nameView;
            ViewHolder(View view){
            super(view);
            nameView = (TextView) view.findViewById(R.id.name_category_outcome);
        }
    }
}

