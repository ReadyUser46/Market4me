package com.example.market4me;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;


public abstract class SingleFragmentActivity extends AppCompatActivity {

    private DrawerLayout mDrawer;


    protected abstract Fragment createFragment();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_frame_layout2);

        /*
        Al iniciar la activity, se infla un layout que tiene un contenedor para fragments
        *Para mostrar el fragment, hay que añadirlo a su contenedor con add y darselo al fragment manager
        *Si quisieramos mostrar fragments en un viewpager, tendriamos que crear un adapter y pasarle el fragment manager
        *Y este adaptaer, darselo al viewpager con set adapter.
        * e.g. mViewPager = (ViewPager) findViewById(R.id.crime_view_pager);
        *         mCrimes = CrimeLab.get(this).getCrimes();
        FragmentManager fragmentManager = getSupportFragmentManager();
        mViewPager.setAdapter(new FragmentStatePagerAdapter(fragmentManager)
        */

        // FRAGMENT MANAGER
        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.fragment_container2);

        if(fragment == null){
            fragment = createFragment();
            fm.beginTransaction().add(R.id.fragment_container2, fragment).commit();

        }

        // Navigation Drawer
        //mDrawer = findViewById(R.id.drawer_layout);

        // Navigation Drawer Icon (Burger)
        ActionBarDrawerToggle toggleBurger = new ActionBarDrawerToggle(
                this,
                mDrawer,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);
        //toggleBurger.syncState();
    }

    @Override
    public void onBackPressed() {
        /* Esto es para cuando pulsemos back, primero cierre el navigation drawer*/
        if (mDrawer.isDrawerOpen(GravityCompat.START)) {
            mDrawer.closeDrawer(GravityCompat.START);
        } else super.onBackPressed();
    }
}
