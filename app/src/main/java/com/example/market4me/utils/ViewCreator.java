package com.example.market4me.utils;

import android.content.Context;
import android.text.InputType;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;

import androidx.core.content.res.ResourcesCompat;

import com.example.market4me.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class ViewCreator {

    private TextInputLayout newTilIngredient;
    private TextInputLayout newTilQuantity;
    private TextInputEditText newIngredientET;
    private TextInputEditText newQuantityET;
    private Spinner newSpinner;
    private LinearLayout tempLinearLayout;

    private RelativeLayout relativeLayout;

    /*  width & height:
     ** -1 = LinearLayout.LayoutParams.MATCH_PARENT
     ** -2 = LinearLayout.LayoutParams.WRAP_CONTENT*/

    public LinearLayout createLinearLayout(Context context, int orientation) {

        /* Orientation:
         ** 0 = LinearLayout.HORIZONTAL
         ** 1 = LinearLayout.VERTICAL */

        LinearLayout linearLayout = new LinearLayout(context);
        linearLayout.setOrientation(orientation);
        linearLayout.setWeightSum(3f);

        return linearLayout;
    }

    public LinearLayout.LayoutParams createLayoutParams(int width, int height) {
        LinearLayout.LayoutParams l = new LinearLayout.LayoutParams(width, height);

        return l;
    }

    public RelativeLayout createRelLayout(Context context) {
        RelativeLayout relativeLayout = new RelativeLayout(context);
        return relativeLayout;
    }

    public LinearLayout.LayoutParams createLayoutParams(int width, int height, float weight) {

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(width, height);
        layoutParams.weight = weight;
        return layoutParams;
    }

    public LinearLayout newBlockEditTexts(Context context) {
        // LinearLayout temporal
        tempLinearLayout = createLinearLayout(context, 0);
        tempLinearLayout.setLayoutParams(createLayoutParams(-1, -2));

        LinearLayout.LayoutParams genericEditTextParams = createLayoutParams(-1, -2);

        // nuevo textInputEditText para ingredientes y added to linearLayout temporal
        newIngredientET = new TextInputEditText(context);
        newIngredientET.setTextSize(16);
        newIngredientET.setTextColor(context.getResources().getColor(R.color.ic_background));
        newIngredientET.setTypeface(ResourcesCompat.getFont(context, R.font.segoe_ui_regular));

        newTilIngredient = new TextInputLayout(context);
        newTilIngredient.setStartIconDrawable(context.getDrawable(R.drawable.ic_add));
        LinearLayout.LayoutParams ingredientTilParams = createLayoutParams(-1, -2, 1f);

        newTilIngredient.addView(newIngredientET, genericEditTextParams);
        newTilIngredient.setHint(context.getString(R.string.hint_ingredient));
        newTilIngredient.setEndIconMode(TextInputLayout.END_ICON_CLEAR_TEXT);


        // nuevo textInputEditText para cantidades y added to linearLayout temporal
        newQuantityET = new TextInputEditText(context);
        newQuantityET.setInputType(InputType.TYPE_CLASS_NUMBER);
        newQuantityET.setTextSize(16);
        newQuantityET.setTextColor(context.getResources().getColor(R.color.ic_background));
        newQuantityET.setTypeface(ResourcesCompat.getFont(context, R.font.segoe_ui_regular));

        newTilQuantity = new TextInputLayout(context);
        LinearLayout.LayoutParams quantityTilParams = createLayoutParams(-1, -2, 1f);

        newTilQuantity.addView(newQuantityET, genericEditTextParams);
        newTilQuantity.setHint(context.getString(R.string.hint_quantity));

        // nuevo Spinner para ingredientes y add to linearLayout temporal
        newSpinner = new Spinner(context);

        LinearLayout.LayoutParams spinnerParams = createLayoutParams(-1, -2, 1f);
        /*
           SPINNER SETUP
           Create an ArrayAdapter using the string array and a default spinner layout
           Specify the layout to use when the list of choices appears
         */
        ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter.createFromResource(context, R.array.quantity_units, android.R.layout.simple_spinner_item);
        //spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //spinnerAdapter.setDropDownViewResource(R.layout.spinner_custom);

        newSpinner.setAdapter(spinnerAdapter);

        // Add EditText and spinner to the LinearLayout Horizontal and this LL to the rootView
        tempLinearLayout.addView(newTilIngredient, ingredientTilParams);
        tempLinearLayout.addView(newTilQuantity, quantityTilParams);
        tempLinearLayout.addView(newSpinner, spinnerParams);

        return tempLinearLayout;
    }

/*
    public RelativeLayout newBlockEditTexts(Context context) {
        // LinearLayout temporal
        //tempLinearLayout = createLinearLayout(context, 0);
        //tempLinearLayout.setLayoutParams(createLayoutParams(-1, -2));

        relativeLayout = createRelLayout(context);

        // nuevo textInputEditText para ingredientes
        newIngredientET = new TextInputEditText(context);
        newIngredientET.setTypeface(ResourcesCompat.getFont(context, R.font.segoe_ui_regular));
        newIngredientET.setTextColor(context.getResources().getColor(R.color.ic_background));
        newIngredientET.setTextSize(16);

        // nuevo TIL_textInputEditText para ingredientes
        newTilIngredient = new TextInputLayout(context);
        newTilIngredient.setHint(context.getString(R.string.hint_ingredient));
        newTilIngredient.setEndIconMode(TextInputLayout.END_ICON_CLEAR_TEXT);
        newTilIngredient.addView(newIngredientET);
        newTilIngredient.setId(View.generateViewId());

        relativeLayout.addView(newTilIngredient);

        // nuevo textInputEditText para ingredientes
        newQuantityET = new TextInputEditText(context);
        newQuantityET.setTypeface(ResourcesCompat.getFont(context, R.font.segoe_ui_regular));
        newQuantityET.setTextColor(context.getResources().getColor(R.color.ic_background));
        newQuantityET.setTextSize(16);
        newQuantityET.setInputType(InputType.TYPE_CLASS_NUMBER);

        // nuevo TIL_textInputEditText para ingredientes
        newTilQuantity = new TextInputLayout(context);
        LinearLayout.LayoutParams quantityTilParams = createLayoutParams(0, -2, 1f);
        newTilQuantity.setHint(context.getString(R.string.hint_quantity));
        newTilQuantity.addView(newQuantityET);
        newTilQuantity.setId(View.generateViewId());

        RelativeLayout.LayoutParams lp1 = new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        lp1.addRule(RelativeLayout.RIGHT_OF, newTilIngredient.getId());
        relativeLayout.addView(newTilQuantity, lp1);

        // nuevo Spinner para ingredientes y add to linearLayout temporal
*/
/*        newSpinner = new Spinner(context);
        ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter.createFromResource(context, R.array.quantity_units, android.R.layout.simple_spinner_item);
        newSpinner.setAdapter(spinnerAdapter);
        newSpinner.setId(View.generateViewId());
        RelativeLayout.LayoutParams lp2 = new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        lp2.addRule(RelativeLayout.RIGHT_OF, newTilQuantity.getId());
        relativeLayout.addView(newSpinner, lp2);*//*



        // Add EditText and spinner to the LinearLayout Horizontal and this LL to the rootView
        //tempLinearLayout.addView(newTilIngredient, ingredientTilParams);
        //tempLinearLayout.addView(newTilQuantity, quantityTilParams);
        //tempLinearLayout.addView(newSpinner, spinnerParams);

        return relativeLayout;
    }
*/


    public TextInputLayout getNewTilIngredient() {
        return newTilIngredient;
    }

    public TextInputLayout getNewTilQuantity() {
        return newTilQuantity;
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
