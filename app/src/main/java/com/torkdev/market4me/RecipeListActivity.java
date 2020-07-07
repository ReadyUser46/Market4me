package com.torkdev.market4me;

import android.content.Context;
import android.content.Intent;

import androidx.fragment.app.Fragment;

public class RecipeListActivity extends SingleFragmentActivity {


    @Override
    protected Fragment createFragment() {
        return RecipeListFragment.newInstance();
    }

    // intent encapsulado hacia esta activity
    public static Intent newIntent(Context context) {
        return new Intent(context, RecipeListActivity.class);
    }
}
