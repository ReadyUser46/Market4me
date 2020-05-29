package com.example.market4me;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;

import com.example.market4me.adapters.RecipeAdapter;
import com.example.market4me.models.Recipe;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class RecipeListFragment extends Fragment {

    private FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    private CollectionReference recipeRef = firebaseFirestore.collection("Recipes");
    private RecipeAdapter mRecipeAdapter;
    private Recipe mRecipe;

    private static final int RC_SIGN_IN = 237;


    @Override
    public void onStart() {
        super.onStart();
        mRecipeAdapter.startListening();


    }

    @Override
    public void onStop() {
        super.onStop();
        mRecipeAdapter.stopListening(); //Mientras la app esté en background, el recyclerview no actualiza nada, para no gastar recursos
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_recipes_list, container, false);
        setUpRecyclerView(view);

        FloatingActionButton floatingActionButton = view.findViewById(R.id.floatingButton);
        floatingActionButton.setOnClickListener(new FloatingButtonListener());

        // Implementar Toolbar
        Toolbar toolbar = view.findViewById(R.id.toolbarRecipesList);
        toolbar.setTitle(R.string.recipe_list_title);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);


        // Navigation Drawer
        DrawerLayout mDrawer = getActivity().findViewById(R.id.drawer_layout);

        // Navigation Drawer Icon (Burger)
        ActionBarDrawerToggle toggleBurger = new ActionBarDrawerToggle(
                getActivity(),
                mDrawer,
                toolbar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);
        mDrawer.addDrawerListener(toggleBurger);
        toggleBurger.syncState();


        return view;
    }

    private void setUpRecyclerView(View v) {

        //Al constructor del adapter hay que pasarle un objeto FirestoreRecyclerOptions.
        //No es más que un objeto que le dice al adapter en que orden mostrar los elementos
        Query query = recipeRef.orderBy("title", Query.Direction.ASCENDING);

        FirestoreRecyclerOptions<Recipe> options = new FirestoreRecyclerOptions.Builder<Recipe>()
                .setQuery(query, Recipe.class)
                .build();

        mRecipeAdapter = new RecipeAdapter(options, getContext()); // le pasamos el context para poder tener acceso a string resources
        RecyclerView recyclerView = v.findViewById(R.id.recipesRecyclerView);
        recyclerView.setHasFixedSize(true); // Si todas las views tienen el mismo tamaño, se optimiza el código mucho poniendo a true.
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(mRecipeAdapter);


        // Deslizar para borrar
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
            private Drawable deleteIcon = getActivity().getDrawable(R.drawable.ic_delete_grey_24dp);
            private final ColorDrawable background = new ColorDrawable(Color.WHITE);

            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                mRecipeAdapter.deleteUndoRecipe(viewHolder.getAdapterPosition());

            }


        }).attachToRecyclerView(recyclerView);


        // listener
        mRecipeAdapter.setOnItemClickListener(new AdapterListener());
    }

    class FloatingButtonListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(getActivity(), NewRecipeActivity.class);

            startActivity(intent);
        }
    }

    // Listener adapter class, check adapters.RecipeAdapter as well
    class AdapterListener implements RecipeAdapter.OnItemClickListener {
        @Override
        public void onItemClick(DocumentSnapshot documentSnapshot, int position) {
            mRecipe = documentSnapshot.toObject(Recipe.class);
            String recipeId = documentSnapshot.getId();
            Intent intent = DisplayRecipeActivity.newIntent(getContext(), mRecipe, recipeId);
            startActivity(intent);

        }
    }
}
