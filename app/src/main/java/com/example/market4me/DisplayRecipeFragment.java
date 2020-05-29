package com.example.market4me;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import com.example.market4me.models.Recipe;
import com.example.market4me.utils.GlideApp;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.List;

public class DisplayRecipeFragment extends Fragment {

    // MEMBER VARIABLES
    private Recipe mRecipe;
    private String mRecipeId;
    private TextView mTitleDisplayed, mPeopleDisplayed, mTimeDisplayed, mIngredientsDisplayed, mNotesDisplayed;
    private FirebaseStorage mStorage;

    // CONSTANTS
    private static final String ARG_RECIPE = "recipe_object";
    private static final String ARG_RECIPE_ID = "recipe_id";


    public static DisplayRecipeFragment newInstance(Recipe recipe, String recipeId) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(ARG_RECIPE, recipe);
        bundle.putString(ARG_RECIPE_ID, recipeId);

        DisplayRecipeFragment fragment = new DisplayRecipeFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //mRecipe = (Recipe) getActivity().getIntent().getSerializableExtra(DisplayRecipeActivity.EXTRA_RECIPE_OBJECT);
        mStorage = FirebaseStorage.getInstance();
        mRecipe = (Recipe) getArguments().getSerializable(ARG_RECIPE);
        mRecipeId = getArguments().getString(ARG_RECIPE_ID);


    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_display_recipe, container, false);

        mTitleDisplayed = view.findViewById(R.id.tv_title_displayed);
        mPeopleDisplayed = view.findViewById(R.id.tv_people_displayed);
        mTimeDisplayed = view.findViewById(R.id.tv_time_displayed);
        mIngredientsDisplayed = view.findViewById(R.id.tv_ingredients_displayed);
        mNotesDisplayed = view.findViewById(R.id.tv_notes_displayed);
        ImageView mRecipeImage = view.findViewById(R.id.imageview_displayed);
        FloatingActionButton mFabEdit = view.findViewById(R.id.fab_edit);

        viewBinder();

        // Toolbar implementation
        Toolbar toolbarDisplay = view.findViewById(R.id.toolbarDisplayRecipe);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbarDisplay);

        CollapsingToolbarLayout collapsingToolbar = view.findViewById(R.id.collapsingToolbarDisplay);
        collapsingToolbar.setTitle("MiReceta");

        // Retrieve image from Firebase Storage
        if (mRecipe.getPhotoName() == null || mRecipe.getPhotoName().trim().equals("")) {

            Snackbar.make(view, "No hay foto", BaseTransientBottomBar.LENGTH_LONG).show();

        } else {
            StorageReference storagedPhotoReference = mStorage.getReference().child("Pictures").child(mRecipe.getPhotoName());

            GlideApp.with(getActivity())
                    .load(storagedPhotoReference)
                    .into(mRecipeImage);
        }

        // FAB listener
        mFabEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent intent = new Intent(getActivity(), NewRecipeActivity.class);
                Intent intent = NewRecipeActivity.newIntent(getActivity(), mRecipe, mRecipeId);
                startActivity(intent);
            }
        });

        // Navigation Drawer
        DrawerLayout mDrawer = getActivity().findViewById(R.id.drawer_layout);

        // Navigation Drawer Icon (Burger)
        ActionBarDrawerToggle toggleBurger = new ActionBarDrawerToggle(
                getActivity(),
                mDrawer,
                toolbarDisplay,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);
        mDrawer.addDrawerListener(toggleBurger);
        toggleBurger.syncState();

        return view;
    }

    private void viewBinder() {
        // Cogemos ingredientes, cantidades y unidades del objeto receta que el usuario y los
        // los ponemos en sus respectivos arraylists.

        List<String> mIngredients = mRecipe.getIngredients();
        List<Integer> mQuantities = mRecipe.getQuantities();
        List<String> mUnits = mRecipe.getUnits();

        // Creamos un único array que se mostrará en el textview correspondiente a modo de lista.
        StringBuilder ultraString = new StringBuilder();
        for (int i = 0; i < mIngredients.size(); i++) {

            ultraString.append("\u2022 ");
            ultraString.append(mQuantities.get(i)).append(" ").append(mUnits.get(i));
            ultraString.append(" de ").append(mIngredients.get(i));
            ultraString.append("\n");

        }

        // Set texts to textviews
        mTitleDisplayed.setText(mRecipe.getTitle());
        mPeopleDisplayed.setText("Personas: " + mRecipe.getPeople());
        mTimeDisplayed.setText("Tiempo: " + mRecipe.getTime());
        mIngredientsDisplayed.setText(ultraString.toString());
        mNotesDisplayed.setText(mRecipe.getPreparation());


    }

}
