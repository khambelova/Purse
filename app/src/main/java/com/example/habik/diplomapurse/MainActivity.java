package com.example.habik.diplomapurse;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.habik.diplomapurse.AllIncome.AllIncomeActivity;
import com.example.habik.diplomapurse.AllOutcome.AllOutcomeActivity;
import com.example.habik.diplomapurse.Balance.BalanceActivity;
import com.example.habik.diplomapurse.Income.IncomeActivity;
import com.example.habik.diplomapurse.Outcome.OutcomeActivity;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    TextView currencyBalance, currencyIncome, currencyOutcome, allTimeBalanceSum, incomeSumToday,
            incomeSumWeek, incomeSumMonth, outcomeSumToday, outcomeSumWeek, outcomeSumMonth;

    ImageButton incomeImgBtn, outcomeImgBtn, balanceImgBtn, allIncomeImgBtn, allOutcomeImgBtn;

    DBHelper dbHelper;
    SQLiteDatabase database;

    DateFormat df = new SimpleDateFormat("d.MM.yyyy");
    String mydate = df.format(Calendar.getInstance().getTime());
    int dayofweek = Calendar.getInstance().get(Calendar.DAY_OF_WEEK);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbHelper = new DBHelper(this);
        database = dbHelper.getWritableDatabase();
        dbHelper.close();

        incomeSumToday = findViewById(R.id.income_sum_today);
        incomeSumWeek = findViewById(R.id.income_sum_week);
        incomeSumMonth = findViewById(R.id.income_sum_month);

        outcomeSumToday = findViewById(R.id.outcome_sum_today);
        outcomeSumWeek = findViewById(R.id.outcome_sum_week);
        outcomeSumMonth = findViewById(R.id.outcome_sum_month);

        allTimeBalanceSum = findViewById(R.id.all_time_balance_sum);

        currencyBalance = findViewById(R.id.currency_balance);
        currencyIncome = findViewById(R.id.currency_income);
        currencyOutcome = findViewById(R.id.currency_outcome);

        incomeImgBtn = findViewById(R.id.income_imgBtn);
        incomeImgBtn.setOnClickListener(this);
        outcomeImgBtn = findViewById(R.id.outcome_imgBtn);
        outcomeImgBtn.setOnClickListener(this);
        balanceImgBtn = findViewById(R.id.balance_imgBtn);
        balanceImgBtn.setOnClickListener(this);

        allIncomeImgBtn = findViewById(R.id.allIncome_imgBtn);
        allIncomeImgBtn.setOnClickListener(this);
        allOutcomeImgBtn = findViewById(R.id.allOutcome_imgBtn);
        allOutcomeImgBtn.setOnClickListener(this);

        incomeSumToday.setText(String.valueOf(getTodayBalance(PurseContract.IncomeEntry.TABLE_INCOME)));
        incomeSumWeek.setText(String.valueOf(getWeekBalance(PurseContract.IncomeEntry.TABLE_INCOME)));
        incomeSumMonth.setText(String.valueOf(getMonthBalance(PurseContract.IncomeEntry.TABLE_INCOME)));

        outcomeSumToday.setText(String.valueOf(getTodayBalance(PurseContract.OutcomeEntry.TABLE_OUTCOME)));
        outcomeSumWeek.setText(String.valueOf(getWeekBalance(PurseContract.OutcomeEntry.TABLE_OUTCOME)));
        outcomeSumMonth.setText(String.valueOf(getMonthBalance(PurseContract.OutcomeEntry.TABLE_OUTCOME)));

        allTimeBalanceSum.setText(String.valueOf(getBalanceAllTime(PurseContract.IncomeEntry.TABLE_INCOME,
                PurseContract.OutcomeEntry.TABLE_OUTCOME)));

        dbHelper.close();
    }

    @Override
    protected void onResume() {
        super.onResume();
        incomeSumToday.setText(String.valueOf(getTodayBalance(PurseContract.IncomeEntry.TABLE_INCOME)));
        incomeSumWeek.setText(String.valueOf(getWeekBalance(PurseContract.IncomeEntry.TABLE_INCOME)));
        incomeSumMonth.setText(String.valueOf(getMonthBalance(PurseContract.IncomeEntry.TABLE_INCOME)));

        outcomeSumToday.setText(String.valueOf(getTodayBalance(PurseContract.OutcomeEntry.TABLE_OUTCOME)));
        outcomeSumWeek.setText(String.valueOf(getWeekBalance(PurseContract.OutcomeEntry.TABLE_OUTCOME)));
        outcomeSumMonth.setText(String.valueOf(getMonthBalance(PurseContract.OutcomeEntry.TABLE_OUTCOME)));
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.income_imgBtn: {
                Intent intent = new Intent(MainActivity.this, IncomeActivity.class);
                intent.putExtra("date_income", mydate);
                startActivity(intent);
                break;
            }
            case R.id.outcome_imgBtn: {
                Intent intent = new Intent(MainActivity.this, OutcomeActivity.class);
                intent.putExtra("date_outcome", mydate);
                startActivity(intent);
                break;
            }

            case R.id.balance_imgBtn: {
                Intent intent = new Intent(MainActivity.this, BalanceActivity.class);
                startActivity(intent);
                break;
            }
            case R.id.allIncome_imgBtn: {
                Intent intent = new Intent(MainActivity.this, AllIncomeActivity.class);
                startActivity(intent);
                break;
            }
            case R.id.allOutcome_imgBtn: {
                Intent intent = new Intent(MainActivity.this, AllOutcomeActivity.class);
                startActivity(intent);
                break;
            }
        }
    }

    int getTodayBalance(String table_name){
        int sum = 0;
        DateFormat df = new SimpleDateFormat("d.MM.yyyy");
        String date_search = df.format(Calendar.getInstance().getTime());
        Cursor cursor;

        database = dbHelper.getReadableDatabase();

         cursor = database.query(table_name, new String[]{"sumInRub"}, "date =?", new String[]{date_search}, null, null, null);
        //cursor = database.rawQuery("SELECT sum_in_RUB AS SUM FROM table_name WHERE date = date_search;", null);
        if (cursor.moveToFirst()) {
            sum = sum + cursor.getInt(0);
            while (cursor.moveToNext()) {
                sum = sum + cursor.getInt(0);
            }
        } else
            sum += 0;
        cursor.close();
        database.close();
        return sum;
    }

    int getWeekBalance(String table_name) {
        int sum = 0;
        String date_search;
        Calendar c = Calendar.getInstance();
        DateFormat df = new SimpleDateFormat("d.MM.yyyy");
        Cursor cursor;

        database = dbHelper.getReadableDatabase();

        for (int i = dayofweek == 1 ? 7 : dayofweek; i > 1; i--) {
            date_search = df.format(c.getTime());
            cursor = database.query(table_name, new String[]{PurseContract.IncomeEntry.INCOME_SUM_IN_RUB}, "date =?", new String[]{date_search}, null, null, null);
            //cursor = database.rawQuery("SELECT sum_in_RUB AS SUM FROM table_name WHERE date = date_search;", null);
            if (cursor.moveToFirst()) {
                sum = sum + cursor.getInt(0);
                while (cursor.moveToNext()) {
                    sum = sum + cursor.getInt(0);
                }
            } else
                sum += 0;

            cursor.close();
            c.add(Calendar.DAY_OF_MONTH, -1);
        }

        database.close();
        return sum;
    }

    private int getMonthBalance(String table_name) {
        int sum = 0;
        DateFormat df = new SimpleDateFormat("MM.yyyy");
        String date_search = "%" + df.format(Calendar.getInstance().getTime());
        Cursor cursor;

        database = dbHelper.getReadableDatabase();

        cursor = database.query(table_name, new String[]{PurseContract.IncomeEntry.INCOME_SUM_IN_RUB}, "date LIKE ?", new String[]{date_search}, null, null, null);
        //cursor = database.rawQuery("SELECT sum_in_RUB AS SUM FROM table_name WHERE date = date_search;", null);
        if (cursor.moveToFirst()) {
            sum = sum + cursor.getInt(0);
            while (cursor.moveToNext()) {
                sum = sum + cursor.getInt(0);
            }
        } else
            sum += 0;

        cursor.close();
        database.close();
        return sum;
    }

    private int getBalanceAllTime(String table_first, String table_second)
    {
        int balance = 0;
        Cursor cursor;

        database = dbHelper.getReadableDatabase();

               cursor = database.query(table_first,new String[]{PurseContract.IncomeEntry.INCOME_SUM_IN_RUB},null,null,null,null,null);
        if (cursor.moveToFirst()) {
            balance = balance + cursor.getInt(0);
        } else
            balance += 0;
        cursor.close();

        cursor = database.query(table_second,new String[]{PurseContract.OutcomeEntry.OUTCOME_SUM_IN_RUB},null,null,null,null,null);
        if (cursor.moveToFirst()) {
            balance = balance - cursor.getInt(0);
        }
        cursor.close();
        database.close();
        return balance;
    }

    /*public void updateIncomeData(){
        incomeSumToday.setText(String.valueOf(getTodayBalance(PurseContract.IncomeEntry.TABLE_INCOME)));
        incomeSumWeek.setText(String.valueOf(getWeekBalance(PurseContract.IncomeEntry.TABLE_INCOME)));
        incomeSumMonth.setText(String.valueOf(getMonthBalance(PurseContract.IncomeEntry.TABLE_INCOME)));
    }

    public void uodateOutcomeData(){
        outcomeSumToday.setText(String.valueOf(getTodayBalance(PurseContract.OutcomeEntry.TABLE_OUTCOME)));
        outcomeSumWeek.setText(String.valueOf(getWeekBalance(PurseContract.OutcomeEntry.TABLE_OUTCOME)));
        outcomeSumMonth.setText(String.valueOf(getMonthBalance(PurseContract.OutcomeEntry.TABLE_OUTCOME)));

    }*/
}
