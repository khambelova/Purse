package com.example.habik.diplomapurse.AllIncome;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.habik.diplomapurse.R;

import java.util.List;

public class DataAdapterAllIncome extends RecyclerView.Adapter<DataAdapterAllIncome.ViewHolder> {
    public interface OnItemClickListener {
        void onItemClick(AllIncome item);
    }

    private OnItemClickListener listener;
    private LayoutInflater inflater;
    private List<AllIncome> allIncomeList;

    public DataAdapterAllIncome(Context context, List<AllIncome> allIncomeList, OnItemClickListener listener) {
        this.inflater = LayoutInflater.from(context);
        this.allIncomeList = allIncomeList;
        this.listener = listener;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    @Override
    public DataAdapterAllIncome.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.list_item_all_income, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(DataAdapterAllIncome.ViewHolder viewHolder, int position) {
        final AllIncome allIncome = allIncomeList.get(position);
        viewHolder.categoryName.setText(allIncome.getCategory_name());
        viewHolder.categorySum.setText(String.valueOf(allIncome.getSum_for_category()));
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClick(allIncome);
            }
        });
    }

    @Override
    public int getItemCount() {
        return allIncomeList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        final TextView categoryName;
        final TextView categorySum;

        public ViewHolder(View itemView) {
            super(itemView);
            categoryName = (TextView) itemView.findViewById(R.id.name_category_all_income);
            categorySum = (TextView) itemView.findViewById(R.id.sum_category_all_income);
        }
    }
}