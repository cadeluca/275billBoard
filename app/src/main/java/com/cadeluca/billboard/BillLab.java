package com.cadeluca.billboard;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.cadeluca.billboard.database.BillBaseHelper;
import com.cadeluca.billboard.database.BillCursorWrapper;
import com.cadeluca.billboard.database.BillDbSchema.BillTable;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.cadeluca.billboard.database.BillDbSchema.BillTable.Cols.*;

public class BillLab {
    private static BillLab sBillLab;
    private Context mContext;
    private SQLiteDatabase mDatabase;

    public static BillLab get(Context context) {
        if (sBillLab == null) {
            sBillLab = new BillLab(context);
        }

        return sBillLab;
    }

    private BillLab(Context context) {
        mContext = context.getApplicationContext();
        mDatabase = new BillBaseHelper(mContext)
                .getWritableDatabase();
    }

//    private BillLab(Context context) {
//        mContext = context.getApplicationContext();
//        mDatabase = new BillBaseHelper(mContext)
//                .getWritableDatabase();
//        for (int i = 0; i < 10; i++) {
//            Bill bill = new Bill();
//            bill.setTitle("Bill #" + i);
//            bill.setAmountDue((20/(i+1)) * 10);
//            this.addBill(bill);
//        }
//    }

    void addBill(Bill p) {
        ContentValues values = getContentValues(p);
        Log.i("myTag", "add bill called");
        mDatabase.insert(BillTable.NAME, null, values);
    }

    void deleteBill(Bill p) {
        mDatabase.delete(
                BillTable.NAME,
                BillTable.Cols.UUID + " = ?",
                new String[] {p.getId().toString()}
        );
    }


    public List<Bill> getBills() {
        List<Bill> bills = new ArrayList<>();
        try (BillCursorWrapper cursor = queryBills(null, null, null)) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                bills.add(cursor.getBill());
                cursor.moveToNext();
            }
        }
        return bills;
    }

    public List<Bill> getBills(String sortType) {
        List<Bill> bills = new ArrayList<>();
        try (BillCursorWrapper cursor = queryBills(null, null, sortType)) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                bills.add(cursor.getBill());
                cursor.moveToNext();
            }
        }
        return bills;
    }


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    Bill getBill(UUID id) {
        try (BillCursorWrapper cursor = queryBills(
                BillTable.Cols.UUID + " = ?",
                new String[]{id.toString()}, ""
        )) {
            if (cursor.getCount() == 0) {
                return null;
            }
            cursor.moveToFirst();
            return cursor.getBill();
        }
    }


    void updateBill(Bill bill) {
        String uuidString = bill.getId().toString();
        ContentValues values = getContentValues(bill);
        mDatabase.update(BillTable.NAME, values,
                BillTable.Cols.UUID + " = ?",
                new String[]{uuidString});
        Log.d("myTag", "update bill called");
    }

    private BillCursorWrapper queryBills(String whereClause, String[] whereArgs, String sortType) {
        Cursor cursor;

        if (sortType == "amount") {
            cursor = mDatabase.query(
                    BillTable.NAME,
                    null, // Columns - null selects all columns
                    whereClause,
                    whereArgs,
                    null, // groupBy
                    null, // having
                    AMOUNT
            );
        }
        // default list order; latest bill first
        else {
            cursor = mDatabase.query(
                    BillTable.NAME,
                    null, // Columns - null selects all columns
                    whereClause,
                    whereArgs,
                    null, // groupBy
                    null, // having
                    DATE+" ASC"  // orderBy
            );
        }
        return new BillCursorWrapper(cursor);
    }

    private static ContentValues getContentValues(Bill bill) {
        ContentValues values = new ContentValues();
        values.put(BillTable.Cols.UUID, bill.getId().toString());
        values.put(TITLE, bill.getTitle());
        values.put(DATE, bill.getDueDate().getTime());
        values.put(AMOUNT, bill.getAmountDue());
        // todo add paid grab
        float f = bill.getAmountDue();
        Log.d("myTag", Float.toString(f));
        return values;
    }
}
