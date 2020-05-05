package com.example.market4me.data;

import java.util.ArrayList;
import java.util.List;

public class Recipe {


    private static List<Recipe> mRecipeList;

    private String title;
    private String [] ingredients;
    private int people;
    private int time;
    private int [] quantities;
    private String [] units;
    private String preparation;

    // Full constructor
    public Recipe(String title, String[] ingredients, int people, int time, int[] quantities, String[] units, String preparation) {
        this.title = title;
        this.ingredients = ingredients;
        this.people = people;
        this.time = time;
        this.quantities = quantities;
        this.units = units;
        this.preparation = preparation;
    }
    // Empty constructor
    public Recipe() {
        mRecipeList = new ArrayList<>();
    }

    public static void addRecipe (Recipe recipe){
        mRecipeList.add(recipe);
    }

    // Getters and Setters for atributes

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String[] getIngredients() {
        return ingredients;
    }

    public void setIngredients(String[] ingredients) {
        this.ingredients = ingredients;
    }

    public int getPeople() {
        return people;
    }

    public void setPeople(int people) {
        this.people = people;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public int[] getQuantities() {
        return quantities;
    }

    public void setQuantities(int[] quantities) {
        this.quantities = quantities;
    }

    public String[] getUnits() {
        return units;
    }

    public void setUnits(String[] units) {
        this.units = units;
    }

    public String getPreparation() {
        return preparation;
    }

    public void setPreparation(String preparation) {
        this.preparation = preparation;
    }
}
