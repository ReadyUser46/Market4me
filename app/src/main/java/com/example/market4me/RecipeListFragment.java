package com.example.market4me;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.market4me.data.Recipe;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class RecipeListFragment extends Fragment {

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference recipeRef = db.collection("Recipes");
    private RecipeAdapter mRecipeAdapter;

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


        return v;


    }

    private void setUpRecyclerView(View v) {

        //Al constructor del adapter hay que pasarle un objeto FirestoreRecyclerOptions.
        //No es más que un objeto que le dice al adapter en que orden mostrar los elementos

        Query query = recipeRef.orderBy("title", Query.Direction.DESCENDING);

        FirestoreRecyclerOptions<Recipe> options = new FirestoreRecyclerOptions.Builder<Recipe>()
                .setQuery(query, Recipe.class)
                .build();

        mRecipeAdapter = new RecipeAdapter(options);
        RecyclerView recyclerView = v.findViewById(R.id.recipesRecyclerView);
        recyclerView.setHasFixedSize(true); // Si todas las views tienen el mismo tamaño, se optimiza el código mucho poniendo a true.
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(mRecipeAdapter);
    }
}
