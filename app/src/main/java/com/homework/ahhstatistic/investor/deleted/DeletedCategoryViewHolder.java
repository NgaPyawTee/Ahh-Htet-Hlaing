package com.homework.ahhstatistic.investor.deleted;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.homework.ahhstatistic.R;
import com.homework.ahhstatistic.model.Investor;

import java.text.NumberFormat;
import java.util.List;

public class DeletedCategoryViewHolder extends RecyclerView.ViewHolder {
    TextView name, amount, companyID, count;
    int amount1, amount2, amount3;

    public DeletedCategoryViewHolder(@NonNull View itemView) {
        super(itemView);

        name = itemView.findViewById(R.id.deleted_investor_name);
        amount = itemView.findViewById(R.id.deleted_investor_total_amount);
        companyID = itemView.findViewById(R.id.deleted_investor_company_id);
        count = itemView.findViewById(R.id.deleted_investor_count);
    }

    public void bind(List<Investor> list, int position, DeletedCategoryRecyclerAdapter.Listener listener) {
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
        NumberFormat numberFormat = NumberFormat.getInstance();
        amount.setText(numberFormat.format(Integer.parseInt(item.getPreProfit())) + " Ks");
    }

}
