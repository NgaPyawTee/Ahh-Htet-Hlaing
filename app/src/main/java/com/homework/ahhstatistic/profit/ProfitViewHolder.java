package com.homework.ahhstatistic.profit;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.homework.ahhstatistic.R;
import com.homework.ahhstatistic.model.Investor;

import org.joda.time.Period;
import org.joda.time.PeriodType;

import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class ProfitViewHolder extends RecyclerView.ViewHolder {
    TextView count, name, amount, companyID;
    int amount1, percent1, amount2, percent2, amount3, percent3, cashBonus, dailyProfit;
    String date1, date2, date3;

    public ProfitViewHolder(@NonNull View itemView) {
        super(itemView);

        count = itemView.findViewById(R.id.profit_count);
        name = itemView.findViewById(R.id.profit_detail_1st_profit);
        companyID = itemView.findViewById(R.id.profit_companyID);
        amount = itemView.findViewById(R.id.profit_amount);
    }

    public void bind(List<Investor> investorList, ProfitRecyclerAdapter.Listener listener, int position) {
        Investor item = investorList.get(position);

        amount1 = Integer.parseInt(item.getAmount811Cash()) + Integer.parseInt(item.getAmount811Banking());
        amount2 = Integer.parseInt(item.getAmount58Cash()) + Integer.parseInt(item.getAmount58Banking());
        amount3 = Integer.parseInt(item.getAmount456Cash()) + Integer.parseInt(item.getAmount456Banking());
        cashBonus = Integer.parseInt(item.getCashBonus());
        dailyProfit = Integer.parseInt(item.getDailyProfit());

        if (item.getPercent811().equals("")) {
            percent1 = 0;
        } else {
            percent1 = Integer.parseInt(item.getPercent811());
        }

        if (item.getPercent58().equals("")) {
            percent2 = 0;
        } else {
            percent2 = Integer.parseInt(item.getPercent58());
        }

        if (item.getPercent456().equals("")) {
            percent3 = 0;
        } else {
            percent3 = Integer.parseInt(item.getPercent456());
        }

        date1 = item.getDate811();
        date2 = item.getDate58();
        date3 = item.getDate456();

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener != null) {
                    listener.onClick(position);
                }
            }
        });

        count.setText(String.valueOf(position + 1));
        name.setText(item.getName());
        companyID.setText(item.getCompanyID());

        CalculateProfit(amount1, amount2, amount3, percent1, percent2, percent3, date1, date2, date3, cashBonus, dailyProfit);
    }

    private void CalculateProfit(int amount1, int amount2, int amount3, int percent1, int percent2, int percent3,
                                 String date1, String date2, String date3, int cashBonus, int dailyProfit) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        int dateDiff1 = 0, dateDiff2 = 0, dateDiff3 = 0;
        int monthly1 = 0, monthly2 = 0, monthly3 = 0;
        NumberFormat nf = NumberFormat.getInstance();

        Calendar c = Calendar.getInstance();
        String strCurrentDate = sdf.format(c.getTime());

        try {
            Date currentDate = sdf.parse(strCurrentDate);
            int currentYear = currentDate.getYear();
            int currentMonth = currentDate.getMonth();
            long currentLong = currentDate.getTime();

            if (date1.equals("")) {
                monthly1 = 0;
            } else {
                Date date811 = sdf.parse(date1);
                long long1 = date811.getTime();
                int year811 = date811.getYear();

                //Check year for 1st
                if (currentYear == year811) {
                    dateDiff1 = currentMonth - date811.getMonth();
                } else {
                    Period period = new Period(long1, currentLong, PeriodType.yearMonthDay());
                    dateDiff1 = period.getMonths();
                }

                if (dateDiff1 >= 0 && dateDiff1 < 4) {
                    monthly1 = (int) (amount1 * percent1 * 0.01) + cashBonus;
                } else {
                    monthly1 = 0;
                }
            }

            if (date2.equals("")) {
                monthly2 = 0;
            } else {
                Date date58 = sdf.parse(date2);
                long long2 = date58.getTime();
                int year58 = date58.getYear();

                //Check year for 2nd
                if (currentYear == year58) {
                    dateDiff2 = currentMonth - date58.getMonth();
                } else {
                    Period period = new Period(long2, currentLong, PeriodType.yearMonthDay());
                    dateDiff2 = period.getMonths();
                }

                if (dateDiff2 >= 0 && dateDiff2 < 4) {
                    monthly2 = (int) (amount2 * percent2 * 0.01);
                } else {
                    monthly2 = 0;
                }
            }

            if (date3.equals("")) {
                monthly3 = 0;
            } else {
                Date date456 = sdf.parse(date3);
                long long3 = date456.getTime();
                int year456 = date456.getYear();

                //Check year for 3rd
                if (currentYear == year456) {
                    dateDiff3 = currentMonth - date456.getMonth();
                } else {
                    Period period = new Period(long3, currentLong, PeriodType.yearMonthDay());
                    dateDiff3 = period.getMonths();
                }

                if (dateDiff3 >= 0 && dateDiff3 < 4) {
                    monthly3 = (int) (amount3 * percent3 * 0.01);
                } else {
                    monthly3 = 0;
                }
            }

            int monthlyProfit = monthly1 + monthly2 + monthly3 + dailyProfit;
            amount.setText(nf.format(monthlyProfit) + " Ks");

        } catch (ParseException e) {
            e.printStackTrace();
        }

    }

}
