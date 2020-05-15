package com.example.market4me;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

public class NewRecipeActivity extends SingleFragmentActivity {


    @Override
    protected Fragment createFragment() {
        return new NewRecipeFragment();
    }


}
