package com.example.market4me;

import android.content.Context;
import android.content.Intent;

import androidx.fragment.app.Fragment;

import com.example.market4me.models.Recipe;

public class DisplayRecipeActivity extends SingleFragmentActivity {

    public static final String EXTRA_RECIPE_OBJECT = "pass.recipe.object.from.recyclerview";

    @Override
    protected Fragment createFragment() {
        return new DisplayRecipeFragment();
    }


    // intent encapsulado hacia esta activity
    public static Intent newIntent(Context context, Recipe recipe) {

        Intent intent = new Intent(context, DisplayRecipeActivity.class);
        intent.putExtra(EXTRA_RECIPE_OBJECT,recipe);

        return intent;
    }
}
