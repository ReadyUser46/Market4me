package com.torkdev.market4me;

import android.content.Context;
import android.content.Intent;

import androidx.fragment.app.Fragment;

import com.torkdev.market4me.models.Recipe;

public class NewRecipeActivity extends SingleFragmentActivity {

    public static final String EXTRA_RECIPE_OBJECT2 = "pass.recipe.object.from.displayRecipe";
    public static final String EXTRA_RECIPE_ID2 = "pass.recipe.id.from.displayRecipe";


    @Override
    protected Fragment createFragment() {
        return NewRecipeFragment.newInstance(mUserId);
    }

    // intent encapsulado hacia esta activity
    public static Intent newIntent(Context context, Recipe recipe, String recipeId) {

        Intent intent = new Intent(context, NewRecipeActivity.class);
        intent.putExtra(EXTRA_RECIPE_OBJECT2, recipe);
        intent.putExtra(EXTRA_RECIPE_ID2, recipeId);

        return intent;
    }


}
