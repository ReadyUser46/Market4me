package com.torkdev.market4me;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.torkdev.market4me.models.Recipe;
import com.torkdev.market4me.utils.GlideApp;

import java.util.List;

public class DisplayRecipeFragment extends Fragment {

    // MEMBER VARIABLES
    private Recipe mRecipe;
    private String mRecipeId;
    private TextView mTitleDisplayed, mPeopleDisplayed, mTimeDisplayed, mIngredientsDisplayed, mNotesDisplayed;
    private FirebaseStorage mStorage;
    private String mUserId;

    // CONSTANTS
    private static final String ARG_RECIPE = "recipe_object";
    private static final String ARG_RECIPE_ID = "recipe_id";
    private static final String ARG_USER_ID = "user_id";
    private boolean start;
    MenuItem menuItem;

    public static DisplayRecipeFragment newInstance(Recipe recipe, String recipeId, String userId) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(ARG_RECIPE, recipe);
        bundle.putString(ARG_RECIPE_ID, recipeId);
        bundle.putString(ARG_USER_ID, userId);

        DisplayRecipeFragment fragment = new DisplayRecipeFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        mStorage = FirebaseStorage.getInstance();

        // get arguments
        mRecipe = (Recipe) getArguments().getSerializable(ARG_RECIPE);
        mRecipeId = getArguments().getString(ARG_RECIPE_ID);
        mUserId = getArguments().getString(ARG_USER_ID);


    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_display_recipe2, container, false);

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


        // Collapsing Toolbar. Hide title
        final CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout) view.findViewById(R.id.collapsingToolbarDisplay);
        AppBarLayout appBarLayout = view.findViewById(R.id.app_bar);

        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = true;
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if (scrollRange + verticalOffset == 0) {
                    collapsingToolbarLayout.setTitle(mRecipe.getTitle());
                    menuItem.setVisible(true);
                    isShow = true;
                } else if (isShow) {
                    collapsingToolbarLayout.setTitle(" ");//careful there should a space between double quote otherwise it wont work
                    isShow = false;
                    if (start) menuItem.setVisible(false);


                }
            }
        });

        // Retrieve image from Firebase Storage
        if (mRecipe.getPhotoName() == null || mRecipe.getPhotoName().trim().equals("")) {

            Snackbar.make(view, "No hay foto", BaseTransientBottomBar.LENGTH_LONG).show();

        } else {
            Log.i("patapum", "UserId from DisplayRecipeFragment: " + mUserId);
            StorageReference storagedPhotoReference = mStorage.getReference().child("Pictures").child(mUserId).child(mRecipe.getPhotoName());

            GlideApp.with(getActivity())
                    .load(storagedPhotoReference)
                    .centerCrop()
                    .into(mRecipeImage);
        }

        // FAB listener
        mFabEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.display_recipe_menu, menu);

        menuItem = menu.findItem(R.id.edit_menu_icon);
        menuItem.setVisible(false);
        start = true;
        super.onCreateOptionsMenu(menu, inflater);

    }

    @Override
    public void onPrepareOptionsMenu(@NonNull Menu menu) {
        super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == R.id.edit_menu_icon) {
            Intent intent = NewRecipeActivity.newIntent(getActivity(), mRecipe, mRecipeId);
            startActivity(intent);
            return true;
        } else
            return super.onOptionsItemSelected(item);

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
            ultraString.append("\n\n");

        }

        // Set texts to textviews
        mTitleDisplayed.setText(mRecipe.getTitle());
        mPeopleDisplayed.setText(String.format("%s %s", mRecipe.getPeople(), getString(R.string.hint_people)));
        String timeUnits = (mRecipe.getTime() == 1) ? "hora" : "horas";
        mTimeDisplayed.setText(String.format("%s %s", mRecipe.getTime(), timeUnits));
        mIngredientsDisplayed.setText(ultraString.toString());
        mNotesDisplayed.setText(mRecipe.getPreparation());


    }

}
