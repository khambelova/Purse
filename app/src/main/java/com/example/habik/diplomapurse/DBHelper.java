package com.example.habik.diplomapurse;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "purse.db";
    private static final int DB_VERSION = 1;

    public DBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(PurseContract.IncomeCategoriesEntry.CREATE_INCOME_CATEGORY);
        db.execSQL(PurseContract.OutcomeCategoriesEntry.CREATE_OUTCOME_CATEGORY);
        db.execSQL(PurseContract.IncomeEntry.CREATE_INCOME);
        db.execSQL(PurseContract.OutcomeEntry.CREATE_OUTCOME);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(PurseContract.IncomeCategoriesEntry.DROP_INCOME_CATEGORY);
        db.execSQL(PurseContract.OutcomeCategoriesEntry.DROP_OUTCOME_CATEGORY);
        db.execSQL(PurseContract.IncomeEntry.DROP_INCOME);
        db.execSQL(PurseContract.OutcomeEntry.DROP_OUTCOME);
        onCreate(db);
    }
}
