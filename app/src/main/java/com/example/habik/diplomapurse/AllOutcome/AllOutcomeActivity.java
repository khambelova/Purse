package com.example.habik.diplomapurse.AllOutcome;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.habik.diplomapurse.AllIncome.AllIncome;
import com.example.habik.diplomapurse.AllIncome.AllIncomeActivity;
import com.example.habik.diplomapurse.AllIncome.AllIncomeInCategory;
import com.example.habik.diplomapurse.AllIncome.AllIncomeInCategoryActivity;
import com.example.habik.diplomapurse.AllIncome.DataAdapterAllIncome;
import com.example.habik.diplomapurse.CalendarActivity;
import com.example.habik.diplomapurse.DateTypeEnum;
import com.example.habik.diplomapurse.DBHelper;
import com.example.habik.diplomapurse.DateTypeEnum;
import com.example.habik.diplomapurse.PurseContract;
import com.example.habik.diplomapurse.R;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class AllOutcomeActivity extends AppCompatActivity implements View.OnClickListener {
    final int REQUEST_CODE_DATA = 200;
    final String [] dates = {"ДЕНЬ","НЕДЕЛЯ","МЕСЯЦ"};
    List<AllOutcome> allOutcomeList = new ArrayList<>();
    ImageButton diagramBtn;
    Button calendar;
    DataAdapterAllOutcome adapterAllOutcome;
    TextView summOutcomes,sum_income_for_period;

    private DBHelper dbHelper;
    SQLiteDatabase database;
    double sumOut = 0.0;
    double sumInc = 0.0;
    private String currentDate;
    private DateTypeEnum currentDateType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_outcome);
        calendar = findViewById(R.id.calendar_Btn);
        calendar.setOnClickListener(this);
        sum_income_for_period = findViewById(R.id.sum_income_for_period);

        RecyclerView recyclerView = findViewById(R.id.recycler_all_outcome);

        adapterAllOutcome = new DataAdapterAllOutcome(this, allOutcomeList, new DataAdapterAllOutcome.OnItemClickListener() {
            @Override
            public void onItemClick(AllOutcome item) {
                Intent intent = new Intent(AllOutcomeActivity.this, AllOutcomeInCategoryActivity.class);
                intent.putExtra("category_outcome", item.getCategory_name());
                intent.putExtra("date_outcome", currentDate);
                intent.putExtra("dateType_outcome", currentDateType);

                startActivity(intent);
            }
        });
        recyclerView.setAdapter(adapterAllOutcome);

        diagramBtn = findViewById(R.id.diagram_btn);
        diagramBtn.setOnClickListener(this);

        summOutcomes = findViewById(R.id.total_sum);

        dbHelper = new DBHelper(this);

        currentDate = new SimpleDateFormat("dd.MM.yyyy").format(Calendar.getInstance().getTime());
        calendar.setText(currentDate);
        currentDateType = DateTypeEnum.DAY;

        updateBalance(currentDate, currentDateType);

        Spinner date_spinner = findViewById(R.id.date_spinner);
        ArrayAdapter<String> dateAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, dates);
        dateAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        date_spinner.setAdapter(dateAdapter);



        AdapterView.OnItemSelectedListener itemSelectedListener = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String item = (String)parent.getItemAtPosition(position);
                currentDateType = item.equals(dates[0]) ? DateTypeEnum.DAY : item.equals(dates[1]) ? DateTypeEnum.WEEK : DateTypeEnum.MONTH;
                updateBalance(currentDate, currentDateType);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        };
        date_spinner.setOnItemSelectedListener(itemSelectedListener);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.calendar_Btn: {
                Intent intent = new Intent(AllOutcomeActivity.this, CalendarActivity.class);
                startActivityForResult(intent,REQUEST_CODE_DATA);
                break;
            }
            case R.id.diagram_btn: {
                Intent intent = new Intent(AllOutcomeActivity.this, DiagramAllOutcomeActivity.class);

                intent.putExtra("categoryList", (ArrayList<AllOutcome>) allOutcomeList);
                intent.putExtra("sum", sumOut);
                intent.putExtra("currentDate", currentDate);
                intent.putExtra("currentType", currentDateType);

                startActivity(intent);
                break;
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQUEST_CODE_DATA: {
                    String calendarDate = data.getStringExtra("calendarDate");
                    currentDate = calendarDate;
                    calendar.setText(currentDate);
                    updateBalance(currentDate, currentDateType);
                    break;
                }
            }
        }
    }

    private void updateBalance(String inputDate, DateTypeEnum dateType)
    {
        allOutcomeList.clear();
        summOutcomes.setText("0");
        sumOut = 0;
        sumInc = 0;

        String convertedDate = getConvertedDate(inputDate, dateType);

        getAllOutcomeCategories(convertedDate);
        getAllIncomeCategories(convertedDate);

        summOutcomes.setText(String.valueOf(sumOut));
        sum_income_for_period.setText(String.valueOf(sumInc));
    }

    private String getConvertedDate(String date, DateTypeEnum dateType) {

        if(date.equals(""))
            return "";

        String convertedDate = "";

        switch (dateType) {
            case DAY: {
                DateFormat df = new SimpleDateFormat("d.MM.yyyy");
                Calendar c = Calendar.getInstance();
                try {
                    c.setTime(df.parse(date));
                } catch (ParseException e) {
                    return "";
                }

                convertedDate = df.format(c.getTime());

                return convertedDate;
            }
            case WEEK: {
                DateFormat df = new SimpleDateFormat("d.MM.yyyy");
                Calendar c = Calendar.getInstance();
                try {
                    c.setTime(df.parse(date));
                } catch (ParseException e) {
                    return "";
                }
                int dayOfWeek = c.get(Calendar.DAY_OF_WEEK);

                for(int i = dayOfWeek == 1 ? 7 : dayOfWeek; i > 1; i--) {
                    convertedDate += (df.format(c.getTime()) + "-");
                    c.add(Calendar.DAY_OF_MONTH, -1);
                }

                if(dayOfWeek == 1)
                {
                    convertedDate += (df.format(c.getTime()) + "-");
                }

                return convertedDate.replaceAll("[-]+$", "");
            }
            case MONTH: {
                DateFormat df = new SimpleDateFormat("MM.yyyy");
                Calendar c = Calendar.getInstance();
                try {
                    c.setTime(new SimpleDateFormat("dd.MM.yyyy").parse(date));
                } catch (ParseException e) {
                    return "";
                }

                convertedDate = "%" + df.format(c.getTime());

                return convertedDate;
            }
        }

        return convertedDate;
    }

    public void getAllOutcomeCategories(String date)
    {
        database = dbHelper.getReadableDatabase();
        Cursor cursor;

        cursor = database.query(PurseContract.OutcomeCategoriesEntry.TABLE_OUTCOME_CATEGORY, null, null, null, null, null, null);
        if(cursor.moveToFirst())
        {
            getAllOutcomesByCategoryAndDate(cursor.getString(1), date);

            while (cursor.moveToNext())
            {
                getAllOutcomesByCategoryAndDate(cursor.getString(1), date);
            }
        }

        adapterAllOutcome.notifyDataSetChanged();
        cursor.close();
        database.close();
    }

    public void getAllIncomeCategories(String date)
    {
        database = dbHelper.getReadableDatabase();
        Cursor cursor;

        cursor = database.query(PurseContract.IncomeCategoriesEntry.TABLE_INCOME_CATEGORY, null, null, null, null, null, null);
        if(cursor.moveToFirst())
        {
            getAllIncomesByCategoryAndDate(cursor.getString(1), date);

            while (cursor.moveToNext())
            {
                getAllIncomesByCategoryAndDate(cursor.getString(1), date);
            }
        }
        cursor.close();
        database.close();
    }

    private void getAllOutcomesByCategoryAndDate(String catName, String inputDate)
    {
        int sum = 0;
        database = dbHelper.getReadableDatabase();
        Cursor cursor;

        String[] dates = inputDate.split("-");

        for (String date : dates) {
            cursor = database.query(PurseContract.OutcomeEntry.TABLE_OUTCOME, new String[]{PurseContract.OutcomeEntry.OUTCOME_SUM_IN_RUB}, "category =? AND date LIKE ?", new String[]{catName, date}, null, null, null);

            if (cursor.moveToFirst()) {
                sum = sum + cursor.getInt(0);

                while (cursor.moveToNext()) {
                    sum = sum + cursor.getInt(0);
                }
            } else
                sum += 0;

            cursor.close();
        }
        allOutcomeList.add(new AllOutcome(catName, sum));
        sumOut += sum;
        database.close();
    }

    private void getAllIncomesByCategoryAndDate(String catName, String inputDate)
    {
        int sum = 0;
        database = dbHelper.getReadableDatabase();
        Cursor cursor;

        String[] dates = inputDate.split("-");

        for (String date : dates) {
            cursor = database.query(PurseContract.IncomeEntry.TABLE_INCOME, new String[]{PurseContract.IncomeEntry.INCOME_SUM_IN_RUB}, "category =? AND date LIKE ?", new String[]{catName, date}, null, null, null);

            if (cursor.moveToFirst()) {
                sum = sum + cursor.getInt(0);

                while (cursor.moveToNext()) {
                    sum = sum + cursor.getInt(0);
                }
            } else
                sum += 0;

            cursor.close();
        }
        sumInc += sum;
        database.close();
    }
}
