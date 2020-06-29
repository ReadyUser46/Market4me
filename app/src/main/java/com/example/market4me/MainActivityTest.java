package com.example.market4me;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.market4me.adapters.RecipeAdapter;
import com.example.market4me.models.Recipe;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class MainActivityTest extends AppCompatActivity {

    private RecipeAdapter mRecipeAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_recipes_list);

        // SearchView
        /*SearchView searchView = findViewById(R.id.searchview_home);
        ImageView searchIcon = searchView.findViewById(androidx.appcompat.R.id.search_mag_icon);
        searchIcon.setColorFilter(getResources().getColor(R.color.white));
        searchView.setQueryHint("Búsquea rápida");*/


        // Implementar Toolbar
        Toolbar toolbar = findViewById(R.id.toolbarRecipesList);
        toolbar.setTitle(R.string.recipe_list_title);
        setSupportActionBar(toolbar);


        // Floating button
        FloatingActionButton floatingActionButton = findViewById(R.id.floatingButton);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivityTest.this,RecipeListActivity.class);
                startActivity(intent);
            }
        });


    }

}
