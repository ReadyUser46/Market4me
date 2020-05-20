package com.example.market4me;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.market4me.models.Recipe;
import com.example.market4me.utils.GlideApp;
import com.example.market4me.utils.MyAppGlideModule;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.List;

public class DisplayRecipeFragment extends Fragment {

    private Recipe mRecipe;
    private TextView mTitleDisplayed, mPeopleDisplayed, mTimeDisplayed, mIngredientsDisplayed, mNotesDisplayed;
    private ImageView mRecipeImage;

    private FirebaseStorage mStorage;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mRecipe = (Recipe) getActivity().getIntent().getSerializableExtra(DisplayRecipeActivity.EXTRA_RECIPE_OBJECT);
        mStorage = FirebaseStorage.getInstance();

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
        mRecipeImage = view.findViewById(R.id.imageview_displayed);

        viewBinder();

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

        // Retrieve image from Firebase Storage

        if (mRecipe.getPhotoName().trim().equals("") || mRecipe.getPhotoName() == null) {

            // No hay foto

        } else {
            StorageReference storagedPhotoReference = mStorage.getReference().child("Pictures").child(mRecipe.getPhotoName());


            GlideApp.with(getActivity())
                    .load(storagedPhotoReference)
                    .into(mRecipeImage);

        }
    }


}
