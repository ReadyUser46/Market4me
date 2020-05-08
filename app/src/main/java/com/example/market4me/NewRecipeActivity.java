package com.example.market4me;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

public class NewRecipeActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_container);


        /*Al iniciar la activity, se infla un layout que tiene un contenedor para fragments
        *Para mostrar el fragment, hay que añadirlo a su contenedor con add y darselo al fragment manager
        *Si quisieramos mostrar fragments en un viewpager, tendriamos que crear un adapter y pasarle el fragment manager
        *Y este adaptaer, darselo al viewpager con set adapter.
        * e.g. mViewPager = (ViewPager) findViewById(R.id.crime_view_pager);

        mCrimes = CrimeLab.get(this).getCrimes();
        FragmentManager fragmentManager = getSupportFragmentManager();
        mViewPager.setAdapter(new FragmentStatePagerAdapter(fragmentManager) */

        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.fragment_container);
        //Fragment fragment = new RecipeFragment();
        if(fragment==null) {
            fragment = new NewRecipeFragment();
            fm.beginTransaction().add(R.id.fragment_container, fragment).commit();
        }
    }





}
