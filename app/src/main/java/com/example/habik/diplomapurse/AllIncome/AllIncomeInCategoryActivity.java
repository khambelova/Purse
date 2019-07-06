package com.example.habik.diplomapurse.AllIncome;

import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.example.habik.diplomapurse.DBHelper;
import com.example.habik.diplomapurse.DateTypeEnum;
import com.example.habik.diplomapurse.Income.IncomeActivity;
import com.example.habik.diplomapurse.PurseContract;
import com.example.habik.diplomapurse.R;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class AllIncomeInCategoryActivity extends AppCompatActivity {

    SQLiteDatabase database;
    DBHelper dbHelper;
    AlertDialog.Builder builder;
    List<AllIncomeInCategory> allIncomeInCategoryList = new ArrayList<>();
    Context context;
    RecyclerView recyclerView;
    DataAdapterIncomeItem adapter;
    String category;
    int index;
    String currentDate;
    DateTypeEnum dateType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_income_in_category);

        category = getIntent().getStringExtra("category");
        currentDate = getIntent().getStringExtra("date");
        dateType = (DateTypeEnum) getIntent().getSerializableExtra("dateType");
        setTitle(category);
        recyclerView = findViewById(R.id.income_recycler);

        context = AllIncomeInCategoryActivity.this;
        dbHelper = new DBHelper(this);
        adapter = new DataAdapterIncomeItem(this, allIncomeInCategoryList, new DataAdapterIncomeItem.OnItemClickListener() {
            @Override
            public void onItemClick(final AllIncomeInCategory item) {
                builder = new AlertDialog.Builder(context);
                builder.setTitle(item.getComment().toString()).setMessage("Что Вы хотите сделать с данным доходом?").setIcon(R.drawable.category2).setCancelable(true);
                builder.setNegativeButton("Удалить", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(PurseContract.IncomeEntry._ID.equalsIgnoreCase(""))
                        {
                            return;
                        }
                        database = dbHelper.getWritableDatabase();
                        database.delete(PurseContract.IncomeEntry.TABLE_INCOME,PurseContract.IncomeEntry._ID + " = ?", new String[]{String.valueOf(item.getId())});
                        allIncomeInCategoryList.remove(item);
                        database.close();
                        adapter.notifyDataSetChanged();
                    }
                });
                builder.setPositiveButton("Изменить", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                   Intent intent = new Intent(AllIncomeInCategoryActivity.this,IncomeActivity.class);
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

        getAllIncomesByCategory();

        recyclerView.setAdapter(adapter);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case 205: {
                    String date = data.getStringExtra("date");
                    ContentValues contentValues = new ContentValues();
                    break;
                }
            }
        }
    }

    private void getAllIncomesByCategory()
    {
        database = dbHelper.getReadableDatabase();
        Cursor cursor;

        String[] dates = getConvertedDate(currentDate, dateType).split("-");

        for (String date : dates) {
            cursor = database.query(PurseContract.IncomeEntry.TABLE_INCOME,
                    new String[]{PurseContract.IncomeEntry._ID, PurseContract.IncomeEntry.INCOME_DATE, PurseContract.IncomeEntry.INCOME_CURRENCY,
                            PurseContract.IncomeEntry.INCOME_COMMENT, PurseContract.IncomeEntry.INCOME_SUM},
                    "category =? AND date=?",
                    new String[]{category, date},
                    null, null, null
            );

            if (cursor.moveToFirst()) {
                AllIncomeInCategory allIncomeInCategory = new AllIncomeInCategory(cursor.getInt(0), cursor.getString(1), cursor.getString(2),
                        cursor.getString(3), cursor.getDouble(4));

                allIncomeInCategoryList.add(allIncomeInCategory);

                while (cursor.moveToNext()) {
                    allIncomeInCategory = new AllIncomeInCategory(cursor.getInt(0), cursor.getString(1), cursor.getString(2),
                            cursor.getString(3), cursor.getDouble(4));

                    allIncomeInCategoryList.add(allIncomeInCategory);
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