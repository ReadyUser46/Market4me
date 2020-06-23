package com.example.market4me;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.market4me.auth.UserAuth;
import com.firebase.ui.auth.ErrorCodes;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public abstract class SingleFragmentActivity extends AppCompatActivity {


    // MEMBER VARIABLES
    private DrawerLayout mDrawer;
    private TextView mUserName, mUserMail, mSignOut;
    private UserAuth mUserAuth;
    private FirebaseAuth mAuth;
    private FirebaseUser mCurrentUser;

    protected String mUserId;

    // CONSTANTS
    public static final int RC_SIGN_IN = 237;

    protected abstract Fragment createFragment();


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_frame_layout2);

        // Navigation Drawer
        mDrawer = findViewById(R.id.drawer_layout);

        // Navigation View
        NavigationView navigationView = findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(new NavigationViewListener());
        navigationView.setCheckedItem(R.id.nav_list);
        View headerView = navigationView.getHeaderView(0);

        // Authentification
        mUserName = headerView.findViewById(R.id.nav_header_user);
        mUserMail = headerView.findViewById(R.id.nav_header_mail);
        mSignOut = headerView.findViewById(R.id.nav_header_sign_out);

        mAuth = FirebaseAuth.getInstance();
        mCurrentUser = mAuth.getCurrentUser();
        mUserAuth = new UserAuth(this, mAuth, mUserName, mUserMail, mSignOut, this);

        if (mCurrentUser == null) {
            Log.i("patapum", "App init with null user");
            mUserAuth.signInAnon(); /*Si el usuario no está logeado, lo logeamos anonimamente*/
            updateUI();
            mUserId = mUserAuth.getmUserId();

        } else {
            String userType = mCurrentUser.isAnonymous() ? "anon" : "registered";
            updateUI();
            mUserId = mAuth.getCurrentUser().getUid();
            Log.i("patapum", "App init with an user: " + userType + " and id: " + mUserId);
        }

        mUserName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(mUserAuth.signIn(), RC_SIGN_IN);
            }
        });
        mSignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mUserAuth.signOut();
                mUserAuth.signInAnon();
                updateUI();
            }
        });
        //mUserId = "ypqtGXmTvwgqIEfwDNcMt0eX0nD3";

        // Firebase Auth Listener
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseAuth.AuthStateListener mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                //updateUI();
            }
        };
        firebaseAuth.addAuthStateListener(mAuthStateListener);




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

        if (fragment == null) {
            fragment = createFragment();
            fm.beginTransaction().add(R.id.fragment_container2, fragment).commit();

        }

    }

    public void updateUI() {

        FirebaseUser user = mAuth.getCurrentUser();
        // Status text

        if (user != null && user.isAnonymous()) {
            mUserName.setText("Sign In");
            mUserMail.setVisibility(View.INVISIBLE);
            mSignOut.setVisibility(View.INVISIBLE);

        } else if (user != null && !user.isAnonymous()) {
            mUserName.setText(mAuth.getCurrentUser().getDisplayName());
            mUserMail.setText(mAuth.getCurrentUser().getEmail());
            mUserMail.setVisibility(View.VISIBLE);
            mSignOut.setVisibility(View.VISIBLE);
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == SingleFragmentActivity.RC_SIGN_IN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);

            // Successfully sign in
            if (resultCode == RESULT_OK) {
                mUserId = mAuth.getCurrentUser().getUid();
                updateUI();
                Snackbar.make(findViewById(R.id.drawer_layout), "Bienvenido " + mUserAuth.getmUserName().getText().toString(), BaseTransientBottomBar.LENGTH_SHORT).show();
                Log.i("patapum", "User signed in successfully with Id: " + mUserId);
            }
            // Sign in failed
            else {
                // Sign in failed
                if (response == null) {
                    // User pressed back button
                    Snackbar.make(findViewById(R.id.drawer_layout), R.string.sign_in_cancelled, BaseTransientBottomBar.LENGTH_SHORT).show();
                    return;
                }
                // No internet
                if (response.getError().getErrorCode() == ErrorCodes.NO_NETWORK) {
                    Snackbar.make(findViewById(R.id.drawer_layout), R.string.no_internet_connection, BaseTransientBottomBar.LENGTH_SHORT).show();
                    return;
                }

                // Unknown error
                Snackbar.make(findViewById(R.id.drawer_layout), R.string.unknown_error, BaseTransientBottomBar.LENGTH_SHORT).show();
                Log.e("patapum", "Sign-in error: ", response.getError());
            }
        }
    }

    @Override
    public void onBackPressed() {

        /*Usamos el FragmentManager para que recoga el click en back desde un fragment*/
        int count = getSupportFragmentManager().getBackStackEntryCount();

        if (count == 0) {
            /* Esto es para cuando pulsemos back, primero cierre el navigation drawer*/
            if (mDrawer.isDrawerOpen(GravityCompat.START)) {
                mDrawer.closeDrawer(GravityCompat.START);
            } else super.onBackPressed();
        } else {
            getSupportFragmentManager().popBackStack();
        }
    }

    public class NavigationViewListener implements NavigationView.OnNavigationItemSelectedListener {

        FragmentManager fragmentManager;

        NavigationViewListener() {
            fragmentManager = getSupportFragmentManager();

        }

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

            switch (item.getItemId()) {
                case (R.id.nav_new_recipe):
                    fragmentManager.beginTransaction().add(R.id.fragment_container2, new NewRecipeFragment()).commit();
                    break;

                case (R.id.nav_list):
                    fragmentManager.beginTransaction().add(R.id.fragment_container2, new RecipeListFragment()).commit();

                    break;
                default:
                    throw new IllegalStateException("Unexpected value: " + item.getItemId());
            }
            mDrawer.closeDrawer(GravityCompat.START);
            return true;
        }
    }


}
