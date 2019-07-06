package com.example.habik.diplomapurse.AllOutcome;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.habik.diplomapurse.AllIncome.AllIncome;
import com.example.habik.diplomapurse.AllIncome.DataAdapterAllIncome;
import com.example.habik.diplomapurse.R;

import java.util.List;

public class DataAdapterAllOutcome extends RecyclerView.Adapter<DataAdapterAllOutcome.ViewHolder> {
    public interface OnItemClickListener {
        void onItemClick(AllOutcome item);
    }

    private OnItemClickListener listener;
    private LayoutInflater inflater;
    private List<AllOutcome> allOutcomeList;

    public DataAdapterAllOutcome(Context context, List<AllOutcome> allOutcomeList, OnItemClickListener listener) {
        this.inflater = LayoutInflater.from(context);
        this.allOutcomeList = allOutcomeList;
        this.listener = listener;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    @Override
    public DataAdapterAllOutcome.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.list_item_all_outcome, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(DataAdapterAllOutcome.ViewHolder viewHolder, int position) {
        final AllOutcome allOutcome = allOutcomeList.get(position);
        viewHolder.categoryName.setText(allOutcome.getCategory_name());
        viewHolder.categorySum.setText(String.valueOf(allOutcome.getSum_for_category()));
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClick(allOutcome);
            }
        });
    }

    @Override
    public int getItemCount() {
        return allOutcomeList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        final TextView categoryName;
        final TextView categorySum;

        public ViewHolder(View itemView) {
            super(itemView);
            categoryName = (TextView) itemView.findViewById(R.id.name_category_all_outcome);
            categorySum = (TextView) itemView.findViewById(R.id.sum_category_all_outcome);
        }
    }
}