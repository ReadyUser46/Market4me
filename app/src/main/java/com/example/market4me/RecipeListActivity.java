package com.example.market4me;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.fragment.app.Fragment;

public class RecipeListActivity extends SingleFragmentActivity {


    @Override
    protected Fragment createFragment() {
        String userId = (mUserId == null) ? "lTp9MOAdZ7UKMZyxjH2AxmGWlFp2" : mUserId;
        Log.i("patapum", "User Id from last time: " + userId);

        return RecipeListFragment.newInstance(userId);
    }

    // intent encapsulado hacia esta activity
    public static Intent newIntent(Context context) {
        return new Intent(context, RecipeListActivity.class);
    }
}
