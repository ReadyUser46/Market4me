package com.example.market4me.auth;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.market4me.R;
import com.example.market4me.SingleFragmentActivity;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class UserAuth {

    private Context context;
    private FirebaseAuth mAuth;
    private TextView mUserName, mUserMail, mSignOut;
    private String mUserId;
    private FirebaseUser mUser;


    private SingleFragmentActivity singleFragmentActivity;

    // CONSTANTS
    public static final int RC_SIGN_IN = 237;

    public UserAuth(Context context, FirebaseAuth auth, TextView mUserName, TextView mUserMail, TextView mSignOut, SingleFragmentActivity singleFragmentActivity) {
        this.context = context;
        this.mAuth = auth;
        this.mUserName = mUserName;
        this.mUserMail = mUserMail;
        this.mSignOut = mSignOut;
        this.singleFragmentActivity = singleFragmentActivity;
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
                                singleFragmentActivity.findViewById(R.id.drawer_layout),
                                R.string.signed_out_successfully,
                                BaseTransientBottomBar.LENGTH_SHORT).show();
                    }
                });


    }

    /*public void updateUI(FirebaseUser user) {

        // Status text & listeners

        if (user != null && user.isAnonymous()) {

            mUserName.setText("Sign In");
            mUserMail.setVisibility(View.INVISIBLE);
            mSignOut.setVisibility(View.INVISIBLE);
            mUserName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    singleFragmentActivity.startActivityForResult(signIn(), RC_SIGN_IN);
                }
            });
            mUserId = mAuth.getCurrentUser().getUid();

        } else if (user != null && !user.isAnonymous()) {
            mUserName.setText(mAuth.getCurrentUser().getDisplayName());
            mUserMail.setText(mAuth.getCurrentUser().getEmail());
            mUserMail.setVisibility(View.VISIBLE);
            mSignOut.setVisibility(View.VISIBLE);
            mUserId = mAuth.getCurrentUser().getUid();

            mSignOut.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    signOut();
                }
            });
        } else if (user == null) {
            signInAnon(); //Si el usuario no está logeado, lo logeamos anonimamente
        }


    }*/


    public String getmUserId() {
        return mUserId;
    }

    public TextView getmUserName() {
        return mUserName;
    }

    public void signInAnon() {

        mAuth.signInAnonymously()
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        mUserId = mAuth.getCurrentUser().getUid();
                        Log.i("patapum", "User sign in anonymously with Id: " + mUserId);

                    }
                });

    }
}


