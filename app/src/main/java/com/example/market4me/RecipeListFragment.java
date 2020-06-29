package com.example.market4me;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.market4me.adapters.RecipeAdapter;
import com.example.market4me.models.Recipe;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class RecipeListFragment extends Fragment {

    // MEMBER VARIABLES

    private RecipeAdapter mRecipeAdapter;
    private Recipe mRecipe;
    private boolean authListenerFlag;

    // CONSTANTS
    private static final String ARG_USER_ID = "fireStore_UserId";
    private static final String ARG_AUTH_OBJECT = "fireStore_user_auth";


    // Activity to Fragment Communication
    public static RecipeListFragment newInstance() {
        RecipeListFragment fragment = new RecipeListFragment();

        //Bundle args = new Bundle();
        //args.putSerializable(ARG_AUTH_OBJECT, userAuth);

        //args.putString(ARG_USER_ID, userId);
        //fragment.setArguments(args);
        return fragment;
    }


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

        // Menu
        setHasOptionsMenu(true);
        Bundle args = getArguments();
        //mUserId = args.getString(ARG_USER_ID);
        //mUserAuth = (UserAuth) args.getSerializable(ARG_AUTH_OBJECT);

        //Log.i("patapum", "User Id received in RecipeListFragment: " + FirebaseAuth.getInstance().getCurrentUser().getUid());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.fragment_recipes_list, container, false);

        // Setup RecyclerView
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        Log.i("patapum", "User Id received in RecipeListFragment: " + userId);
        setUpRecyclerView(view, userId);

        FloatingActionButton floatingActionButton = view.findViewById(R.id.floatingButton);
        floatingActionButton.setOnClickListener(new FloatingButtonListener());

        // Implementar Toolbar
        Toolbar toolbar = view.findViewById(R.id.toolbarRecipesList);
        //toolbar.setTitle(R.string.recipe_list_title);

        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowTitleEnabled(false);

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

        // Firebase Auth Listener
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseAuth.AuthStateListener mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                Log.i("patapum", "Auth listener detected something!");
                //String userId = firebaseAuth.getCurrentUser().getUid();
                if (!authListenerFlag && firebaseAuth.getCurrentUser().getUid() != null) {
                    setUpRecyclerView(view, firebaseAuth.getCurrentUser().getUid());
                }
            }
        };
        firebaseAuth.addAuthStateListener(mAuthStateListener);

        return view;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        inflater.inflate(R.menu.recipe_list_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {

            case R.id.search_menu_icon:
                Toast.makeText(getContext(), "Buscar", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.settings_menu_icon:
                Toast.makeText(getContext(), "Opciones", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }

    }

    private void setUpRecyclerView(View v, String userId) {

        // The first time we get in the setUpRecyclerView, the Auth listener get activated
        authListenerFlag = true;

        //Al constructor del adapter hay que pasarle un objeto FirestoreRecyclerOptions.
        //No es más que un objeto que le dice al adapter en que orden mostrar los elementos
        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();

        CollectionReference recipeRef = firebaseFirestore.collection("Users").document(userId).collection("Recipes");
        Query query = recipeRef.orderBy("title", Query.Direction.ASCENDING);

        FirestoreRecyclerOptions<Recipe> options = new FirestoreRecyclerOptions.Builder<Recipe>()
                .setQuery(query, Recipe.class)
                .build();

        mRecipeAdapter = new RecipeAdapter(options, getContext(), userId); // le pasamos el context para poder tener acceso a string resources
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
