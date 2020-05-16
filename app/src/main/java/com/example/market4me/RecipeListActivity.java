package com.example.market4me;

import android.content.Context;
import android.content.Intent;

import androidx.fragment.app.Fragment;

public class RecipeListActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment() {
        return new RecipeListFragment();
    }

    // intent encapsulado hacia esta activity
    public static Intent newIntent(Context context) {
        return new Intent(context, RecipeListActivity.class);
    }
}
