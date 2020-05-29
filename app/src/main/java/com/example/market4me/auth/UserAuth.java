package com.example.market4me.auth;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.annotation.NonNull;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class UserAuth {

    private Context context;

    public UserAuth(Context context) {
        this.context = context;
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
                    }
                });
    }

    public boolean isUserSignedIn() {

        FirebaseAuth auth = FirebaseAuth.getInstance();
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


