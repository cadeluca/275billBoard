package com.cadeluca.billboard;

import androidx.fragment.app.Fragment;

public class NewBillActivity extends SingleFragmentActivity {
    @Override
    protected Fragment createFragment() {
        return new NewBillFragment();
    }

}
