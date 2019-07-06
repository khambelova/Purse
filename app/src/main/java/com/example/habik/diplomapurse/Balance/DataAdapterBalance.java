package com.example.habik.diplomapurse.Balance;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.habik.diplomapurse.R;

import java.util.List;

public class DataAdapterBalance extends RecyclerView.Adapter<DataAdapterBalance.ViewHolder> {

    private LayoutInflater inflater;
    private List<Balance> balanceList;

    DataAdapterBalance(Context context, List<Balance> balanceList) {
        this.balanceList = balanceList;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public DataAdapterBalance.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.list_item_balance, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(DataAdapterBalance.ViewHolder holder, int position) {
        final Balance balance = balanceList.get(position);
        holder.monthView.setText(balance.getMonth());
        holder.sumView.setText(String.valueOf(balance.getSum()));
    }

    @Override
    public int getItemCount() {
        return balanceList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        final TextView monthView;
        final TextView sumView;

            ViewHolder(View view){
            super(view);
            monthView = (TextView) view.findViewById(R.id.month);
            sumView = (TextView)view.findViewById(R.id.sum_for_month);
        }
    }
}

