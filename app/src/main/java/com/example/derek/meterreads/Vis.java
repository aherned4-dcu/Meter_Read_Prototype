package com.example.derek.meterreads;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;

public class Vis extends AppCompatActivity {
    TextView editTextReading, editTextDate, editTextMPRN;
    BarChart barchart;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        setContentView(R.layout.activity_vis);
        barchart = (BarChart) findViewById(R.id.chart1);
        barchart.setDrawBarShadow(false);
        barchart.setPinchZoom(false);
        barchart.setDrawGridBackground(false);
        int color = ContextCompat.getColor(this, R.color.sseblue);

        ArrayList<BarEntry> barEntries = new ArrayList<>();
        barEntries.add(new BarEntry(1,564f));
        barEntries.add(new BarEntry(2,560f));
        barEntries.add(new BarEntry(3,400f));
        barEntries.add(new BarEntry(4,469f));
        barEntries.add(new BarEntry(5,550f));
        barEntries.add(new BarEntry(6,680f));


        BarDataSet barDataSet = new BarDataSet(barEntries,"Monthly Energy Usage (kWh)");
        barDataSet.setColors(color);

        BarData data = new BarData(barDataSet);

        barchart.setData(data);


        XAxis xAxis = barchart.getXAxis();
        String[] months = new String[]{"","Jan","Feb","Mar","Apr","May","Jun"};
        xAxis.setValueFormatter(new MyXAxisValueFormatter(months));
        xAxis.setPosition(XAxis.XAxisPosition.BOTH_SIDED);
        xAxis.setDrawGridLines(false);
        xAxis.setGranularity(1);

       // xAxis.setCenterAxisLabels(true);
        //xAxis.setAxisMinimum(1);



    }

    public void openHome (View v) {
        Intent homeIntent = new Intent(this,Home.class);
        startActivity(homeIntent);
    }

    public class MyXAxisValueFormatter implements IAxisValueFormatter {

        private String[] mValues;

        public MyXAxisValueFormatter(String[] values) {
            this.mValues = values;
        }

        @Override
        public String getFormattedValue(float value, AxisBase axis) {
            return mValues[(int)value];
        }

    }

    }

