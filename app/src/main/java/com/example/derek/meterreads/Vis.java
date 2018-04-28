package com.example.derek.meterreads;

import android.app.AlertDialog;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
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
import java.util.ArrayList;

public class Vis extends BaseActivity {
    TextView editTextReading, editTextDate, editTextMPRN;
    BarChart barchart;
    Button btnAddData,btnViewAll,btnDelete;
    DataBaseBuild myDb;
    String today=getDate();


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
                        boolean isInserted = myDb.insertData("123456789", "45758",today );
                        if(isInserted == true)
                            Toast.makeText(Vis.this, R.string.data_added ,Toast.LENGTH_LONG).show();
                        else
                            Toast.makeText(Vis.this, R.string.data_not_added,Toast.LENGTH_LONG).show();
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
                            Toast.makeText(Vis.this, R.string.data_deleted ,Toast.LENGTH_LONG).show();
                        else
                            Toast.makeText(Vis.this, R.string.data_not_delete,Toast.LENGTH_LONG).show();
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
                            showMessage(getString(R.string.error),getString(R.string.found_not));
                            return;
                        }

                        StringBuffer buffer = new StringBuffer();

                        int read_index=res.getColumnIndex("READING");
                        int readDate_index=res.getColumnIndex("READDATE");

                        while (res.moveToNext()) {
                            String col_read=res.getString(read_index);
                            String col_date=res.getString(readDate_index);
                            buffer.append(getString(R.string.read_view)+ col_read+"\n");
                            buffer.append(getString(R.string.date_view)+ col_date+"\n");
                        }

                        // Show all data
                        showMessage(getString(R.string.data),buffer.toString());
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
        String meterTeam=getString(R.string.team_email);
        String subject=getString(R.string.sub_email)+Constants.MPRN_CON;
        String mprn=intent.getStringExtra(Constants.MPRN_CON);
        String reading=intent.getStringExtra(Constants.READING);
        String date=intent.getStringExtra(Constants.DATE);
        String message = getString(R.string.email_body1)+mprn+"\n"+getString(R.string.email_body2)+reading+"\n"+getString(R.string.email_body3)+date+"\n"+getString(R.string.email_body4);
        Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
        emailIntent.setType("message/rfc822");
        emailIntent.setData(Uri.parse("mailto:"+meterTeam));
        emailIntent.putExtra(Intent.EXTRA_SUBJECT,subject);
        emailIntent.putExtra(Intent.EXTRA_TEXT,message);
        startActivityForResult(Intent.createChooser(emailIntent,getString(R.string.email_chooser_msg)),1001);

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

            if(res.getCount() == 0) {

                return false;
            }

            int read_index=res.getColumnIndex("READING");
            int readDate_index=res.getColumnIndex("READ_DATE");
            barEntries.clear();
            while (res.moveToNext()) {
                String col_read=res.getString(read_index);
                String col_date=res.getString(readDate_index);
                barEntries.add(new Pair<String, String>(col_read,col_date));
            }
            return true;
        }

        @Override
        protected void onPostExecute(Boolean aVoid) {
            super.onPostExecute(aVoid);
            if(aVoid){
                ArrayList<BarEntry> barChartEntries = new ArrayList<>();
                for (Pair<String,String> item:barEntries){
                    barChartEntries.add(new BarEntry(Float.parseFloat(item.second),Float.parseFloat(item.first)));
                }
                /*barChartEntries.add(new BarEntry(1,564f));
                barChartEntries.add(new BarEntry(2,560f));
                barChartEntries.add(new BarEntry(3,400f));
                barChartEntries.add(new BarEntry(4,469f));
                barChartEntries.add(new BarEntry(5,550f));
                barChartEntries.add(new BarEntry(6,680f));*/

                int color = ContextCompat.getColor(Vis.this, R.color.sseblue);
                BarDataSet barDataSet = new BarDataSet(barChartEntries,getString(R.string.bar_xlabel));
                barDataSet.setColors(color);

                BarData data = new BarData(barDataSet);

                barchart.setData(data);


                XAxis xAxis = barchart.getXAxis();
                String[] months = new String[]{getString(R.string.month_zero),
                        getString(R.string.jan),
                        getString(R.string.feb),
                        getString(R.string.mar),
                        getString(R.string.apr),
                        getString(R.string.may),
                        getString(R.string.jun),
                        getString(R.string.jul),
                        getString(R.string.aug),
                        getString(R.string.sep),
                        getString(R.string.oct),
                        getString(R.string.nov),
                        getString(R.string.dec)};

                xAxis.setValueFormatter(new MyXAxisValueFormatter(months));
                xAxis.setPosition(XAxis.XAxisPosition.BOTH_SIDED);
                xAxis.setDrawGridLines(false);
                xAxis.setGranularity(1);
            }else{
                showMessage(getString(R.string.error),getString(R.string.found_not));
            }
        }
    }
    }

