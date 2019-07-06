package com.example.habik.diplomapurse;

import android.provider.BaseColumns;

import java.util.HashMap;
import java.util.Map;

public class PurseContract {

    public static final  String TYPE_TEXT ="TEXT";
    public static final String TYPE_INTEGER = "INTEGER";
    public static final String TYPE_REAL= "REAL";

    public static  final class IncomeCategoriesEntry implements BaseColumns {
        public static final String TABLE_INCOME_CATEGORY = "categoriesIncome";
        public static final String INCOME_CATEGORY_NAME = "name";

        public static final String CREATE_INCOME_CATEGORY = "CREATE TABLE IF NOT EXISTS " + TABLE_INCOME_CATEGORY +
                "(" + _ID + " " + TYPE_INTEGER + " PRIMARY KEY AUTOINCREMENT, " + INCOME_CATEGORY_NAME + " "
                + TYPE_TEXT + ")";

        public static final String DROP_INCOME_CATEGORY = "DROP TABLE IF EXISTS " + INCOME_CATEGORY_NAME;

    }
        public static final class OutcomeCategoriesEntry implements BaseColumns
        {
            public static final String TABLE_OUTCOME_CATEGORY = "categoriesOutcome";
            public static  final String OUTCOME_CATEGORY_NAME = "name";

            public static final  String TYPE_TEXT ="TEXT";
            public static final String TYPE_INTEGER = "INTEGER";

            public static final String CREATE_OUTCOME_CATEGORY = "CREATE TABLE IF NOT EXISTS "+ TABLE_OUTCOME_CATEGORY +
                    "("+_ID + " " + TYPE_INTEGER + " PRIMARY KEY AUTOINCREMENT, " + OUTCOME_CATEGORY_NAME + " "
                    + TYPE_TEXT + ")";

            public static final String DROP_OUTCOME_CATEGORY = "DROP TABLE IF EXISTS " + OUTCOME_CATEGORY_NAME;
        }

    public static final class OutcomeEntry implements BaseColumns
    {
        public static final String TABLE_OUTCOME = "Outcome";
        public static  final String OUTCOME_DATE = "date";
        public static  final String OUTCOME_CATEGORY = "category";
        public static  final String OUTCOME_SUM = "sum";
        public static  final String OUTCOME_CURRENCY = "currency";
        public static  final String OUTCOME_COMMENT = "comment";
        public static  final String OUTCOME_SUM_IN_RUB = "sumInRub";

        public static final String CREATE_OUTCOME = "CREATE TABLE IF NOT EXISTS "+ TABLE_OUTCOME +
                "("+_ID + " " + TYPE_INTEGER + " PRIMARY KEY AUTOINCREMENT, " + OUTCOME_DATE + " "
                + TYPE_TEXT + ", "+  OUTCOME_CATEGORY + " " + TYPE_TEXT + ", " +  OUTCOME_SUM + " "
                + TYPE_INTEGER + ", " + OUTCOME_CURRENCY + " " + TYPE_TEXT + ", " + OUTCOME_COMMENT + " "
                + TYPE_TEXT + ", " + OUTCOME_SUM_IN_RUB + " " + TYPE_REAL + ")";

        public static final String DROP_OUTCOME = "DROP TABLE IF EXISTS " + TABLE_OUTCOME;
    }

    public static final class IncomeEntry implements BaseColumns
    {
        public static final String TABLE_INCOME = "Income";
        public static  final String INCOME_DATE = "date";
        public static  final String INCOME_CATEGORY = "category";
        public static  final String INCOME_SUM = "sum";
        public static  final String INCOME_CURRENCY = "currency";
        public static  final String INCOME_COMMENT = "comment";
        public static  final String INCOME_SUM_IN_RUB = "sumInRub";

        public static final String CREATE_INCOME = "CREATE TABLE IF NOT EXISTS "+ TABLE_INCOME +
                "("+_ID + " " + TYPE_INTEGER + " PRIMARY KEY AUTOINCREMENT, " + INCOME_DATE + " "
                + TYPE_TEXT + ", "+  INCOME_CATEGORY + " " + TYPE_TEXT + ", " +  INCOME_SUM + " "
                + TYPE_INTEGER + ", " + INCOME_CURRENCY + " " + TYPE_TEXT + ", " + INCOME_COMMENT + " "
                + TYPE_TEXT + ", " + INCOME_SUM_IN_RUB + " " + TYPE_REAL + ")";

        public static final String DROP_INCOME = "DROP TABLE IF EXISTS " + TABLE_INCOME;
    }
}
