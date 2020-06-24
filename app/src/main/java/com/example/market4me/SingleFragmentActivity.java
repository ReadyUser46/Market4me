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
    private UserAuth mUserAuth;
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

        // Nav Header Account
        TextView userName = headerView.findViewById(R.id.nav_header_user);
        TextView userMail = headerView.findViewById(R.id.nav_header_mail);
        TextView signOut = headerView.findViewById(R.id.nav_header_sign_out);

        // Authentication
        mUserAuth = new UserAuth(this, userName, userMail, signOut, this);
        FirebaseUser mCurrentUser = mUserAuth.getCurrentUser();

        // Check if user is signed in
        if (mCurrentUser == null) {
            Log.i("patapum", "App init with null user");
            mUserAuth.signInAnon(); /*Si el usuario no está logeado, lo logeamos anonimamente*/
        } else if (mCurrentUser.isAnonymous()) {
            mUserAuth.updateUI(true);
            mUserId = mUserAuth.getUserId();
            Log.i("patapum", "App init with anon user, and id: " + mUserId);
        } else if (!mCurrentUser.isAnonymous()) {
            mUserAuth.updateUI(false);
            mUserId = mUserAuth.getUserId();
            Log.i("patapum", "App init with registered user, and id: " + mUserId);
        }

        // SignIn and SignOut listeners
        userName.setOnClickListener(new ClickListenerAuth("signin"));
        signOut.setOnClickListener(new ClickListenerAuth("signout"));

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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == SingleFragmentActivity.RC_SIGN_IN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);

            // Successfully sign in
            if (resultCode == RESULT_OK) {
                mUserId = mUserAuth.getUserId();
                mUserAuth.updateUI(false);
                Snackbar.make(findViewById(R.id.drawer_layout), "Bienvenido " + mUserAuth.getCurrentUser().getDisplayName(), BaseTransientBottomBar.LENGTH_SHORT).show();
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

    class NavigationViewListener implements NavigationView.OnNavigationItemSelectedListener {

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

    class ClickListenerAuth implements View.OnClickListener {
        String listenerType;

        public ClickListenerAuth(String listenerType) {
            this.listenerType = listenerType;
        }

        @Override
        public void onClick(View v) {

            switch (listenerType) {
                case "signin": {
                    startActivityForResult(mUserAuth.signIn(), RC_SIGN_IN);
                    break;
                }
                case "signout": {
                    mUserAuth.signOut();
                    mUserAuth.signInAnon();
                    mUserAuth.updateUI(true);
                    break;
                }

            }
        }
    }


}
