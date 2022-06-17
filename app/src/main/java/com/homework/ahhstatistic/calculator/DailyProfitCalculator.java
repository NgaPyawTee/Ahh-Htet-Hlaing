package com.homework.ahhstatistic.calculator;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.view.WindowInsetsControllerCompat;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.homework.ahhstatistic.R;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class DailyProfitCalculator extends AppCompatActivity {
    Button btnCalculate;
    View resultLayout;
    ImageView startCalendar;
    DatePickerDialog.OnDateSetListener listener;
    TextView dayleftNumber, dayleftText, startDateText, startDate, endDate;
    EditText edtAmount, edtPercent;

    SimpleDateFormat dateFormat = new SimpleDateFormat("dd/M/yyyy");
    NumberFormat nf = NumberFormat.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily_profit_calculator);
        WindowCompat.setDecorFitsSystemWindows(getWindow(), false);

        Toolbar toolbar = findViewById(R.id.calculator_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_normal_back);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Current date and end month date show
        startDateText = findViewById(R.id.start_date_text);
        startDate = findViewById(R.id.start_date);
        endDate = findViewById(R.id.end_date);
        dayleftNumber = findViewById(R.id.day_left_number);
        dayleftText = findViewById(R.id.day_left_text);
        DefaultDateShow();

        edtAmount = findViewById(R.id.edt_amount);

        //get percent to multiply
        edtPercent = findViewById(R.id.edt_percent);

        //Button calculate click
        btnCalculate = findViewById(R.id.btn_calculate);
        btnCalculate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CalculateResult();
            }
        });

        //Image view calendar dialog pop up
        startCalendar = findViewById(R.id.start_calendar);
        startCalendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StartDateDialog();
            }
        });

        //DatePickerDialog Listener
        listener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;
                Calendar calendar = Calendar.getInstance();
                int currentYear = calendar.get(Calendar.YEAR);
                int currentMonth = calendar.get(Calendar.MONTH) + 1;
                int currentDay = calendar.get(Calendar.DAY_OF_MONTH);

                if (year == currentYear && month == currentMonth && day == currentDay) {
                    DefaultDateShow();
                    return;
                } else if (year == currentYear && month == currentMonth && day != currentDay) {
                    startDateText.setText("Start Date");
                    String chosenDate = day + "/" + month + "/" + year;
                    startDate.setText(chosenDate);

                    //get last day of the month
                    Calendar c = new GregorianCalendar(year, month, 0);
                    SimpleDateFormat dateFormat1 = new SimpleDateFormat("dd");
                    Date endDateDate = c.getTime();
                    int lastDay = Integer.parseInt(dateFormat1.format(endDateDate));

                    endDate.setText(lastDay + "/" + month + "/" + year);

                    //show date differnce
                    int dateDiff = lastDay - day;
                    if (dateDiff == 0) {
                        dayleftNumber.setText("You picked the last day of this month");
                        dayleftText.setText("");
                    } else if (dateDiff == 1) {
                        dayleftNumber.setText(String.valueOf(dateDiff));
                        dayleftText.setText(" day left to end" + " this " + "month");
                    } else {
                        dayleftNumber.setText(String.valueOf(dateDiff));
                        dayleftText.setText(" days left to end" + " this " + "month");
                    }
                } else {
                    startDateText.setText("Start Date");
                    String chosenDate = day + "/" + month + "/" + year;
                    startDate.setText(chosenDate);

                    //date difference calculation
                    Calendar c = new GregorianCalendar(year, month, 0);
                    SimpleDateFormat dateFormat1 = new SimpleDateFormat("dd");
                    Date endDateDate = c.getTime();
                    int lastDay = Integer.parseInt(dateFormat1.format(endDateDate));

                    endDate.setText(lastDay + "/" + month + "/" + year);

                    //show date differnce
                    int dateDiff = lastDay - day;
                    if (dateDiff == 0) {
                        dayleftNumber.setText("You picked the last day of the month");
                        dayleftText.setText("");
                    } else if (dateDiff == 1) {
                        dayleftNumber.setText(String.valueOf(dateDiff));
                        dayleftText.setText(" day left to end" + " the " + "month");
                    } else {
                        dayleftNumber.setText(String.valueOf(dateDiff));
                        dayleftText.setText(" days left to end" + " the " + "month");
                    }
                }
            }
        };

    }

    private void DefaultDateShow() {
        Calendar calendar = Calendar.getInstance();
        int currentYear = calendar.get(Calendar.YEAR);
        int currentMonth = calendar.get(Calendar.MONTH) + 1;
        int currentDay = calendar.get(Calendar.DAY_OF_MONTH);

        //today date
        startDateText.setText("Today Date");

        String currentDate = dateFormat.format(calendar.getTime());
        startDate.setText(currentDate);

        //get last day of the month
        Calendar c = new GregorianCalendar(currentYear, currentMonth, 0);
        int endMonthdate = c.get(Calendar.DAY_OF_MONTH);
        String endDateString = dateFormat.format(c.getTime());
        endDate.setText(endDateString);

        //show date differnce
        int dayDifference = endMonthdate - currentDay;
        if (dayDifference == 0) {
            dayleftNumber.setText("Today is the last day of this month");
            dayleftText.setText("");
        } else if (dayDifference == 1) {
            dayleftNumber.setText(String.valueOf(dayDifference));
            dayleftText.setText(" day left to end" + " this " + "month");
        } else {
            dayleftNumber.setText(String.valueOf(dayDifference));
            dayleftText.setText(" days left to end" + " this " + "month");
        }

    }

    private void CalculateResult() {
        resultLayout = findViewById(R.id.result_layout);
        TextView resultAmount = findViewById(R.id.result_amount);
        TextView resultDay = findViewById(R.id.result_day);

        String toggle = btnCalculate.getText().toString();
        String s = dayleftNumber.getText().toString();

        if (toggle.equals("calculate")) {
            if (s.equals("You picked the last day of this month") | s.equals("You picked the last day of the month") | s.equals("Today is the last day of this month")) {
                Toast.makeText(DailyProfitCalculator.this, "No day left to calculate", Toast.LENGTH_SHORT).show();
                return;
            } else if (edtAmount.length() == 0) {
                Toast.makeText(DailyProfitCalculator.this, "Insert any amount to calculate", Toast.LENGTH_SHORT).show();
                return;
            } else if (edtPercent.length() == 0) {
                Toast.makeText(DailyProfitCalculator.this, "Insert percentage", Toast.LENGTH_SHORT).show();
                return;
            } else {
                btnCalculate.setText("hide");
                resultLayout.setVisibility(View.VISIBLE);

                int amount = Integer.parseInt(edtAmount.getText().toString());

                Calculation(amount, resultAmount, resultDay);
            }
        } else {
            btnCalculate.setText("calculate");
            resultLayout.setVisibility(View.GONE);
        }
    }

    private void Calculation(int amount, TextView resultAmount, TextView resultDay) {
        int getMultiplier = Integer.parseInt(edtPercent.getText().toString());

        int dayLeftMultiplier = Integer.parseInt(dayleftNumber.getText().toString());

        //get only day from end date
        String s = endDate.getText().toString();
        try {
            Date date = dateFormat.parse(s);
            int lastDayMultiplier = date.getDate();

            //calculate daily profit
            int oneMonthProfit = (int) (amount * getMultiplier * 0.01);
            int dailyProfit = ((oneMonthProfit * dayLeftMultiplier) / lastDayMultiplier);

            resultAmount.setText(nf.format(dailyProfit) + " Ks");

            if (dayLeftMultiplier == 1) {
                resultDay.setText(dayLeftMultiplier + " Day");
            } else {
                resultDay.setText(dayLeftMultiplier + " Days");
            }


        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    private void StartDateDialog() {
        Calendar calendar = Calendar.getInstance();
        int startYear = calendar.get(Calendar.YEAR);
        int startMonth = calendar.get(Calendar.MONTH);
        int startDay = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog dialog = new DatePickerDialog(DailyProfitCalculator.this, R.style.DialogTheme2, listener, startYear, startMonth, startDay);
        dialog.show();
    }
}