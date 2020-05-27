package com.example.market4me;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import com.example.market4me.models.Recipe;

public class DisplayRecipeActivity extends SingleFragmentActivity {

    public static final String EXTRA_RECIPE_OBJECT = "pass.recipe.object.from.recyclerview";
    public static final String EXTRA_RECIPE_ID = "pass.recipe.id.from.recyclerview";

    @Override
    protected Fragment createFragment() {
        // nos comunicamos con arguments (bundle)
        Recipe recipe = (Recipe) getIntent().getSerializableExtra(EXTRA_RECIPE_OBJECT);
        Bundle bundle = getIntent().getExtras();
        String recipeId = bundle.getString(EXTRA_RECIPE_ID);
        return DisplayRecipeFragment.newInstance(recipe, recipeId);
    }


    // intent encapsulado hacia esta activity
    public static Intent newIntent(Context context, Recipe recipe, String recipeId) {

        Intent intent = new Intent(context, DisplayRecipeActivity.class);
        intent.putExtra(EXTRA_RECIPE_OBJECT, recipe);
        intent.putExtra(EXTRA_RECIPE_ID, recipeId);

        return intent;
    }
}
