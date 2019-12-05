package com.cadeluca.billboard;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateFormat;
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

import java.util.UUID;


public class BillFragment extends Fragment {

    private static final String ARG_BILL_ID = "bill_id";
    private static final String DIALOG_DELETE = "dialog_delete";
    private static final int REQUEST_DELETE = 3;
    private Bill mBill;
    private EditText mTitleField;
    private Button mDateButton;
    private Button mSaveEditsButton;
    private EditText mPriceInput;


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
        View v = inflater.inflate(R.layout.fragment_price, container, false);
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

        mDateButton = (Button) v.findViewById(R.id.price_date);
        updateDate();

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
        Log.d("myTag", "on pause called");
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
        mDateButton.setText(mBill.getDueDate().toString());
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_price, menu);
    }

    private String getPriceReport() {
        String dateFormat = "EEE, MMM dd";
        String dateString = DateFormat.format(dateFormat, mBill.getDueDate()).toString();
        String report = "temporary report string";
        return report;
    }


    /**
     * Takes in an input string and coverts it to a $_.__ formatted string
     * @param digits the string (from charSequence) from a view/edit
     * @return formatted string
     */
//    private String addCurrencySign(String digits) {
//        String amount = "$";
//        // remove any non numeric chars
//        digits = digits.replace(".", "");
//
//        // Amount length greater than 2 means we need to add a decimal point
//        if (digits.length() > 2) {
//            String dollar = digits.substring(0, digits.length() - 2); // Pound part
//            String cents = digits.substring(digits.length() - 2); // Pence part
//            amount += dollar + "." + cents;
//        }
//        else if (digits.length() == 1) {
//            amount += "0.0" + digits;
//        }
//        else if (digits.length() == 2) {
//            amount += "0." + digits;
//        }
//
//        return amount;
//    }

}
