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

/**
 * The Vis class creates the functionality for the visual and statistics activity
 * Instantiates a DataBaseBuild instance.
 * Instantiates a BarChart instance
 */
public class Vis extends BaseActivity {
    /* Citation: Class contains code adapted from
     * URL: https://github.com/PhilJay/MPAndroidChart
     * Permission: MIT Licence Retrieved on:15th April 2018  */
    TextView editTextReading, editTextDate, editTextMPRN;
    BarChart barchart;
    Button btnAddData,btnViewAll,btnDelete;
    DataBaseBuild myDb;
    String today=getDate();
    public static final String TAG = Vis.class.getSimpleName(); //Log Tag


    private ArrayList<Pair<String,String>> barEntries = new ArrayList<>(); //to store bar chart entries
    @Override
    /**
     * The onCreate method set the content to activity_vis.
     * Gets an instance of the DataBaseBuild and BarChart
     * Formats the BarChart. Executes queries
     *
     * @param savedInstanceState
     */
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
        myDb = new DataBaseBuild(this); //give me the data base

        //create a bar chart and format it
        barchart = findViewById(R.id.chart1);
        barchart.setDrawBarShadow(false);
        barchart.setPinchZoom(false);
        barchart.setDescription(null);
        barchart.setDrawGridBackground(false);

        String mprn=getIntent().getStringExtra(Constants.MPRN_CON); //get the MPRN for SQL query
        new ReadData().execute(mprn); //execute the query


    }

    /**
     * A method to return to Home.java
     * @param v
     */
    public void openHome (View v) {
        goHome();
    }

    /**
     *
     */
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

    /**
     * A method to insert mock data into meter_table once the add data button is pressed
     */
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
    /**
     * A method to delete data from meter_table once the delete data button is pressed
     */
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
    /**
     * A method to select all data from meter_table once the show all data button is pressed
     */
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

    /**
     * A method to show a message
     *
     * @param title
     * @param Message
     */
    public void showMessage(String title,String Message){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(Message);
        builder.show();
    }

    /**
     * A method to call an implicit intent for an email app. Email is populated with captured data
     * @param v
     */
    public void openFinal (View v) {

        Intent intent=getIntent();
        String meterTeam=getString(R.string.team_email); //send it to the Meter Read Team
        String mprn=intent.getStringExtra(Constants.MPRN_CON); //get the MPRN
        String subject=getString(R.string.sub_email)+mprn; //set the subject
        String reading=intent.getStringExtra(Constants.READING); //get the reading
        String date=intent.getStringExtra(Constants.DATE); //get the date
        String message = getString(R.string.email_body1)+mprn+"\n"+getString(R.string.email_body2)+reading+"\n"+getString(R.string.email_body3)+date+"\n"+getString(R.string.email_body4); //for the body
        Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
        emailIntent.setType("message/rfc822");
        emailIntent.setData(Uri.parse("mailto:"+meterTeam));
        emailIntent.putExtra(Intent.EXTRA_SUBJECT,subject);
        emailIntent.putExtra(Intent.EXTRA_TEXT,message);
        startActivityForResult(Intent.createChooser(emailIntent,getString(R.string.email_chooser_msg)),1001);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) { //start final activity if email is sent
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1001 && resultCode==RESULT_OK){
            Intent finalIntent = new Intent(this,Final.class);
            startActivity(finalIntent);
        }
    }

    /**
     * Get the SQL query results cursor and map the columns to the value of bar entries as String Pairs
     */
    class ReadData extends AsyncTask<String,Void,Boolean>{

        @Override
        protected Boolean doInBackground(String... strings) {
            String mprn=strings[0]; //user MPRN
            Cursor res = myDb.getDateReads(mprn); //get aggregated data for the user MPRN

            if(res.getCount() == 0) {

                return false;
            }

            int read_index=res.getColumnIndex("READING");//get the column position
            int readDate_index=res.getColumnIndex("READ_DATE");//get the column position
            barEntries.clear();
            while (res.moveToNext()) {
                String col_read=res.getString(read_index); //give me readings
                String col_date=res.getString(readDate_index); //give me dates
                barEntries.add(new Pair<String, String>(col_read,col_date));
            }
            return true;
        }

        @Override
        /**
         * Create the bar chart entries by parsing bar entries from string to float and then maps the floats to descriptive months to be used for x-axis
         */
        protected void onPostExecute(Boolean aVoid) {
            super.onPostExecute(aVoid);
            if(aVoid){
                ArrayList<BarEntry> barChartEntries = new ArrayList<>(); //store the entries
                for (Pair<String,String> item:barEntries){ //build the bar chart
                    barChartEntries.add(new BarEntry(Float.parseFloat(item.second),Float.parseFloat(item.first)));
                }

                //Uncomment the below for testing..
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

                barchart.setData(data); //set the data


                XAxis xAxis = barchart.getXAxis(); //create an xaxis
                // map bar chart entries to months strings
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

