package com.homework.ahhstatistic.investor.detail;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.homework.ahhstatistic.R;
import com.homework.ahhstatistic.model.ExpiredDate;

import java.text.NumberFormat;
import java.util.List;

public class ContractAdapter extends RecyclerView.Adapter<ContractAdapter.ViewHolder> {
    Context context;
    List<ExpiredDate> list;
    Listener listener;
    String contract;

    public ContractAdapter(Context con, List<ExpiredDate> list, String contract) {
        this.context = con;
        this.list = list;
        this.contract = contract;
    }

    public interface Listener{
        void onClick(int position);
    }

    public void setListener(Listener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (contract == "1st"){
            View v = LayoutInflater.from(context).inflate(R.layout.cyan_contract_card_view_layout,parent,false);
            return new ViewHolder(v);
        }else  if (contract == "2nd"){
            View v = LayoutInflater.from(context).inflate(R.layout.blue_contract_card_view_layout,parent,false);
            return new ViewHolder(v);
        }else  if (contract == "3rd"){
            View v = LayoutInflater.from(context).inflate(R.layout.pale_blue_contract_card_view_layout,parent,false);
            return new ViewHolder(v);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ExpiredDate item = list.get(position);

        NumberFormat numberFormat = NumberFormat.getInstance();
        holder.amount.setText(numberFormat.format(Integer.parseInt(item.getAmount())));
        holder.percent.setText(item.getPercent());
        holder.date.setText(item.getDate());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView amount, percent, date;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            amount = itemView.findViewById(R.id.contract_amount);
            percent = itemView.findViewById(R.id.contract_percent);
            date = itemView.findViewById(R.id.contract_date);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int pos = getAdapterPosition();
                    if (listener != null){
                        listener.onClick(pos);
                    }
                }
            });
        }
    }
}