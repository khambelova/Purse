package com.example.habik.diplomapurse.Outcome;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.habik.diplomapurse.CalendarActivity;

import com.example.habik.diplomapurse.DBHelper;
import com.example.habik.diplomapurse.MainActivity;
import com.example.habik.diplomapurse.PurseContract;
import com.example.habik.diplomapurse.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class OutcomeActivity extends AppCompatActivity implements View.OnClickListener {
    final int REQUEST_CODE_DATA = 0;
    final int REQUEST_CODE_CATEGORY = 1;

    TextView outcomeDate, categoryText;
    ImageButton dateImBtng, categoryImgBtn, saveImgBtn;
    EditText outcomeSum, outcomeComment;
    TextInputLayout textInputLayout;

    Map<String,Double> currencyDict = new HashMap<String, Double>();
    private String[] currencies = {"RUB (Российский рубль)", "USD (Доллар США)", "EUR (Евро)", "GBP (Фунт стерлингов)", "BYN (Белорусский рубль)", "UAH (Украинская гривна)"};
    String currency;
    double sumInRub = 0.0;
    int index = 0;
    boolean isEdited = false;

    DBHelper dbHelper;
    SQLiteDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_outcome);
        outcomeDate = findViewById(R.id.date_outcome);
        dateImBtng = findViewById(R.id.date_imgBtn);
        dateImBtng.setOnClickListener(this);
        categoryText = findViewById(R.id.category);
        categoryImgBtn = findViewById(R.id.category_imgBtn);
        categoryImgBtn.setOnClickListener(this);
        saveImgBtn = findViewById(R.id.btn_save);
        saveImgBtn.setOnClickListener(this);
        outcomeSum = findViewById(R.id.sum_for_category);
        outcomeComment = findViewById(R.id.comment);
        Intent intent = getIntent();
        String date = intent.getStringExtra("date_outcome");
        outcomeDate.setText(date);
        textInputLayout = findViewById(R.id.text_input_layout);
        outcomeComment = (EditText) textInputLayout.findViewById(R.id.comment);
        textInputLayout.setHint(getString(R.string.comment));
        ArrayAdapter<String> currencyAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, currencies);
        currencyAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        currencyDict.put("RUB (Российский рубль)",1.0);
        currencyDict.put("USD (Доллар США)",65.3834);
        currencyDict.put("EUR (Евро)",72.8436);
        currencyDict.put("GBP (Фунт стерлингов)",82.5531);
        currencyDict.put("UAH (Украинская гривна)",2.43333);
        currencyDict.put("BYN (Белорусский рубль)",31.1884);

        Spinner spinnerCurrency = (Spinner) findViewById(R.id.currencies_spinner);
        spinnerCurrency.setAdapter(currencyAdapter);
        spinnerCurrency.setPrompt("Список валют");
        spinnerCurrency.setOnItemSelectedListener(onItemSelectedListener());

        dbHelper = new DBHelper(this);


        isEdited = getIntent().getBooleanExtra("isEdited", false);

        if(isEdited)
        {
            index = getIntent().getIntExtra("id", 0);
            outcomeDate.setText(getIntent().getStringExtra("date"));
            outcomeSum.setText(String.valueOf(getIntent().getDoubleExtra("sum", 0.0)));
            outcomeComment.setText(getIntent().getStringExtra("comment"));
            categoryText.setText(getIntent().getStringExtra("category"));
            spinnerCurrency.setSelection(currencyAdapter.getPosition(getIntent().getStringExtra("currency")));
        }
    }

    AdapterView.OnItemSelectedListener onItemSelectedListener() {
        return new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                currency = currencies[position];
                if(currency!= null && !outcomeSum.getText().toString().equals("")) {
                    sumInRub = currencyDict.get(currency) * Double.parseDouble(outcomeSum.getText().toString());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        };
    }

    public void onClick(View view) {
        ContentValues contentValues = new ContentValues();
        database = dbHelper.getWritableDatabase();

        if(sumInRub == 0 && currency!= null && !outcomeSum.getText().toString().equals("")) {
            sumInRub = currencyDict.get(currency) * Double.parseDouble(outcomeSum.getText().toString());
        }

        switch (view.getId()) {
            case R.id.date_imgBtn: {
                Intent intent_data = new Intent(OutcomeActivity.this, CalendarActivity.class);
                startActivityForResult(intent_data, REQUEST_CODE_DATA);
                break;
            }
            case R.id.category_imgBtn: {
                Intent intent_category = new Intent(OutcomeActivity.this, CategoryOutcomeActivity.class);
                startActivityForResult(intent_category, REQUEST_CODE_CATEGORY);
                break;
            }
            case R.id.btn_save: {
                contentValues.put(PurseContract.OutcomeEntry.OUTCOME_DATE, outcomeDate.getText().toString());
                contentValues.put(PurseContract.OutcomeEntry.OUTCOME_CATEGORY, categoryText.getText().toString());
                contentValues.put(PurseContract.OutcomeEntry.OUTCOME_SUM, Integer.parseInt(outcomeSum.getText().toString()));
                contentValues.put(PurseContract.OutcomeEntry.OUTCOME_CURRENCY, currency);
                contentValues.put(PurseContract.OutcomeEntry.OUTCOME_COMMENT, outcomeComment.getText().toString());
                contentValues.put(PurseContract.OutcomeEntry.OUTCOME_SUM_IN_RUB, sumInRub);
                database.insert(PurseContract.OutcomeEntry.TABLE_OUTCOME, null, contentValues);
                sumInRub = 0;
                if (!isEdited) {
                    Intent intent = new Intent(OutcomeActivity.this, MainActivity.class);
                } else {
                    database.update(PurseContract.OutcomeEntry.TABLE_OUTCOME, contentValues,
                            PurseContract.OutcomeEntry._ID + "= ?", new String[]{String.valueOf(index)});
                }

                Intent intent = new Intent(OutcomeActivity.this, MainActivity.class);
                startActivity(intent);
                break;
            }
        }
        dbHelper.close();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQUEST_CODE_DATA: {
                    String calendarDate = data.getStringExtra("calendarDate");
                    outcomeDate.setText(calendarDate);
                    break;
                }
                case REQUEST_CODE_CATEGORY: {
                    String categoryName = data.getStringExtra("category");
                    categoryText.setText(categoryName);
                    break;
                }
            }
        }
    }
}
