package com.cadeluca.billboard.home;

import android.os.Bundle;
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

    DecimalFormat df = new DecimalFormat("0.00");
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEEE, MMM dd, YYYY");


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        final TextView textView = root.findViewById(R.id.text_home);
        final TextView textView2 = root.findViewById(R.id.text_home2);
        final TextView textView3 = root.findViewById(R.id.text_home3);
        final TextView textView4 = root.findViewById(R.id.text_home4);

        List<Bill> b = getBills();
        Bill testbill = b.get(0);

        DecimalFormat df = new DecimalFormat("0.00");
        String next = "Your next due bill is: " + testbill.getTitle() + " for $" + df.format(testbill.getAmountDue()) + " by " + testbill.getDueDate();
        textView.setText(next);

        // logic for checking if you have bills due in the next coming weeks
        int noOfDays = 7; // one week later
        Calendar calendar = Calendar.getInstance();
        Date todaysDate = new Date();
        calendar.setTime(todaysDate);
        calendar.add(Calendar.DAY_OF_YEAR, noOfDays);
        Date weekDate = calendar.getTime();

        ArrayList<Bill> billArrayList = new ArrayList<>();

        int billTotal = 0;
        float billTotalAmount = 0;
        for (int i = 0; i < b.size(); i++) {
            Bill bill = b.get(i);
            if (!bill.getPaid()) {
                billTotal += 1;
                billTotalAmount += bill.getAmountDue();
            }
            if (bill.getDueDate().compareTo(weekDate) <= 0) {
                billArrayList.add(bill);
            }
        }

        String paidVsUnpaid = "You have " + b.size() + " bills, of which " + billTotal + " are unpaid with a total of $" + df.format(billTotalAmount) + " owed.";
        textView2.setText(paidVsUnpaid);

        String tv3 = "You have " + billArrayList.size() + " bills coming up this week. They are: ";

        for (Bill bil : billArrayList) {
            tv3 += ", " + bil.getTitle();
        }
        textView3.setText(tv3);

        double money = 100.00;
        String mon = "Your mysterious offshore account: $"+df.format(money);
        textView4.setText(mon);






        return root;
    }



    private List<Bill> getBills() {
        BillLab billLab = BillLab.get(getActivity());
        return billLab.getBills();
    }
}