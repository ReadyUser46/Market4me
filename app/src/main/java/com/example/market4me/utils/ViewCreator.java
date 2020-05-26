package com.example.market4me.utils;

import android.content.Context;
import android.text.InputType;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;

import com.example.market4me.NewRecipeFragment;
import com.example.market4me.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class ViewCreator {

    private TextInputEditText newIngredientET;
    private TextInputEditText newQuantityET;
    private Spinner newSpinner;
    private LinearLayout tempLinearLayout;


    public static LinearLayout linearLayout(Context context, int orientation) {


        /* Orientation:
         ** 0 = LinearLayout.HORIZONTAL
         ** 1 = LinearLayout.VERTICAL */

        LinearLayout linearLayout = new LinearLayout(context);
        linearLayout.setOrientation(orientation);

        return linearLayout;
    }

    public static LinearLayout.LayoutParams layoutParams(Context context, int width, int height) {

        /*  width & height:
         ** -1 = LinearLayout.LayoutParams.MATCH_PARENT
         ** -2 = LinearLayout.LayoutParams.WRAP_CONTENT*/
        return new LinearLayout.LayoutParams(width, height);
    }

    public static LinearLayout.LayoutParams layoutParams(Context context, int width, int height, float weight) {

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(width, height);
        layoutParams.weight = weight;
        return layoutParams;
    }

    public void newBlockEditTexts(Context context, SpinnerAdapter spinnerAdapter) {
        // LinearLayout temporal
        tempLinearLayout = ViewCreator.linearLayout(context, 0);
        tempLinearLayout.setLayoutParams(ViewCreator.layoutParams(context, -1, -1));


        // nuevo textInputEditText para ingredientes y added to linearLayout temporal
        newIngredientET = new TextInputEditText(context);
        LinearLayout.LayoutParams textInputEditTextParams = ViewCreator.layoutParams(context, -1, -1);

        TextInputLayout newTilIngredient = new TextInputLayout(context);
        LinearLayout.LayoutParams ingredientTilParams = ViewCreator.layoutParams(context, -1, -2, 1f);

        newTilIngredient.addView(newIngredientET, textInputEditTextParams);
        newTilIngredient.setHint(context.getString(R.string.hint_ingredient));
        newTilIngredient.setEndIconMode(TextInputLayout.END_ICON_CLEAR_TEXT);


        // nuevo textInputEditText para cantidades y added to linearLayout temporal
        newQuantityET = new TextInputEditText(context);
        newQuantityET.setInputType(InputType.TYPE_CLASS_NUMBER);
        TextInputLayout newTilQuantity = new TextInputLayout(context);
        LinearLayout.LayoutParams quantityTilParams = ViewCreator.layoutParams(context, -1, -2, 2f);

        newTilQuantity.addView(newQuantityET, textInputEditTextParams);
        newTilQuantity.setHint(context.getString(R.string.hint_quantity));

        // nuevo Spinner para ingredientes y add to linearLayout temporal
        newSpinner = new Spinner(context);
        LinearLayout.LayoutParams spinnerParams = ViewCreator.layoutParams(context, -1, -2, 2.2f);
        newSpinner.setAdapter(spinnerAdapter);

        // Add EditText and spinner to the LinearLayout Horizontal and this LL to the rootView
        tempLinearLayout.addView(newTilIngredient, ingredientTilParams);
        tempLinearLayout.addView(newTilQuantity, quantityTilParams);
        tempLinearLayout.addView(newSpinner, spinnerParams);

        /*mIngredientEditTexts.add(newIngredientET);
        mQuantityEditTexts.add(newQuantityET);
        mSpinners.add(newSpinner);

        rootLayout.addView(tempLinearLayout, position);

        // Aumentamos posición y añadimos listener al editText generado.
        position++;
        newIngredientET.addTextChangedListener(new NewRecipeFragment.AddEditTextsListener(context, position, true, view));*/


    }

    public TextInputEditText getNewIngredientET() {
        return newIngredientET;
    }

    public TextInputEditText getNewQuantityET() {
        return newQuantityET;
    }

    public Spinner getNewSpinner() {
        return newSpinner;
    }

    public LinearLayout getTempLinearLayout() {
        return tempLinearLayout;
    }
}
