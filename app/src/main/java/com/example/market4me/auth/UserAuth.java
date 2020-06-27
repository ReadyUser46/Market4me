package com.example.market4me.auth;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.market4me.R;
import com.example.market4me.SingleFragmentActivity;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.io.Serializable;


public class UserAuth {

    private Context context;
    private FirebaseAuth mAuth;
    private TextView mUserName, mUserMail, mSignOut;
    private String mUserId;
    private FirebaseUser mCurrentUser;
    private Activity activity;


    public UserAuth(Context context, TextView mUserName, TextView mUserMail, TextView mSignOut, Activity activity) {
        this.context = context;
        this.mAuth = FirebaseAuth.getInstance();
        this.mUserName = mUserName;
        this.mUserMail = mUserMail;
        this.mSignOut = mSignOut;
        this.activity = activity;
        mCurrentUser = mAuth.getCurrentUser();
    }

    public Intent signIn() {
        Intent intent = AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setIsSmartLockEnabled(false)
                .build();
        return intent;
    }

    public void signOut() {
        AuthUI.getInstance()
                .signOut(context)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Log.i("patapum", "User is now signed out");
                        Snackbar.make(
                                activity.findViewById(R.id.drawer_layout),
                                R.string.signed_out_successfully,
                                BaseTransientBottomBar.LENGTH_SHORT).show();
                    }
                });
    }

    public void updateUI(boolean signInAvailable) {

        // Status text
        if (signInAvailable) {
            mUserName.setText("Sign In");
            mUserMail.setVisibility(View.INVISIBLE);
            mSignOut.setVisibility(View.INVISIBLE);

        }
        if (!signInAvailable) {
            mUserName.setText(mAuth.getCurrentUser().getDisplayName());
            mUserMail.setText(mAuth.getCurrentUser().getEmail());
            mUserMail.setVisibility(View.VISIBLE);
            mSignOut.setVisibility(View.VISIBLE);
        }
    }

    public String getUserId() {
        return mAuth.getCurrentUser().getUid();
    }

    public FirebaseUser getCurrentUser() {
        return mCurrentUser;
    }

    public FirebaseAuth getmAuth() {
        return mAuth;
    }

    public void signInAnon() {

        mAuth.signInAnonymously()
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        updateUI(true);
                        mUserId = mAuth.getCurrentUser().getUid();
                        Log.i("patapum", "User sign in anonymously with Id: " + mUserId);

                    }
                });

    }
}


