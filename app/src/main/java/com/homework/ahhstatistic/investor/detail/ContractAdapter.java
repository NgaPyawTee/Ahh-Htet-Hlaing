package com.homework.ahhstatistic.investor.detail;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;


import com.homework.ahhstatistic.R;
import com.homework.ahhstatistic.model.ExpiredDate;

import java.text.NumberFormat;
import java.util.List;

public class ContractAdapter extends RecyclerView.Adapter<ContractAdapter.ViewHolder> {
    Context context;
    List<ExpiredDate> list;
    Listener listener;

    public ContractAdapter(Context con, List<ExpiredDate> list) {
        this.context = con;
        this.list = list;
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
            View v = LayoutInflater.from(context).inflate(R.layout.contract_cardview_layout,parent,false);
            return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ExpiredDate item = list.get(position);

        NumberFormat numberFormat = NumberFormat.getInstance();
        holder.amount.setText(numberFormat.format(Integer.parseInt(item.getAmount())));
        holder.percent.setText(item.getPercent());
        holder.date.setText(item.getDate());

        if (item.getColor().equals("Cyan")){
            holder.cardView.setCardBackgroundColor(Color.parseColor("#00CCCC"));
        }else  if (item.getColor().equals("Blue")){
            holder.cardView.setCardBackgroundColor(Color.parseColor("#2196F3"));
        }else  if (item.getColor().equals("Pale Blue")){
            holder.cardView.setCardBackgroundColor(Color.parseColor("#4460a9"));
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView amount, percent, date;
        CardView cardView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            cardView = itemView.findViewById(R.id.contract_cardview);
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