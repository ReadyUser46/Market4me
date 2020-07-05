package com.torkdev.market4me;

import androidx.fragment.app.Fragment;

public class HomeDashBoardActivity extends SingleFragmentActivity {


    @Override
    protected Fragment createFragment() {
        return HomeDashBoardFragment.newInstance();
    }
}
