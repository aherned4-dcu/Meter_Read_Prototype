package com.example.derek.meterreads;

import android.app.AlertDialog;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.google.firebase.auth.FirebaseAuth;


import java.util.ArrayList;

public class Vis extends AppCompatActivity {
    TextView editTextReading, editTextDate, editTextMPRN;
    BarChart barchart;
    Button btnAddData,btnViewAll,btnDelete;
    DataBaseBuild myDb;
    private FirebaseAuth mAuth;
    public static final String MPRN_CON="123456789";
    public static final String READING="54321";
    public static final String DATE="2018-04-25";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        setContentView(R.layout.activity_vis);
        btnAddData = findViewById(R.id.button3);
        btnViewAll= findViewById(R.id.buttonShowAll);
        btnDelete= findViewById(R.id.buttonDelete);
        AddData();
        viewAll();
        DeleteData();
        myDb = new DataBaseBuild(this);
        barchart = (BarChart) findViewById(R.id.chart1);
        barchart.setDrawBarShadow(false);
        barchart.setPinchZoom(false);
        barchart.setDescription(null);
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

    public  void AddData() {
        btnAddData.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        boolean isInserted = myDb.insertData("12345672", "123456", "2018-01-01" );
                        if(isInserted == true)
                            Toast.makeText(Vis.this,"Data Inserted",Toast.LENGTH_LONG).show();
                        else
                            Toast.makeText(Vis.this,"Data not Inserted",Toast.LENGTH_LONG).show();
                    }
                }
        );
    }

    public  void DeleteData() {
        btnDelete.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        boolean isDeleted= myDb.deleteData();

                        if(isDeleted == true)
                            Toast.makeText(Vis.this,"Data Deleted" ,Toast.LENGTH_LONG).show();
                        else
                            Toast.makeText(Vis.this,"Data not Deleted",Toast.LENGTH_LONG).show();
                    }
                }
        );
    }

    public void viewAll() {
        btnViewAll.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                       Cursor res = myDb.getDateReads("123456789");
                       // Cursor res = myDb.getAllData();
                        if(res.getCount() == 0) {
                            // show message
                            showMessage("Error","Nothing found");
                            return;
                        }

                        StringBuffer buffer = new StringBuffer();
                        while (res.moveToNext()) {
                            buffer.append("READ :"+ res.getString(2)+"\n");
                            buffer.append("Date :"+ res.getString(3)+"\n");
                            //buffer.append("Read :"+ res.getString(3)+"\n\n");
                        }

                        // Show all data
                        showMessage("Data",buffer.toString());
                    }
                }
        );
    }

    public void showMessage(String title,String Message){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(Message);
        builder.show();
    }

    public void openFinal (View v) {
        Intent finalIntent = new Intent(this,Final.class);
        startActivity(finalIntent);
        //String email=mAuth.getCurrentUser().getEmail();
        String meterTeam="Metersteam@sse.com";
        String subject="Read Submission for MPRN:"+MPRN_CON;
        String mprn=MPRN_CON;
        String reading=READING;
        String date=DATE;
        String message = "Hi Team!\n A new meter read for MPRN:"+mprn+" has been submitted.\n\nREADING:"+reading+"\nDATE:"+date+"\n\nThanks";

        Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
        emailIntent.setType("message/rfc822");
        emailIntent.setData(Uri.parse("mailto:"+meterTeam));
        emailIntent.putExtra(Intent.EXTRA_SUBJECT,subject);
        emailIntent.putExtra(Intent.EXTRA_TEXT,message);
        startActivity(Intent.createChooser(emailIntent,"Send a mail to the meter team...."));


    }
    }

