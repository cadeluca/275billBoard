package com.cadeluca.billboard.home;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.cadeluca.billboard.Bill;
import com.cadeluca.billboard.BillLab;
import com.cadeluca.billboard.R;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class HomeFragment extends Fragment {
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        final TextView textViewNext = root.findViewById(R.id.next_bill);
        final TextView textViewTotal = root.findViewById(R.id.total_bills_and_owed);
        final TextView textViewWeek = root.findViewById(R.id.this_week);

        DecimalFormat df = new DecimalFormat("0.00");
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEEE, MMM dd, YYYY");

        List<Bill> b = getBills();
        Bill testbill = b.get(0);

        // next due bill
        String next = "Your next due bill is: " + testbill.getTitle() + "; $" + df.format(testbill.getAmountDue()) + " due by " + simpleDateFormat.format(testbill.getDueDate());
        textViewNext.setText(next);

        // calendar logic for checking if you have bills due in the next coming weeks
        int noOfDays = 7; // one week later
        Calendar calendar = Calendar.getInstance();
        Date todaysDate = new Date();
        calendar.setTime(todaysDate);
        calendar.add(Calendar.DAY_OF_YEAR, noOfDays);
        Date weekDate = calendar.getTime();
        ArrayList<String> billArrayList = new ArrayList<>();

        // loop through bills to calc total owed and bills for this week
        float billTotalAmount = 0;
        for (int i = 0; i < b.size(); i++) {
            Bill bill = b.get(i);
            billTotalAmount += bill.getAmountDue();
            if (bill.getDueDate().compareTo(weekDate) <= 0) {
                billArrayList.add(bill.getTitle());
            }
        }

        // unpaid display
        String totalOwed = "You have " + b.size() + " bills, totaling $" + df.format(billTotalAmount) + " owed.";
        textViewTotal.setText(totalOwed);

        // upcoming week of bills
        String billsWeek  = "You have " + billArrayList.size() + " bills coming up this week. They are: ";
        billsWeek += TextUtils.join(", ", billArrayList);
        textViewWeek.setText(billsWeek);

        return root;
    }


    /**
     * Get list of bills from billLab
     * @return Bill list
     */
    private List<Bill> getBills() {
        BillLab billLab = BillLab.get(getActivity());
        return billLab.getBills();
    }
}