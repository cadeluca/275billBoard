package com.cadeluca.billboard.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.cadeluca.billboard.database.BillDbSchema.BillTable;

public class BillBaseHelper extends SQLiteOpenHelper {
    private static final int VERSION = 1;
    private static final String DATABASE_NAME = "billBase.db";

    public BillBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + BillTable.NAME + "(" +
                " _id integer primary key autoincrement, " + BillTable.Cols.UUID + ", " + BillTable.Cols.TITLE + ", " +
                BillTable.Cols.DATE + ", " +
                BillTable.Cols.AMOUNT + ", " +
                BillTable.Cols.PAID +
                ")"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }
}