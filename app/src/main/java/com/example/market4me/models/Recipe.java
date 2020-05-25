package com.example.market4me.models;

import android.content.Context;
import android.net.Uri;

import java.io.File;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Recipe implements Serializable {


    private String title;
    private List<String> ingredients;
    private int people;
    private int time;
    private List<Integer> quantities;
    private List<String> units;
    private String preparation;
    private String photoName;


    public Recipe(String title, List<String> ingredients, int people, int time, List<Integer> quantities, List<String> units, String preparation, String photoName) {
        this.title = title;
        this.ingredients = ingredients;
        this.people = people;
        this.time = time;
        this.quantities = quantities;
        this.units = units;
        this.preparation = preparation;
        this.photoName = photoName;
    }

    // Empty constructor for FireStore Cloud
    public Recipe() {
    }

    // Getters and Setters for atributes

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<String> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<String> ingredients) {
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

    public List<Integer> getQuantities() {
        return quantities;
    }

    public void setQuantities(List<Integer> quantities) {
        this.quantities = quantities;
    }

    public List<String> getUnits() {
        return units;
    }

    public void setUnits(List<String> units) {
        this.units = units;
    }

    public String getPreparation() {
        return preparation;
    }

    public void setPreparation(String preparation) {
        this.preparation = preparation;
    }

    public String getPhotoName() {
        return photoName;
    }

    public void setPhotoName(String photoName) {
        this.photoName = photoName;
    }

    /*public File getPhotoFile(Context context) {

        // Create file location
        File storageDir = context.getFilesDir();

        return new File(storageDir, photoName);
    }*/
}
