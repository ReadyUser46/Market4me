package com.example.market4me;

import android.os.Bundle;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;

public class MainActivityTest extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_home);


        // SearchView
        SearchView searchView = findViewById(R.id.searchview_home);
        ImageView searchIcon = searchView.findViewById(androidx.appcompat.R.id.search_mag_icon);
        //searchIcon.setColorFilter(getResources().getColor(R.color.white));
        //searchView.setQueryHint("Búsquea rápida");

        // Bring to front Views
        //ImageView iconCard = findViewById(R.id.card_icon_circle);
        //iconCard.bringToFront();


    }
}
