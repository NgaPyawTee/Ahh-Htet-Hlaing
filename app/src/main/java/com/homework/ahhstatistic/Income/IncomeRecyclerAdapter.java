package com.homework.ahhstatistic.Income;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.homework.ahhstatistic.R;
import com.homework.ahhstatistic.model.Income;

import java.text.NumberFormat;
import java.util.List;

public class IncomeRecyclerAdapter extends RecyclerView.Adapter<IncomeRecyclerAdapter.IncomeViewHolder> {
    Context context;
    List<Income> list;
    Listener listener;
    NumberFormat nf = NumberFormat.getInstance();

    public IncomeRecyclerAdapter(Context context, List<Income> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public IncomeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.income_cardview_layout, parent,false);
        return new IncomeViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull IncomeViewHolder holder, int position) {
        Income item = list.get(position);

        holder.amount.setText(nf.format(Integer.parseInt(item.getIncome())) + " Ks");
        holder.date.setText(item.getDate());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public interface Listener{
        void onClick(int position);
    }

    public void setListener(Listener listener) {
        this.listener = listener;
    }

    public class IncomeViewHolder extends RecyclerView.ViewHolder {
        TextView amount, date;

        public IncomeViewHolder(@NonNull View itemView) {
            super(itemView);

            amount = itemView.findViewById(R.id.income_cardview_amount);
            date = itemView.findViewById(R.id.income_cardview_date);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listener != null){
                        listener.onClick(getAdapterPosition());
                    }
                }
            });
        }
    }
}
