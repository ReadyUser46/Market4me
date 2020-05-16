package com.example.market4me;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.market4me.models.Recipe;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class NewRecipeFragment extends Fragment {

    private Button mSaveButton;
    private TextInputEditText mTitleEditText, mPeopleEditText, mTimeEditText, mPreparationEditText;
    private TextInputEditText mIngredientEditText0, mQuantityEditText0;
    private Spinner mUnitSpinner0;
    private ArrayAdapter<CharSequence> spinnerAdapter;


    private List<String> mIngredientsList;
    private List<Integer> mQuantitiesList;
    private List<String> mUnitsList;
    private List<TextInputEditText> mIngredientEditTexts;
    private List<TextInputEditText> mQuantityEditTexts;
    private List<Spinner> mSpinners;

    private CollectionReference recipesRef;
    private LinearLayout rootLayout;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // init arraylists
        mIngredientsList = new ArrayList<>();
        mQuantitiesList = new ArrayList<>();
        mUnitsList = new ArrayList<>();
        mIngredientEditTexts = new ArrayList<>();
        mQuantityEditTexts = new ArrayList<>();
        mSpinners = new ArrayList<>();



        // init Firebase
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        recipesRef = db.collection("Recipes");

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_new_recipe, container, false);
        rootLayout = view.findViewById(R.id.linearlayout_ingredients);


        viewBinder(view); // Binding of every element on the screen to his view + spinners

        mIngredientEditTexts.add(mIngredientEditText0);
        mQuantityEditTexts.add(mQuantityEditText0);
        mSpinners.add(mUnitSpinner0);

        /* Recorremos el array de EditTexts de ingredientes y en cada uno, podemos un listener para el texto.
         *  El listener es un custom listener que hemos creado nosotros, como clase aparte e implementa la interfaz TextWatcher.
         *  Dentro de esa clase, se crea constructor, que pide dos argumentos, current edit text y el siguiente.
         *  Dentro del for loop, se los pasamos.*/

        // Custom listener para el editText
        mIngredientEditText0.addTextChangedListener(new AddEditTexts(getContext(), 4, true));

        // Custom listener para el button
        mSaveButton.setOnClickListener(new SaveButtonListener());

        return view;
    }


    private void viewBinder(View view) {

        mSaveButton = view.findViewById(R.id.but_save);
        mTitleEditText = view.findViewById(R.id.et_title);
        mPeopleEditText = view.findViewById(R.id.et_people);
        mPreparationEditText = view.findViewById(R.id.et_preparation);
        mTimeEditText = view.findViewById(R.id.et_time);
        mIngredientEditText0 = view.findViewById(R.id.et_ingredient0);
        mQuantityEditText0 = view.findViewById(R.id.et_quantity0);
        mUnitSpinner0 = view.findViewById(R.id.spinner0);


        // SPINNER SETUP
        // Create an ArrayAdapter using the string array and a default spinner layout
        spinnerAdapter = ArrayAdapter.createFromResource(getActivity(), R.array.quantity_units, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mUnitSpinner0.setAdapter(spinnerAdapter);


    }


    class AddEditTexts implements TextWatcher { //Inner class which implements a custom listener for text changes.

        Context context;
        int position;
        boolean isActivated;


        public AddEditTexts(Context context, int position, boolean isActivated) {
            this.context = context;
            this.position = position;
            this.isActivated = isActivated;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (isActivated) {

                // LinearLayout temporal
                LinearLayout tempLinearLayout = ViewCreator.linearLayout(getContext(), 0);
                tempLinearLayout.setLayoutParams(ViewCreator.layoutParams(getContext(), -1, -1));


                // nuevo textInputEditText para ingredientes y added to linearLayout temporal
                TextInputEditText newIngredient = new TextInputEditText(getContext());
                LinearLayout.LayoutParams textInputEditTextParams = ViewCreator.layoutParams(getContext(), -1, -1);

                TextInputLayout newTilIngredient = new TextInputLayout(getContext());
                LinearLayout.LayoutParams ingredientTilParams = ViewCreator.layoutParams(getContext(), -1, -2, 1f);

                newTilIngredient.addView(newIngredient, textInputEditTextParams);
                newTilIngredient.setHint("Ingrediente");
                newTilIngredient.setEndIconMode(TextInputLayout.END_ICON_CLEAR_TEXT);


                // nuevo textInputEditText para cantidades y added to linearLayout temporal
                TextInputEditText newQuantity = new TextInputEditText(getContext());
                newQuantity.setInputType(InputType.TYPE_CLASS_NUMBER);
                TextInputLayout newTilQuantity = new TextInputLayout(getContext());
                LinearLayout.LayoutParams quantityTilParams = ViewCreator.layoutParams(getContext(), -1, -2, 2f);

                newTilQuantity.addView(newQuantity, textInputEditTextParams);
                newTilQuantity.setHint("Cantidad");


                // nuevo Spinner para ingredientes y add to linearLayout temporal
                Spinner newSpinner = new Spinner(getContext());
                LinearLayout.LayoutParams spinnerParams = ViewCreator.layoutParams(getContext(), -1, -2, 2.2f);
                newSpinner.setAdapter(spinnerAdapter);

                // Add EditText and spinner to the LinearLayout Horizontal and this LL to the rootView
                tempLinearLayout.addView(newTilIngredient, ingredientTilParams);
                tempLinearLayout.addView(newTilQuantity, quantityTilParams);
                tempLinearLayout.addView(newSpinner, spinnerParams);

                mIngredientEditTexts.add(newIngredient);
                mQuantityEditTexts.add(newQuantity);
                mSpinners.add(newSpinner);

                rootLayout.addView(tempLinearLayout, position);

                // Aumentamos posición y añadimos listener al editText generado.
                position++;
                newIngredient.addTextChangedListener(new AddEditTexts(getContext(), position, true));


            }

            isActivated = false;


        }

        @Override
        public void afterTextChanged(Editable s) {

        }

    }


    class SaveButtonListener implements View.OnClickListener { //Inner class which implements a custom listener

        @Override
        public void onClick(View v) {


            /*Con este bucle for, recorremos todos los editTexts de ingredientes, cantidades y spinner,
             * cogemos los valores que el usuario ha introducido y vamos poblando la lista de ingredientes, cantidades y spinner,
             * para posteriormente, pasarlos al objeto receta*/
            for (int i = 0; i < mIngredientEditTexts.size(); i++) {
                TextInputEditText et_ingredient = mIngredientEditTexts.get(i);
                TextInputEditText et_quantity = mQuantityEditTexts.get(i);
                Spinner spinner = mSpinners.get(i);

                if (et_ingredient != null && et_ingredient.getText().length() != 0) {
                    mUnitsList.add(spinner.getSelectedItem().toString());
                    mIngredientsList.add(et_ingredient.getText().toString());
                    if (et_quantity.getText().length() == 0) { // fix this with editText material design
                        mQuantitiesList.add(0);
                    } else {
                        mQuantitiesList.add(Integer.parseInt(et_quantity.getText().toString()));
                    }
                }
            }

            Recipe recipe = new Recipe();
            recipe.setTitle(mTitleEditText.getText().toString());
            if (mPeopleEditText.getText().length() != 0 && mTimeEditText.getText().length() != 0) { // fix this with edittext material design
                recipe.setPeople(Integer.parseInt(mPeopleEditText.getText().toString()));
                recipe.setTime(Integer.parseInt(mTimeEditText.getText().toString()));
            }
            recipe.setPreparation(mPreparationEditText.getText().toString());
            recipe.setIngredients(mIngredientsList);
            recipe.setQuantities(mQuantitiesList);
            recipe.setUnits(mUnitsList);

            Recipe.addRecipe(recipe); // añadir la receta a una lista de recetas

            recipesRef.add(recipe); // upload la receta a fireStore
            Intent intent = RecipeListActivity.newIntent(getContext());

            startActivity(intent);


        }
    }
}


