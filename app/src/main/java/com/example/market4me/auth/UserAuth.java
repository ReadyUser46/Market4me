package com.example.market4me.auth;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;

import com.bumptech.glide.Glide;
import com.example.market4me.R;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;


public class UserAuth {

    private FirebaseAuth mAuth;
    private TextView mSignIn, mSignOut;
    private ImageView imageView;
    private String mUserId;
    private FirebaseUser firebaseUser;
    private Activity activity;

    public UserAuth(TextView mSignIn, TextView mSignOut, ImageView imageView, Activity activity) {
        this.imageView = imageView;
        this.mSignIn = mSignIn;
        this.mSignOut = mSignOut;
        this.activity = activity;
        this.mAuth = FirebaseAuth.getInstance();
        firebaseUser = mAuth.getCurrentUser();

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
                .signOut(activity)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Log.i("patapum_auth", "User is now signed out");
                        Snackbar.make(
                                activity.findViewById(R.id.drawer_layout),
                                R.string.signed_out_successfully,
                                BaseTransientBottomBar.LENGTH_SHORT).show();
                    }
                });
    }

    public void updatePicture(Uri uri) {

        UserProfileChangeRequest update = new UserProfileChangeRequest.Builder()
                .setPhotoUri(uri)
                .build();

        firebaseUser.updateProfile(update)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) Log.i("patapum_auth", "User picture updated");
                    }
                });
    }

    public void updateUI(boolean userLogged) {

        // Status text
        if (!userLogged) {
            mSignIn.setText(R.string.sign);
            mSignOut.setVisibility(View.INVISIBLE);
            Drawable userPic = ResourcesCompat.getDrawable(activity.getResources(), R.drawable.dummy_user, null);

            Glide.
                    with(activity)
                    .load(userPic)
                    .centerCrop()
                    .into(imageView);
        }

        if (userLogged) {

            mSignIn.setText(mAuth.getCurrentUser().getDisplayName());
            mSignOut.setVisibility(View.VISIBLE);
            Uri userPic = firebaseUser.getPhotoUrl();

            Glide.
                    with(activity)
                    .load(userPic)
                    .centerCrop()
                    .into(imageView);


        }


    }

    public String getUserId() {
        return mAuth.getCurrentUser().getUid();
    }

    public FirebaseUser getCurrentUser() {
        return firebaseUser;
    }

    public void signInAnon() {

        mAuth.signInAnonymously()
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        updateUI(true);
                        mUserId = mAuth.getCurrentUser().getUid();
                        Log.i("patapum_auth", "User sign in anonymously with Id: " + mUserId);

                    }
                });

    }
}


