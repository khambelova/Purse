package com.example.habik.diplomapurse.Balance;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.habik.diplomapurse.DBHelper;
import com.example.habik.diplomapurse.Income.Income;
import com.example.habik.diplomapurse.PurseContract;
import com.example.habik.diplomapurse.R;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BalanceActivity extends AppCompatActivity implements View.OnClickListener {

    TextView year, total;
    ImageButton left, right;
    int choose_year;
    RecyclerView recyclerView;
    List<Balance> balanceList = new ArrayList<>();
    DataAdapterBalance adapterBalance;
    DBHelper dbHelper;
    SQLiteDatabase database;
    Map<String, String> monthDict = new HashMap<>();
    double sumBalance = 0.0;
    List<Balance> incomeList = new ArrayList<>();
    List<Balance> outcomeList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_balance);
        year = findViewById(R.id.year);
        Calendar current_year = Calendar.getInstance();
        choose_year = current_year.get(Calendar.YEAR);
        year.setText(String.valueOf(choose_year));

        left = findViewById(R.id.left_imgBtn);
        left.setOnClickListener(this);
        right = findViewById(R.id.right_imgBtn);
        right.setOnClickListener(this);
        total = findViewById(R.id.total_sum);

        recyclerView = findViewById(R.id.recycler_balance);
        adapterBalance = new DataAdapterBalance(this, balanceList);

        dbHelper = new DBHelper(this);
        database = dbHelper.getReadableDatabase();
        recyclerView.setAdapter(adapterBalance);
        makeKeys(choose_year);
        getBalance();
    }

    private void getBalance() {
        String[] months = {"01.", "02.", "03.", "04.", "05.", "06.", "07.", "08.", "09.", "10.", "11.", "12."};
        sumBalance = 0.0;
        balanceList.clear();
        double incomeSum = 0;
        double outcomeSum = 0;
        double balanceSum = 0;
        database = dbHelper.getReadableDatabase();

        Cursor cursor;
        for (int i = 0; i < months.length; i++) {
            months[i] = "%" + months[i] + choose_year;
            cursor = database.query(PurseContract.IncomeEntry.TABLE_INCOME, new String[]{PurseContract.IncomeEntry.INCOME_SUM_IN_RUB}, "date LIKE ?", new String[]{months[i]}, null, null, null);
            if (cursor.moveToFirst()) {
                incomeSum = incomeSum + cursor.getInt(0);
                while (cursor.moveToNext()) {
                    incomeSum = incomeSum + cursor.getInt(0);
                }
            } else
                incomeSum = 0;
            sumBalance = sumBalance + incomeSum;
            cursor.close();

            cursor = database.query(PurseContract.OutcomeEntry.TABLE_OUTCOME, new String[]{PurseContract.OutcomeEntry.OUTCOME_SUM_IN_RUB}, "date LIKE ?", new String[]{months[i]}, null, null, null);
            if (cursor.moveToFirst()) {
                outcomeSum = outcomeSum + cursor.getInt(0);
                while (cursor.moveToNext()) {
                    outcomeSum = outcomeSum + cursor.getInt(0);
                }
            } else
                outcomeSum = 0;
            sumBalance = sumBalance - outcomeSum;

            balanceSum = incomeSum - outcomeSum;
            incomeSum = 0;
            outcomeSum = 0;
            balanceList.add(new Balance(monthDict.get(months[i]), balanceSum));
            cursor.close();
            total.setText(String.valueOf(sumBalance));


            adapterBalance.notifyDataSetChanged();
        }
    }

    private void makeKeys(int year){
        monthDict.put("%" + "01." + year, "Январь");
        monthDict.put("%" + "02." + year, "Февраль");
        monthDict.put("%" + "03." + year, "Март");
        monthDict.put("%" + "04." + year, "Апрель");
        monthDict.put("%" + "05." + year, "Май");
        monthDict.put("%" + "06." + year, "Июнь");
        monthDict.put("%" + "07." + year, "Июль");
        monthDict.put("%" + "08." + year, "Август");
        monthDict.put("%" + "09." + year, "Сентябрь");
        monthDict.put("%" + "10." + year, "Октябрь");
        monthDict.put("%" + "11." + year, "Ноябрь");
        monthDict.put("%" + "12." + year, "Декабрь");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.left_imgBtn: {
                choose_year = choose_year - 1;
                year.setText(String.valueOf(choose_year));
                monthDict.clear();

                makeKeys(choose_year);
                getBalance();
                break;
            }
            case R.id.right_imgBtn: {
                choose_year = choose_year + 1;
                year.setText(String.valueOf(choose_year));
                monthDict.clear();
                makeKeys(choose_year);
                getBalance();
                break;
            }
        }
        dbHelper.close();
    }
}
