package com.cadeluca.billboard;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import java.util.List;
import java.util.UUID;

public class BillPagerActivity extends AppCompatActivity {

    private static final String EXTRA_BILL_ID = "com.cadeluca.billapp.bill_id";
    private ViewPager mViewPager;
    private List<Bill> mBills;

    public static Intent newIntent(Context packageContext, UUID billId) {
        Intent intent = new Intent(packageContext, BillPagerActivity.class);
        intent.putExtra(EXTRA_BILL_ID, billId);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bill_pager);

        UUID billId = (UUID) getIntent().getSerializableExtra(EXTRA_BILL_ID);

        mViewPager = findViewById(R.id.price_view_pager);

        mBills = BillLab.get(this).getBills();
        FragmentManager fragmentManager = getSupportFragmentManager();
        mViewPager.setAdapter(new FragmentStatePagerAdapter(fragmentManager) {

            @NonNull
            @Override
            public Fragment getItem(int position) {
                Bill bill = mBills.get(position);
                return BillFragment.newInstance(bill.getId());
            }

            /**
             * Get count of bills
             * @return integer count
             */
            @Override
            public int getCount() {
                return mBills.size();
            }
        });

        for (int i = 0; i < mBills.size(); i++) {
            if (mBills.get(i).getId().equals(billId)) {
                mViewPager.setCurrentItem(i);
                break;
            }
        }
    }
}
