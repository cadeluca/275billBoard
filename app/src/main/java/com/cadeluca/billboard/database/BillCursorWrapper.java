package com.cadeluca.billboard.database;

import android.database.Cursor;
import android.database.CursorWrapper;

import com.cadeluca.billboard.Bill;

import java.util.Date;
import java.util.UUID;

import static com.cadeluca.billboard.database.BillDbSchema.BillTable.*;

public class BillCursorWrapper extends CursorWrapper {

    public BillCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    public Bill getBill() {
        String uuidString = getString(getColumnIndex(Cols.UUID));
        String title = getString(getColumnIndex(Cols.TITLE));
        long date = getLong(getColumnIndex(Cols.DATE));
        float amount = getFloat(getColumnIndex(Cols.AMOUNT));
        boolean paid = getInt(getColumnIndex(Cols.PAID)) == 1;

        Bill bill = new Bill(UUID.fromString(uuidString));
        bill.setTitle(title);
        bill.setDueDate(new Date(date));
        bill.setAmountDue(amount);
        bill.setPaid(paid);

        return bill;
    }
}