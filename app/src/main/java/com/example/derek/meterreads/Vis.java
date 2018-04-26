package com.example.derek.meterreads;

import android.app.AlertDialog;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.Pair;
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

    private ArrayList<Pair<String,String>> barEntries = new ArrayList<>();
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
        barchart = findViewById(R.id.chart1);
        barchart.setDrawBarShadow(false);
        barchart.setPinchZoom(false);
        barchart.setDescription(null);
        barchart.setDrawGridBackground(false);
        String mprn=getIntent().getStringExtra(Constants.MPRN_CON);
        new ReadData().execute(mprn);


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
                        boolean isInserted = myDb.insertData(Constants.MPRN_CON, Constants.READING, Constants.DATE );
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
                       Cursor res = myDb.getAllData();

                        if(res.getCount() == 0) {
                            // show message
                            showMessage("Error","Nothing found");
                            return;
                        }

                        StringBuffer buffer = new StringBuffer();

                        int read_index=res.getColumnIndex("READING");
                        int readDate_index=res.getColumnIndex("READDATE");

                        while (res.moveToNext()) {
                            String col_read=res.getString(read_index);
                            String col_date=res.getString(readDate_index);
                            buffer.append("READ :"+ col_read+"\n");
                            buffer.append("Date :"+ col_date+"\n");


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

        Intent intent=getIntent();
        String meterTeam="Metersteam@sse.com";
        String subject="Read Submission for MPRN:123456789";//+intent.getStringExtra(Constants.MPRN_CON);
        String mprn=intent.getStringExtra(Constants.MPRN_CON);
        String reading=intent.getStringExtra(Constants.READING);
        String date=intent.getStringExtra(Constants.DATE);
        String message = "Hi Team!\n A new meter read for MPRN:"+mprn+" has been submitted.\n\nREADING:"+reading+"\nDATE:"+date+"\n\nThanks";

        Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
        emailIntent.setType("message/rfc822");
        emailIntent.setData(Uri.parse("mailto:"+meterTeam));
        emailIntent.putExtra(Intent.EXTRA_SUBJECT,subject);
        emailIntent.putExtra(Intent.EXTRA_TEXT,message);
        startActivityForResult(Intent.createChooser(emailIntent,"Send a mail to the meter team...."),1001);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1001 && resultCode==RESULT_OK){
            Intent finalIntent = new Intent(this,Final.class);
            startActivity(finalIntent);
        }
    }

    class ReadData extends AsyncTask<String,Void,Boolean>{

        @Override
        protected Boolean doInBackground(String... strings) {
            String mprn=strings[0];
            Cursor res = myDb.getDateReads(mprn);
            // Cursor res = myDb.getAllData();
            if(res.getCount() == 0) {

                return true;
            }

            int read_index=res.getColumnIndex("READ");
            int readDate_index=res.getColumnIndex("READDATE");
            barEntries.clear();
            while (res.moveToNext()) {
                String col_read=res.getString(read_index);
                String col_date=res.getString(readDate_index);
                barEntries.add(new Pair<String, String>(col_read,col_date));
            }
            return false;
        }

        @Override
        protected void onPostExecute(Boolean aVoid) {
            super.onPostExecute(aVoid);
            if(aVoid){
                ArrayList<BarEntry> barChartEntries = new ArrayList<>();
                //for (Pair<String,String> item:barEntries){
                 //   barChartEntries.add(new BarEntry(Float.parseFloat(item.first),Float.parseFloat(item.second)));
                //}
                barChartEntries.add(new BarEntry(1,564f));
                barChartEntries.add(new BarEntry(2,560f));
                barChartEntries.add(new BarEntry(3,400f));
                barChartEntries.add(new BarEntry(4,469f));
                barChartEntries.add(new BarEntry(5,550f));
                barChartEntries.add(new BarEntry(6,680f));

                int color = ContextCompat.getColor(Vis.this, R.color.sseblue);
                BarDataSet barDataSet = new BarDataSet(barChartEntries,"Monthly Energy Usage (kWh)");
                barDataSet.setColors(color);

                BarData data = new BarData(barDataSet);

                barchart.setData(data);


                XAxis xAxis = barchart.getXAxis();
                String[] months = new String[]{"","Jan","Feb","Mar","Apr","May","Jun"};
                xAxis.setValueFormatter(new MyXAxisValueFormatter(months));
                xAxis.setPosition(XAxis.XAxisPosition.BOTH_SIDED);
                xAxis.setDrawGridLines(false);
                xAxis.setGranularity(1);
            }else{
                showMessage("Error","Nothing found");
            }
        }
    }
    }

