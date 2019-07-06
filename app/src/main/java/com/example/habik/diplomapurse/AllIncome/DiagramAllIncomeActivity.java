package com.example.habik.diplomapurse.AllIncome;

import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.example.habik.diplomapurse.DateTypeEnum;
import com.example.habik.diplomapurse.R;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LegendEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.List;

public class DiagramAllIncomeActivity extends AppCompatActivity {

    double sumInc;
    String diagramDate;
    DateTypeEnum dateType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diagram_all_income);

        List<AllIncome> allIncomeList = (List<AllIncome>) getIntent().getSerializableExtra("categoryList");
        sumInc = getIntent().getDoubleExtra("sum", 0);
        diagramDate = getMonth(getIntent().getStringExtra("currentDate"));
        dateType = (DateTypeEnum) getIntent().getSerializableExtra("currentType");
        setupPieChart(allIncomeList);

        setTitle(diagramDate);
    }

    private void setupPieChart(List<AllIncome> allIncomesList){
        PieChart chart = (PieChart)findViewById(R.id.chart);
        chart.setUsePercentValues(true);

        ArrayList<PieEntry> pieEntries = new ArrayList<PieEntry>();
        ArrayList<LegendEntry> legendEntries = new ArrayList<LegendEntry>();
        String labelName;
        for (int i = 0; i < allIncomesList.size(); i++)
        {
            if(allIncomesList.get(i).getSum_for_category() > 0.0) {
                labelName = getLabelName(allIncomesList.get(i).getCategory_name(), allIncomesList.get(i).getSum_for_category());

                pieEntries.add(new PieEntry(getPercent(allIncomesList.get(i).getSum_for_category()), labelName, i));

                legendEntries.add(new LegendEntry(allIncomesList.get(i).getCategory_name(), Legend.LegendForm.SQUARE,
                        15f, 10f, null, ColorTemplate.COLORFUL_COLORS[i]));
            }
        }

        PieDataSet dataSet = new PieDataSet(pieEntries,"");
        PieData data = new PieData(dataSet);
        data.setValueFormatter(new PercentFormatter());
        chart.setData(data);

        Description description = new Description();
        description.setTextSize(15f);
        description.setText(getDescription());
        chart.setDescription(description);

        Legend legend = chart.getLegend();
        legend.setTextSize(15f);
        legend.setFormSize(15f);
        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        legend.setOrientation(Legend.LegendOrientation.VERTICAL);
        legend.setCustom(legendEntries);

        chart.setDrawHoleEnabled(true);
        chart.setHoleRadius(15f);
        chart.setTransparentCircleRadius(15f);
        dataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        dataSet.setValueTextSize(20);
        chart.setDrawEntryLabels(true);
        chart.setDrawSlicesUnderHole(true);
        chart.animateY(1000);
        chart.invalidate();
    }

    private String getLabelName(String name, double value)
    {
        return name + " (" + value + ")";
    }

    private String getMonth(String date)
    {
        String month = date.split("\\.")[1];

        switch (month)
        {
            case "01": return "Доходы за январь " + date.split("\\.")[2];
            case "02": return "Доходы за февраль " + date.split("\\.")[2];
            case "03": return "Доходы за март " + date.split("\\.")[2];
            case "04": return "Доходы за апрель " + date.split("\\.")[2];
            case "05": return "Доходы за май " + date.split("\\.")[2];
            case "06": return "Доходы за июнь " + date.split("\\.")[2];
            case "07": return "Доходы за июль " + date.split("\\.")[2];
            case "08": return "Доходы за август " + date.split("\\.")[2];
            case "09": return "Доходы за сентябрь " + date.split("\\.")[2];
            case "10": return "Доходы за октябрь " + date.split("\\.")[2];
            case "11": return "Доходы за ноябрь " + date.split("\\.")[2];
            case "12": return "Доходы за декабрь " + date.split("\\.")[2];
            default: return "Доходы за месяц";
        }
    }

    private String getDescription() {
        String decs = "Доходы по категориям за ";

        switch (dateType)
        {
            case DAY: return decs + "день";
            case WEEK: return decs + "неделю";
            case MONTH: return decs + "месяц";
            default: return decs + "тип";
        }
    }

    private float getPercent(double value)
    {
        double percent = (value / sumInc) * 100.0;
        return (float)percent;
    }
}
