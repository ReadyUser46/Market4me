package com.example.market4me;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class DisplayRecipeFragment extends Fragment {


    private TextView mTitleDisplayed, mPeopleDisplayed, mTimeDisplayed, mIngredientsDisplayed, mNotesDisplayed;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_recipe, container, false);

        mTitleDisplayed = view.findViewById(R.id.tv_title_displayed);
        mPeopleDisplayed = view.findViewById(R.id.tv_people_displayed);
        mTimeDisplayed = view.findViewById(R.id.tv_time_displayed);
        mIngredientsDisplayed = view.findViewById(R.id.tv_ingredients_displayed);
        mNotesDisplayed = view.findViewById(R.id.tv_notes_displayed);

        return view;
    }
}
