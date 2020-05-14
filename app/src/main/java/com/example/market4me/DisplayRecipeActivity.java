package com.example.market4me;

import androidx.fragment.app.Fragment;

public class DisplayRecipeActivity extends SingleFragmentActivity {
    @Override
    protected Fragment createFragment() {
        return new DisplayRecipeFragment();
    }
}
