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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;

public class UserAuth {

    private Context context;
    private FirebaseAuth auth;
    private TextView userName, userMail, signOut;

    private SingleFragmentActivity singleFragmentActivity;

    public UserAuth(Context context, FirebaseAuth auth, TextView userName, TextView userMail, TextView signOut, SingleFragmentActivity singleFragmentActivity) {
        this.context = context;
        this.auth = auth;
        this.userName = userName;
        this.userMail = userMail;
        this.signOut = signOut;
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
                        userName.setText("Sign In");
                        userMail.setVisibility(View.INVISIBLE);
                        signOut.setVisibility(View.INVISIBLE);

                        userName.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                singleFragmentActivity.startActivityForResult(signIn(), SingleFragmentActivity.RC_SIGN_IN);
                            }
                        });

                    }
                });
        Snackbar.make(singleFragmentActivity.findViewById(R.id.drawer_layout), R.string.signed_out_successfully, BaseTransientBottomBar.LENGTH_SHORT).show();

    }

    public boolean isUserSignedIn() {

        if (auth.getCurrentUser() != null) {
            // already sign in
            Log.i("patapum", "Current user: Hay un usuario logeado");
            Log.i("patapum", "Current user: " + auth.getCurrentUser().getDisplayName());
            return true;
        } else {
            // not sign in
            return false;
        }
    }
}


