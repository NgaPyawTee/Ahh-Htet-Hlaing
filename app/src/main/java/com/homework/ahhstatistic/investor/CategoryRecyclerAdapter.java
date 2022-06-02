package com.homework.ahhstatistic.investor;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.homework.ahhstatistic.R;
import com.homework.ahhstatistic.investor.CategoryViewHolder;
import com.homework.ahhstatistic.investor.deleted.DeletedCategoryRecyclerAdapter;
import com.homework.ahhstatistic.investor.deleted.DeletedCategoryViewHolder;
import com.homework.ahhstatistic.model.Investor;

import java.util.List;

public class CategoryRecyclerAdapter extends RecyclerView.Adapter<CategoryViewHolder> {
    Context context;
    List<Investor> investorList;
    Listener listener;

    public CategoryRecyclerAdapter(Context context, List<Investor> investorList) {
        this.context = context;
        this.investorList = investorList;
    }

    public interface Listener{
        void onClick(int pos);
    }

    public void setListener(Listener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.investor_cardview_layout, parent, false);
        return new CategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
        holder.bind(investorList, position, listener);
    }

    @Override
    public int getItemCount() {
        return investorList.size();
    }
}
