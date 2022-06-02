package com.homework.ahhstatistic.investor.deleted;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.homework.ahhstatistic.R;
import com.homework.ahhstatistic.model.Investor;

import java.util.List;

public class DeletedCategoryRecyclerAdapter extends RecyclerView.Adapter<DeletedCategoryViewHolder> {
    Context context;
    List<Investor> investorList;
    Listener listener;

    public interface Listener {
        void onClick(int position);
    }

    public void setListener(Listener listener) {
        this.listener = listener;
    }

    public DeletedCategoryRecyclerAdapter(Context context, List<Investor> investorList) {
        this.context = context;
        this.investorList = investorList;
    }

    @NonNull
    @Override
    public DeletedCategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.deleted_investor_cardview_layout, parent, false);
        return new DeletedCategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DeletedCategoryViewHolder holder, int position) {
        holder.bind(investorList, position, listener);
    }

    @Override
    public int getItemCount() {
        return investorList.size();
    }
}
