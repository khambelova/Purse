package com.example.habik.diplomapurse.AllOutcome;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.example.habik.diplomapurse.AllIncome.AllIncomeInCategory;
import com.example.habik.diplomapurse.DBHelper;
import com.example.habik.diplomapurse.DateTypeEnum;
import com.example.habik.diplomapurse.Outcome.OutcomeActivity;
import com.example.habik.diplomapurse.PurseContract;
import com.example.habik.diplomapurse.R;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class AllOutcomeInCategoryActivity extends AppCompatActivity {

    SQLiteDatabase database;
    DBHelper dbHelper;
    AlertDialog.Builder builder;
    List<AllOutcomeInCategory> allOutcomeInCategoryList = new ArrayList<>();
    Context context;
    RecyclerView recyclerView;
    DataAdapterOutcomeItem adapter;
    String category;
    String currentDate;
    DateTypeEnum dateType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_outcome_in_category);

        category = getIntent().getStringExtra("category_outcome");
        currentDate = getIntent().getStringExtra("date_outcome");
        dateType = (DateTypeEnum) getIntent().getSerializableExtra("dateType_outcome");
        setTitle(category);
        recyclerView = findViewById(R.id.outcome_recycler);

        context = AllOutcomeInCategoryActivity.this;
        dbHelper = new DBHelper(this);
        adapter = new DataAdapterOutcomeItem(this, allOutcomeInCategoryList, new DataAdapterOutcomeItem.OnItemClickListener() {
            @Override
            public void onItemClick(final AllOutcomeInCategory item) {
                builder = new AlertDialog.Builder(context);
                builder.setTitle(item.getComment().toString()).setMessage("Что Вы хотите сделать с данным расходом?").setIcon(R.drawable.category2).setCancelable(true);
                builder.setNegativeButton("Удалить", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(PurseContract.OutcomeEntry._ID.equalsIgnoreCase(""))
                        {
                            return;
                        }
                        database = dbHelper.getWritableDatabase();
                        database.delete(PurseContract.OutcomeEntry.TABLE_OUTCOME,PurseContract.OutcomeEntry._ID + " = ?", new String[]{String.valueOf(item.getId())});
                        allOutcomeInCategoryList.remove(item);
                        database.close();
                        adapter.notifyDataSetChanged();                    }
                });
                builder.setPositiveButton("Изменить", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(AllOutcomeInCategoryActivity.this,OutcomeActivity.class);
                        intent.putExtra("id", item.getId());
                        intent.putExtra("date",item.getDate());
                        intent.putExtra("currency",item.getCurrency());
                        intent.putExtra("sum",item.getSum());
                        intent.putExtra("comment",item.getComment());
                        intent.putExtra("category",category);
                        intent.putExtra("isEdited",true);
                        startActivityForResult(intent,205);
                    }
                });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });

        getAllOutcomesByCategory();

        recyclerView.setAdapter(adapter);
    }

    private void getAllOutcomesByCategory()
    {
        database = dbHelper.getReadableDatabase();
        Cursor cursor;

        String[] dates = getConvertedDate(currentDate, dateType).split("-");

        for (String date : dates) {
            cursor = database.query(PurseContract.OutcomeEntry.TABLE_OUTCOME,
                    new String[]{PurseContract.OutcomeEntry._ID, PurseContract.OutcomeEntry.OUTCOME_DATE, PurseContract.OutcomeEntry.OUTCOME_CURRENCY,
                            PurseContract.OutcomeEntry.OUTCOME_COMMENT, PurseContract.OutcomeEntry.OUTCOME_SUM},
                    "category =? AND date=?",
                    new String[]{category, date},
                    null, null, null
            );

            if (cursor.moveToFirst()) {
                AllOutcomeInCategory allOutcomeInCategory = new AllOutcomeInCategory(cursor.getInt(0), cursor.getString(1), cursor.getString(2),
                        cursor.getString(3), cursor.getDouble(4));

                allOutcomeInCategoryList.add(allOutcomeInCategory);

                while (cursor.moveToNext()) {
                    allOutcomeInCategory = new AllOutcomeInCategory(cursor.getInt(0), cursor.getString(1), cursor.getString(2),
                            cursor.getString(3), cursor.getDouble(4));

                    allOutcomeInCategoryList.add(allOutcomeInCategory);
                }
            }

            cursor.close();
        }

        database.close();
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
}
