package com.example.market4me;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.market4me.models.Recipe;

public class DisplayRecipeFragment extends Fragment {

    private Recipe mRecipe;
    private TextView mTitleDisplayed, mPeopleDisplayed, mTimeDisplayed, mIngredientsDisplayed, mNotesDisplayed;

    private static final String TAG = "Snapshots";


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mRecipe = (Recipe) getActivity().getIntent().getSerializableExtra(DisplayRecipeActivity.EXTRA_RECIPE_OBJECT);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_recipe, container, false);

        mTitleDisplayed = view.findViewById(R.id.tv_title_displayed);
        mPeopleDisplayed = view.findViewById(R.id.tv_people_displayed);
        mTimeDisplayed = view.findViewById(R.id.tv_time_displayed);
        mIngredientsDisplayed = view.findViewById(R.id.tv_ingredients_displayed);
        mNotesDisplayed = view.findViewById(R.id.tv_notes_displayed);


        mTitleDisplayed.setText(mRecipe.getTitle());

        /*
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("Recipes").document("id").get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();

                    if (document.exists()) {
                        mRecipe = document.toObject(Recipe.class);
                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });
        */

        return view;
    }
}
