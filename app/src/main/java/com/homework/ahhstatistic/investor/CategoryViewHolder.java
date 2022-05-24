package com.homework.ahhstatistic.investor;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.homework.ahhstatistic.R;
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

        String stramount1 = item.getAmount811();
        String stramount2 = item.getAmount58();
        String stramount3 = item.getAmount456();

        amount1 = Integer.parseInt(stramount1);
        amount2 = Integer.parseInt(stramount2);
        amount3 = Integer.parseInt(stramount3);

        CalculateTotalAmount(amount1, amount2, amount3);
    }

    private void CalculateTotalAmount(int a, int b, int c) {
        int amountTotal = a + b + c;

        NumberFormat numberFormat = NumberFormat.getInstance();
        amount.setText(numberFormat.format(amountTotal) + " Ks");
    }
}