package com.example.derek.meterreads;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
/**
 *
 * The DataBaseBuild is a data base helper which creates a SQLite database, a meter_read table and method to query the table
 *
 *
 * @link {@link Confirm}
 *
 * @author – Derek Aherne
 * @version – 25/04/2018
 */
public class DataBaseBuild extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "MeterReads.db";
    public static final String TABLE_NAME = "meter_table";
    public static final String COL_1 = "EVENT_ID";
    public static final String COL_2 = "MPRN";
    public static final String COL_3 = "READING";
    public static final String COL_4 = "READDATE";

    public static final String TAG = DataBaseBuild.class.getSimpleName(); //Log Tag


    public DataBaseBuild(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    /**
     *
     * The onCreate method creates a meter_read table
     *
     * @author – Derek Aherne
     * @version – 25/04/2018
     * @param - SQLiteDatabase db A database instance
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_NAME + " (EVENT_ID INTEGER PRIMARY KEY AUTOINCREMENT,MPRN STRING,READING STRING,READDATE STRING)");
    }

    /**
     *
     * The onUpgrade method creates query to drop the table if it exists after an upgrade
     *
     * @author – Derek Aherne
     * @version – 25/04/2018
     * @param - SQLiteDatabase db A database instance
     * @param - int oldVersion
     * @param - int newVersion
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
    /**
     *
     * The insertData method inserts data into the meter_read table
     *
     * @author – Derek Aherne
     * @version – 25/04/2018
     * @param - String MPRN Meter Point Reference Number
     * @param - String READING A meter reading
     * @param - String READDATE The reading date
     * @return - boolean
     */
    public boolean insertData(String MPRN, String READING, String READDATE) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_2, MPRN);
        contentValues.put(COL_3, READING);
        contentValues.put(COL_4, READDATE);
        long result = db.insert(TABLE_NAME, null, contentValues);
        if (result == -1)
            return false;
        else
            return true;
    }
    /**
     *
     * The getAllData returns a cursor of all data in the meter_read table
     *
     * @author – Derek Aherne
     * @version – 25/04/2018
     * @return Cursor The results of the query
     */
    public Cursor getAllData() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from meter_table" , null);
        return res;
    }
    /**
     *
     * The getDateReads returns an aggregated view of the table for a given MPRN
     *
     * @author – Derek Aherne
     * @version – 25/04/2018
     * @param  - String mprn
     * @return Cursor The results of the query
     */
    public Cursor getDateReads(String mprn) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select sum(cast(READING as int)) as READING,substr( READDATE, 6, 2 ) as READ_DATE from meter_table where MPRN="+mprn+" group by READ_DATE",null);
        return res;
    }
    /**
     *
     * The getAllData method deletes all data in the meter_read table
     *
     * @author – Derek Aherne
     * @version – 25/04/2018
     * @return boolean
     */
    public boolean deleteData(){
        SQLiteDatabase db = this.getWritableDatabase();
        long result = db.delete(TABLE_NAME, null,null);
        if (result == -1)
            return false;
        else
            return true;
    }
}
