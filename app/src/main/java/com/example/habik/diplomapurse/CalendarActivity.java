package com.example.habik.diplomapurse;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.CalendarView;

public class CalendarActivity extends AppCompatActivity {
    CalendarView calendarView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);
        calendarView = findViewById(R.id.calendar);
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange( CalendarView view, int year, int month, int dayOfMonth) {
                int mYear = year;
                int mMonth = month + 1;
                int mDay = dayOfMonth;
                String sMonth;
                if(mMonth<=9){
                    sMonth=0+Integer.toString(mMonth);
                }
                else sMonth=Integer.toString(mMonth);

                String selectedDate = new StringBuilder().append(mDay).append(".").append(sMonth).append(".")
                        .append(mYear).toString();
                Intent intent_data = new Intent();
                intent_data.putExtra("calendarDate",selectedDate);
                setResult(RESULT_OK,intent_data);
                finish();
            }
        });
    }
}
