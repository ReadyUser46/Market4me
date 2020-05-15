package com.example.market4me;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.market4me.models.Recipe;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class NewRecipeFragment extends Fragment {

    private Button mSaveButton;
    private EditText mTitleEditText, mPeopleEditText, mTimeEditText, mPreparationEditText;

    private List<EditText> mEditTextIngredientsList;
    private List<EditText> mEditTextQuantitiesList;
    private List<Spinner> mSpinnersList;
    private List<String> mIngredientsList;
    private List<Integer> mQuantitiesList;
    private List<String> mUnitsList;

    private CollectionReference recipesRef;

    private EditText mIngredientEditText0, mQuantityEditText0;
    private Spinner mUnitSpinner0;


    private LinearLayout rootLayout;
    private List<EditText> createdIngredients, createdQuantities;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // init arraylists
        mEditTextIngredientsList = new ArrayList<>();
        mEditTextQuantitiesList = new ArrayList<>();
        mSpinnersList = new ArrayList<>();
        mIngredientsList = new ArrayList<>();
        mQuantitiesList = new ArrayList<>();
        mUnitsList = new ArrayList<>();
        createdIngredients = new ArrayList<>();

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

        /* Recorremos el array de EditTexts de ingredientes y en cada uno, podemos un listener para el texto.
         *  El listener es un custom listener que hemos creado nosotros, como clase aparte e implementa la interfaz TextWatcher.
         *  Dentro de esa clase, se crea constructor, que pide dos argumentos, current edit text y el siguiente.
         *  Dentro del for loop, se los pasamos.*/

        for (int i = 0; i < mEditTextIngredientsList.size(); i++) {
            mEditTextIngredientsList.get(i).addTextChangedListener(new AddEditTexts(getContext()));
        }


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
        EditText mIngredientEditText1 = view.findViewById(R.id.et_ingredient1);
        EditText mIngredientEditText2 = view.findViewById(R.id.et_ingredient2);
        mQuantityEditText0 = view.findViewById(R.id.et_quantity0);
        EditText mQuantityEditText1 = view.findViewById(R.id.et_quantity1);
        EditText mQuantityEditText2 = view.findViewById(R.id.et_quantity2);
        mUnitSpinner0 = view.findViewById(R.id.spinner0);
        Spinner mUnitSpinner1 = view.findViewById(R.id.spinner1);
        Spinner mUnitSpinner2 = view.findViewById(R.id.spinner2);

        mEditTextIngredientsList.add(mIngredientEditText0);
        mEditTextIngredientsList.add(mIngredientEditText1);
        mEditTextIngredientsList.add(mIngredientEditText2);

        mEditTextQuantitiesList.add(mQuantityEditText0);
        mEditTextQuantitiesList.add(mQuantityEditText1);
        mEditTextQuantitiesList.add(mQuantityEditText2);

        mSpinnersList.add(mUnitSpinner0);
        mSpinnersList.add(mUnitSpinner1);
        mSpinnersList.add(mUnitSpinner2);

        // SPINNER SETUP
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(), R.array.quantity_units, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to each spinner

        for (Spinner sp : mSpinnersList) {
            sp.setAdapter(adapter);
        }

    }


    class SaveButtonListener implements View.OnClickListener { //Inner class which implements a custom listener

        @Override
        public void onClick(View v) {


            /*Con este bucle for, recorremos todos los editTexts de ingredientes, cantidades y spinner,
             * cogemos los valores que el usuario ha introducido y vamos poblando la lista de ingredientes, cantidades y spinner,
             * para posteriormente, pasarlos al objeto receta*/
            for (int i = 0; i < mEditTextIngredientsList.size(); i++) {
                EditText et_ingredient = mEditTextIngredientsList.get(i);
                EditText et_quantity = mEditTextQuantitiesList.get(i);
                Spinner spinner = mSpinnersList.get(i);

                if (et_ingredient != null && et_ingredient.isShown() && et_ingredient.getText().length() != 0) {
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

    class AddEditTexts implements TextWatcher { //Inner class which implements a custom listener for text changes.

        Context context;

        public AddEditTexts(Context context) {
            this.context = context;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

            // LinearLayout temporal
            LinearLayout tempLinearLayout = new LinearLayout(context);
            tempLinearLayout.setOrientation(LinearLayout.HORIZONTAL);
            LinearLayout.LayoutParams tempLinearLayoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            tempLinearLayout.setLayoutParams(tempLinearLayoutParams);

            // nuevo editText para ingredientes y añadido el linearLayout temporal
            EditText newIngredient = new EditText(context);
            LinearLayout.LayoutParams ingredientParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1);
            newIngredient.setHint("Ingrediente");
            newIngredient.setHintTextColor(getResources().getColor(R.color.colorHint));
            tempLinearLayout.addView(newIngredient, ingredientParams);

            newIngredient.addTextChangedListener(this);
            //createdIngredients.add(newIngredient);

            // nuevo editText para cantidades y añadido el linearLayout temporal
            EditText newQuantity = new EditText(context);
            LinearLayout.LayoutParams quantityParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1);
            newQuantity.setHint("Cantidad");
            newQuantity.setHintTextColor(getResources().getColor(R.color.colorHint));
            tempLinearLayout.addView(newQuantity, quantityParams);


            // Se añade tot lo anterior al linear layout parent.
            rootLayout.addView(tempLinearLayout, 4);



        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    }


}


