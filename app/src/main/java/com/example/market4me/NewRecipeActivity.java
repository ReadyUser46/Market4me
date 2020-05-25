package com.example.market4me;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.market4me.models.Recipe;

public class NewRecipeActivity extends SingleFragmentActivity {

    public static final String EXTRA_RECIPE_OBJECT2 = "pass.recipe.object.from.displayRecipe";

    @Override
    protected Fragment createFragment() {
        return new NewRecipeFragment();
    }

    // intent encapsulado hacia esta activity
    public static Intent newIntent(Context context, Recipe recipe) {

        Intent intent = new Intent(context, DisplayRecipeActivity.class);
        intent.putExtra(EXTRA_RECIPE_OBJECT2, recipe);

        return intent;
    }


}
