package com.cadeluca.billboard.dashboard;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cadeluca.billboard.Bill;
import com.cadeluca.billboard.BillLab;
import com.cadeluca.billboard.BillPagerActivity;
import com.cadeluca.billboard.NewBillActivity;
import com.cadeluca.billboard.R;
import com.google.android.material.card.MaterialCardView;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.List;

public class DashboardFragment extends Fragment {
    private RecyclerView mPriceRecyclerView;
    private boolean mSubtitleVisible;
    private static final String SAVED_SUBTITLE_VISIBLE = "subtitle";
    private BillAdapter mAdapter;
    private boolean mIsSort;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_dashboard, container, false);
        if (savedInstanceState != null) {
            mSubtitleVisible = savedInstanceState.getBoolean(SAVED_SUBTITLE_VISIBLE);
        }
        mPriceRecyclerView = root.findViewById(R.id.price_recycler_view);
        mPriceRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        updateUI();
        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        updateUI();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            return;
        }
    }

    @Override
    public void onSaveInstanceState (Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(SAVED_SUBTITLE_VISIBLE, mSubtitleVisible);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add_price:
                Intent intent = new Intent(getContext(), NewBillActivity.class);
                startActivity(intent);
                return true;
            case R.id.price_sort:
                mIsSort = !mIsSort;
                getActivity().invalidateOptionsMenu();
                updateUI();
                return true;
            case R.id.show_subtitle:
                mSubtitleVisible = !mSubtitleVisible;
                getActivity().invalidateOptionsMenu();
                updateSubtitle();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_dashboard, menu);

        MenuItem subtitleItem = menu.findItem(R.id.show_subtitle);
        if (mSubtitleVisible) {
            subtitleItem.setTitle(R.string.hide_subtitle);
        } else {
            subtitleItem.setTitle(R.string.show_subtitle);
        }

        MenuItem sortTitleItem =  menu.findItem(R.id.price_sort);
        if (mIsSort) {
            sortTitleItem.setTitle(R.string.default_order);
        } else {
            sortTitleItem.setTitle(R.string.price_sort);
        }
    }

    private void updateSubtitle() {
        BillLab priceLab = BillLab.get(getActivity());
        int priceCount = priceLab.getBills().size();
        String subtitle = getString(R.string.subtitle_format, priceCount);

        if (!mSubtitleVisible) {
            subtitle = null;
        }
        AppCompatActivity activity = (AppCompatActivity)getActivity();
        activity.getSupportActionBar().setSubtitle(subtitle);
    }

    private void updateUI() {
        BillLab billLab = BillLab.get(getActivity());
        List<Bill> bills;

        // todo swtich
        if (mIsSort) {
            bills = billLab.getBills("amount");
        } else {
            bills = billLab.getBills();
        }

        if (mAdapter == null) {
            mAdapter = new BillAdapter(bills);
            mPriceRecyclerView.setAdapter(mAdapter);
        } else {
            mAdapter.setBills(bills);
            mAdapter.notifyDataSetChanged();
        }
    }

    private class BillHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {

        private Bill mBill;
        private TextView mTitleTextView;
        private TextView mDateTextView;
        private TextView mAmountTextView;
        private MaterialCardView mMaterialCardView;

        public BillHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.list_item_price, parent, false));
            itemView.setOnClickListener(this);

            mMaterialCardView = itemView.findViewById(R.id.bill_card);
            mTitleTextView = itemView.findViewById(R.id.bill_title);
            mDateTextView = itemView.findViewById(R.id.bill_due_date);
            mAmountTextView = itemView.findViewById(R.id.bill_amount);

            mMaterialCardView.setOnClickListener(view -> {
                Intent intent = BillPagerActivity.newIntent(getActivity(), mBill.getId());
                startActivity(intent);
            });
        }

        DecimalFormat df = new DecimalFormat("0.00");
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEEE, MMM dd, YYYY");

        public void bind(Bill bill) {
            mBill = bill;
            mTitleTextView.setText(mBill.getTitle());
            String dueString = "Due "+simpleDateFormat.format(mBill.getDueDate());
            mDateTextView.setText(dueString);
            String amountStr = "$"+df.format(mBill.getAmountDue());
            mAmountTextView.setText(amountStr);
        }

        @Override
        public void onClick(View view) {
            Intent intent = BillPagerActivity.newIntent(getActivity(), mBill.getId());
            startActivity(intent);
        }
    }

    private class BillAdapter extends RecyclerView.Adapter<BillHolder> {

        private List<Bill> mBills;

        public BillAdapter(List<Bill> bills) {
            mBills = bills;
        }

        @Override
        public BillHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            return new BillHolder(layoutInflater, parent);
        }

        @Override
        public void onBindViewHolder(BillHolder holder, int position) {
            Bill bill = mBills.get(position);
            holder.bind(bill);
        }

        @Override
        public int getItemCount() {
            return mBills.size();
        }


        public void setBills(List<Bill> bills) {
            mBills = bills;
        }
    }
}