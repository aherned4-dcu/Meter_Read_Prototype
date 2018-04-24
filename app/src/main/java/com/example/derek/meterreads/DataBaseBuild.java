package com.example.derek.meterreads;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DataBaseBuild extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "MeterReads.db";
    public static final String TABLE_NAME = "meter_table";
    public static final String COL_1 = "EVENT_ID";
    public static final String COL_2 = "MPRN";
    public static final String COL_3 = "READING";
    public static final String COL_4 = "DATE";


    public DataBaseBuild(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_NAME + " (EVENT_ID INTEGER PRIMARY KEY AUTOINCREMENT,MPRN STRING,READING STRING,DATE STRING)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public boolean insertData(String MPRN, String READING, String DATE) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_2, MPRN);
        contentValues.put(COL_3, READING);
        contentValues.put(COL_4, DATE);
        long result = db.insert(TABLE_NAME, null, contentValues);
        if (result == -1)
            return false;
        else
            return true;
    }

    public Cursor getAllData() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select meter_table.READING,meter_table.DATE from " + TABLE_NAME, null);
        return res;
    }

    public Cursor getDateReads(String mprn) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from meter_table where MPRN ="+mprn,null);;
        return res;
    }

    public boolean deleteData(){
        SQLiteDatabase db = this.getWritableDatabase();
        long result = db.delete(TABLE_NAME, null,null);
        if (result == -1)
            return false;
        else
            return true;
    }
}
