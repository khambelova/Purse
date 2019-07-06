package com.example.habik.diplomapurse.AllIncome;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.habik.diplomapurse.R;

import java.util.List;

public class DataAdapterIncomeItem extends RecyclerView.Adapter<DataAdapterIncomeItem.ViewHolder> {

    public interface OnItemClickListener{
        void onItemClick(AllIncomeInCategory item);
    }

    private OnItemClickListener listener;
    private LayoutInflater inflater;
    private List<AllIncomeInCategory> itemList;

    DataAdapterIncomeItem(Context context, List<AllIncomeInCategory> itemList, OnItemClickListener listener) {
        this.itemList = itemList;
        this.inflater = LayoutInflater.from(context);
        this.listener = listener;
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        this.listener = listener;
    }

    @Override
    public DataAdapterIncomeItem.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(DataAdapterIncomeItem.ViewHolder holder, int position) {
        final AllIncomeInCategory allIncomeInCategory = itemList.get(position);
        holder.dateView.setText(allIncomeInCategory.getDate());
        holder.sumView.setText(String.valueOf(allIncomeInCategory.getSum()));
        holder.currencyView.setText(allIncomeInCategory.getCurrency());
        holder.commentView.setText(allIncomeInCategory.getComment());


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClick(allIncomeInCategory);
            }
        });
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        final TextView dateView;
        final TextView sumView;
        final TextView commentView;
        final TextView currencyView;

            ViewHolder(View view){
            super(view);
            dateView = (TextView) view.findViewById(R.id.date_item);
            sumView = (TextView) view.findViewById(R.id.sum_item);
            currencyView = (TextView) view.findViewById(R.id.currency_item);
            commentView = (TextView) view.findViewById(R.id.comment_item);

        }
    }
}

