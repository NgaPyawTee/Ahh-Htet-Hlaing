package com.homework.ahhstatistic.profit;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.homework.ahhstatistic.R;
import com.homework.ahhstatistic.model.Investor;

import java.util.List;

public class ProfitRecyclerAdapter extends RecyclerView.Adapter<ProfitViewHolder> {
    Context context;
    List<Investor> investorList;
    Listener listener;

    public interface Listener{
        void onClick(int pos);
    }

    public void setListener(Listener listener) {
        this.listener = listener;
    }

    public ProfitRecyclerAdapter(Context context, List<Investor> investorList) {
        this.context = context;
        this.investorList = investorList;
    }

    @NonNull
    @Override
    public ProfitViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.profit_cardview_layout,parent,false);
        return new ProfitViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ProfitViewHolder holder, int position) {
        holder.bind(investorList,listener,position);
    }

    @Override
    public int getItemCount() {
        return investorList.size();
    }
}
