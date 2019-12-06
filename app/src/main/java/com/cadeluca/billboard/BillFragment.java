package com.cadeluca.billboard;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.UUID;


public class BillFragment extends Fragment {

    private static final String ARG_BILL_ID = "bill_id";
    private static final String DIALOG_DELETE = "dialog_delete";
    private static final String DIALOG_DATE = "DialogDate";
    private static final int REQUEST_DATE = 0;
    private static final int REQUEST_DELETE = 3;
    private Bill mBill;
    private EditText mTitleField;
    private Button mDateButton;
    private Button mSaveEditsButton;
    private EditText mPriceInput;

    DecimalFormat df = new DecimalFormat("0.00");
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEEE, MMM dd, YYYY");


    public static BillFragment newInstance(UUID priceId) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_BILL_ID, priceId);
        BillFragment fragment = new BillFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        UUID priceId = (UUID) getArguments().getSerializable(ARG_BILL_ID);
        mBill = BillLab.get(getActivity()).getBill(priceId);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_bill, container, false);
        mTitleField = (EditText) v.findViewById(R.id.price_title);
        mTitleField.setText(mBill.getTitle());
        mTitleField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mBill.setTitle(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        mDateButton = v.findViewById(R.id.price_date);
        updateDate();
        mDateButton.setOnClickListener(v14 -> {
            FragmentManager manager = BillFragment.this.getFragmentManager();
            DatePickerFragment dialog = DatePickerFragment
                    .newInstance(mBill.getDueDate());
            dialog.setTargetFragment(BillFragment.this, REQUEST_DATE);
            dialog.show(manager, DIALOG_DATE);
        });

        mSaveEditsButton = (Button) v.findViewById(R.id.price_edit);
        mSaveEditsButton.setOnClickListener(view -> {
            Log.d("myTag", "Clicked saved edits");
            BillLab.get(getActivity()).updateBill(mBill);
            Toast toast = Toast.makeText(getContext(), "Edited price successfully!", Toast.LENGTH_SHORT);
            toast.show();
        });

        mPriceInput = (EditText) v.findViewById(R.id.price_input);
        mPriceInput.setText(Float.toString(mBill.getAmountDue()));
        mPriceInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                mBill.setAmountDue(Float.parseFloat(charSequence.toString()));
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        return v;
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            return;
        } else if (requestCode == REQUEST_DELETE) {
            BillLab.get(getActivity()).deleteBill(mBill);
            getActivity().finish();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.delete_price:
                FragmentManager manager = getFragmentManager();
                DeleteDialogFragment dialog = new DeleteDialogFragment();
                dialog.setTargetFragment(BillFragment.this, REQUEST_DELETE);
                dialog.show(manager, DIALOG_DELETE);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void updateDate() {
        mDateButton.setText(simpleDateFormat.format(mBill.getDueDate()));
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_price, menu);
    }
}
