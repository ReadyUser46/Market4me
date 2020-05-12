package com.example.market4me;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.market4me.data.Recipe;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class RecipeListFragment extends Fragment {

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference recipeRef = db.collection("Recipes");
    private RecipeAdapter mRecipeAdapter;

    private FloatingActionButton mFloatingActionButton;

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

        View v = inflater.inflate(R.layout.fragment_recipes_recyclerview, container, false);
        setUpRecyclerView(v);

        mFloatingActionButton = v.findViewById(R.id.floatingButton);
        mFloatingActionButton.setOnClickListener(new FloatingButtonListener());

        return v;


    }

    private void setUpRecyclerView(View v) {

        //Al constructor del adapter hay que pasarle un objeto FirestoreRecyclerOptions.
        //No es más que un objeto que le dice al adapter en que orden mostrar los elementos

        Query query = recipeRef.orderBy("title", Query.Direction.ASCENDING);

        FirestoreRecyclerOptions<Recipe> options = new FirestoreRecyclerOptions.Builder<Recipe>()
                .setQuery(query, Recipe.class)
                .build();

        mRecipeAdapter = new RecipeAdapter(options);
        RecyclerView recyclerView = v.findViewById(R.id.recipesRecyclerView);
        recyclerView.setHasFixedSize(true); // Si todas las views tienen el mismo tamaño, se optimiza el código mucho poniendo a true.
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(mRecipeAdapter);


        // Deslizar para borrar

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                mRecipeAdapter.deleteRecipe(viewHolder.getAdapterPosition());
            }
        }).attachToRecyclerView(recyclerView);
    }


    class FloatingButtonListener implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(getActivity(),NewRecipeActivity.class);
            startActivity(intent);
        }
    }
}
