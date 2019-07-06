package com.example.habik.diplomapurse.Income;

import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import com.example.habik.diplomapurse.Category;
import com.example.habik.diplomapurse.DBHelper;
import com.example.habik.diplomapurse.PurseContract;
import com.example.habik.diplomapurse.R;

import java.util.ArrayList;
import java.util.List;

public class CategoryIncomeActivity extends AppCompatActivity implements View.OnClickListener{
    List<Category> categoriesInc = new ArrayList<>();
    ImageButton imgBtnAddCategory;
    EditText addCategoryET;
    String catName;
    String oldCatName;
    RecyclerView recyclerView;
    DataAdapterIncome adapter;
    private DBHelper dbHelper;
    AlertDialog.Builder builder;
    Context context;
    int  position;
    boolean isEdited = false;
    SQLiteDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_income);

        imgBtnAddCategory = (ImageButton) findViewById(R.id.btn_save);
        imgBtnAddCategory.setOnClickListener(this);
        addCategoryET = (EditText) findViewById(R.id.category_et);
        recyclerView = (RecyclerView) findViewById(R.id.categoty_income_list);

        context = CategoryIncomeActivity.this;
        dbHelper = new DBHelper(this);
        database = dbHelper.getWritableDatabase();

        adapter = new DataAdapterIncome(this, categoriesInc, new DataAdapterIncome.OnItemClickListener() {
            @Override
            public void onItemClick(final Category item) {
                builder = new AlertDialog.Builder(context);
                builder.setTitle(item.getNameCategory().toString()).setMessage("Что Вы хотите сделать с данной категорией?").setIcon(R.drawable.category2).setCancelable(true);
                builder.setNegativeButton("Удалить", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(item.getNameCategory().toString().equalsIgnoreCase(""))
                        {
                            return;
                        }
                            database.delete(PurseContract.IncomeCategoriesEntry.TABLE_INCOME_CATEGORY,PurseContract.IncomeCategoriesEntry.INCOME_CATEGORY_NAME + " = ?",new String[]{item.getNameCategory().toString()});
                            categoriesInc.remove(item);
                            adapter.notifyDataSetChanged();

                        dbHelper.close();
                    }
                });
                builder.setPositiveButton("Изменить", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        oldCatName = item.getNameCategory().toString();
                        position = adapter.getItemCount();
                        addCategoryET.setText(item.getNameCategory().toString());
                        isEdited = true;

                    }
                });
                builder.setNeutralButton("Выбрать", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent_category = new Intent();
                        intent_category.putExtra("category", item.getNameCategory().toString());
                        setResult(RESULT_OK, intent_category);
                        finish();
                    }
                });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });
        recyclerView.setAdapter(adapter);

        Cursor cursor = database.query(PurseContract.IncomeCategoriesEntry.TABLE_INCOME_CATEGORY, null, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            int nameIndex = cursor.getColumnIndex(PurseContract.IncomeCategoriesEntry.INCOME_CATEGORY_NAME);
            Category category = new Category(cursor.getString(nameIndex));
            categoriesInc.add(category);

            while (cursor.moveToNext()) {
                nameIndex = cursor.getColumnIndex(PurseContract.IncomeCategoriesEntry.INCOME_CATEGORY_NAME);
                category = new Category(cursor.getString(nameIndex));
                categoriesInc.add(category);
            }
            adapter.notifyDataSetChanged();
        }
        cursor.close();
    }

    @Override
    public void onClick(View v) {
        catName = addCategoryET.getText().toString();
        ContentValues contentValues = new ContentValues();
        database = dbHelper.getWritableDatabase();
        switch (v.getId()) {
            case R.id.btn_save: {
                if (isEdited == true) {
                    if (catName.equalsIgnoreCase("")) {
                        break;
                    }
                    contentValues.put(PurseContract.IncomeCategoriesEntry.INCOME_CATEGORY_NAME, catName);
                    database.update(PurseContract.IncomeCategoriesEntry.TABLE_INCOME_CATEGORY, contentValues,
                            PurseContract.IncomeCategoriesEntry.INCOME_CATEGORY_NAME + " = ?" ,new String[]{oldCatName});
                    addCategoryET.setText("");
                    categoriesInc.set(position - 1,new Category(catName));
                    adapter.notifyDataSetChanged();
                    isEdited = false;
                }
                else{
                    contentValues.put(PurseContract.IncomeCategoriesEntry.INCOME_CATEGORY_NAME, catName);
                    database.insert(PurseContract.IncomeCategoriesEntry.TABLE_INCOME_CATEGORY, null, contentValues);
                    categoriesInc.add(new Category(catName));
                    addCategoryET.setText("");
                    adapter.notifyDataSetChanged();
                }
                break;
            }
        }
        dbHelper.close();
    }
}
