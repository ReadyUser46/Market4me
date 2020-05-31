package com.example.market4me;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.fragment.app.Fragment;

public class RecipeListActivity extends SingleFragmentActivity {


    @Override
    protected Fragment createFragment() {
        Log.i("patapum", "User Id passed: " + mUserId);

        return RecipeListFragment.newInstance(mUserId);
    }

    // intent encapsulado hacia esta activity
    public static Intent newIntent(Context context) {
        return new Intent(context, RecipeListActivity.class);
    }
}
