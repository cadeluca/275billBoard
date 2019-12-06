package com.cadeluca.billboard;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;


public class NewBillFragment extends Fragment {

    DecimalFormat df = new DecimalFormat("0.00");
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEEE, MMM dd, YYYY");

    private static final String DIALOG_DATE = "DialogDate";
    private static final int REQUEST_DATE = 0;
    private Button mSaveBillButton;
    private Bill mBill;
    private Button mDateButton;
    private EditText mTitleField;
    private EditText mAmountInput;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        Objects.requireNonNull(Objects.requireNonNull(activity).getSupportActionBar()).setTitle("Add a Bill"); //todo: make a string
        mBill = new Bill();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_new_bill, container, false);

        mTitleField = v.findViewById(R.id.price_title);
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
            FragmentManager manager = NewBillFragment.this.getFragmentManager();
            DatePickerFragment dialog = DatePickerFragment
                    .newInstance(mBill.getDueDate());
            dialog.setTargetFragment(NewBillFragment.this, REQUEST_DATE);
            dialog.show(manager, DIALOG_DATE);
        });



        mSaveBillButton = v.findViewById(R.id.price_save);
        mSaveBillButton.setOnClickListener(view -> {
            // add the bill to the BillLab
            BillLab.get(getActivity()).addBill(mBill);
            Toast toast = Toast.makeText(getContext(), "Added bill successfully!", Toast.LENGTH_SHORT); // todo: add string
            toast.show();
            // go to bill list after adding a bill
//            Navigation.findNavController(v).navigate(R.id.action_add_bill_to_nav_list2);
            // go back to listFragment
            getActivity().finish();

        });

        mAmountInput = v.findViewById(R.id.price_input);
        mAmountInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() > 0) {
                    mBill.setAmountDue(Float.parseFloat(charSequence.toString()));
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        return v;
    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            return;
        } else if (requestCode == REQUEST_DATE) {
            Date date = (Date) data
                    .getSerializableExtra(DatePickerFragment.EXTRA_DATE);
            mBill.setDueDate(date);
            updateDate();
        }
    }

    private void updateDate() {
        mDateButton.setText(simpleDateFormat.format(mBill.getDueDate()));
    }
}
