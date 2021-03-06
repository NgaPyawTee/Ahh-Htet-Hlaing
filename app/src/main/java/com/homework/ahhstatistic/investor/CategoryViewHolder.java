package com.homework.ahhstatistic.investor;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.homework.ahhstatistic.R;
import com.homework.ahhstatistic.investor.deleted.DeletedCategoryRecyclerAdapter;
import com.homework.ahhstatistic.model.Investor;

import java.text.NumberFormat;
import java.util.List;

public class CategoryViewHolder extends RecyclerView.ViewHolder {
    TextView name, amount, companyID, count;
    int amount1, amount2, amount3;

    public CategoryViewHolder(@NonNull View itemView) {
        super(itemView);

        name = itemView.findViewById(R.id.investor_name);
        amount = itemView.findViewById(R.id.investor_total_amount);
        companyID = itemView.findViewById(R.id.investor_company_id);
        count = itemView.findViewById(R.id.investor_count);
    }

    public void bind(List<Investor> list, int position, CategoryRecyclerAdapter.Listener listener) {
        Investor item = list.get(position);

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener != null) {
                    listener.onClick(position);
                }
            }
        });

        count.setText(String.valueOf(position+1));
        name.setText(item.getName());
        companyID.setText(item.getCompanyID());

        amount1 = Integer.parseInt(item.getAmount811Cash()) + Integer.parseInt(item.getAmount811Banking());
        amount2 = Integer.parseInt(item.getAmount58Cash()) + Integer.parseInt(item.getAmount58Banking());
        amount3 = Integer.parseInt(item.getAmount456Cash()) + Integer.parseInt(item.getAmount456Banking());

        CalculateTotalAmount(amount1, amount2, amount3);
    }

    private void CalculateTotalAmount(int a, int b, int c) {
        int amountTotal = a + b + c;

        NumberFormat numberFormat = NumberFormat.getInstance();
        amount.setText(numberFormat.format(amountTotal) + " Ks");
    }
}