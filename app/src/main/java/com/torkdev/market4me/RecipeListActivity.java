package com.torkdev.market4me;

import android.content.Context;
import android.content.Intent;

import androidx.fragment.app.Fragment;

public class RecipeListActivity extends SingleFragmentActivity {


    @Override
    protected Fragment createFragment() {
        //String userId = (mUserId == null) ? "lTp9MOAdZ7UKMZyxjH2AxmGWlFp2" : mUserId;

        return RecipeListFragment.newInstance();
    }

    // intent encapsulado hacia esta activity
    public static Intent newIntent(Context context) {
        return new Intent(context, RecipeListActivity.class);
    }
}
