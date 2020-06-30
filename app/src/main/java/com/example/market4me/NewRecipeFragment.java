package com.example.market4me;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.FileProvider;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.market4me.models.Recipe;
import com.example.market4me.utils.ViewCreator;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import static android.app.Activity.RESULT_OK;


public class NewRecipeFragment extends Fragment {

    // MEMBER VARIABLES
    private LinearLayout mRootLayout;

    private TextInputEditText mTitleEditText, mPeopleEditText, mTimeEditText, mPreparationEditText;
    private TextInputLayout mTilTitle, mTilPeople, mTilTime;
    private Button mSaveButton;
    private int mTime, mPeople;
    private String mTitle;

    private String mPhotoName;

    private List<String> mIngredientsList;
    private List<Integer> mQuantitiesList;
    private List<String> mUnitsList;
    private List<TextInputEditText> mIngredientEditTexts;
    private List<TextInputEditText> mQuantityEditTexts;
    private List<TextInputLayout> mIngredientTils;
    private List<TextInputLayout> mQuantityTils;
    private List<Spinner> mSpinners;

    private ImageButton mImageButton;
    private TextView mImgBtnText;
    private Uri mPhotoUri;

    private Recipe mRecipe;
    private String mRecipeId;
    private String mUserId;

    private boolean mFlagEdit;
    private DrawerLayout mDrawer;

    // CONSTANTS
    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final String ARG_USER_ID = "fireStore_UserId";
    private LinearLayout mRootLayout2;

    // Activity to Fragment Communication
    public static NewRecipeFragment newInstance(String userId) {
        NewRecipeFragment fragment = new NewRecipeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_USER_ID, userId);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        // init Firebase
        //FirebaseFirestore db = FirebaseFirestore.getInstance();
        //mRecipesRef = db.collection("Recipes");

        // init arraylists
        mIngredientEditTexts = new ArrayList<>();
        mQuantityEditTexts = new ArrayList<>();
        mIngredientTils = new ArrayList<>();
        mQuantityTils = new ArrayList<>();
        mSpinners = new ArrayList<>();
        mIngredientsList = new ArrayList<>();
        mQuantitiesList = new ArrayList<>();
        mUnitsList = new ArrayList<>();

        // get bundle (data) from activity
        Bundle args = getArguments();
        mUserId = args.getString(ARG_USER_ID);

        // get intent extras
        mRecipe = (Recipe) getActivity().getIntent().getSerializableExtra(NewRecipeActivity.EXTRA_RECIPE_OBJECT2);
        mRecipeId = getActivity().getIntent().getStringExtra(NewRecipeActivity.EXTRA_RECIPE_ID2);
        Log.i("patapum", "mRecipeId= " + mRecipeId);


        // new or edit
        if (mRecipe == null) {
            mRecipe = new Recipe();
        } else {
            Log.i("patapum", "Hay intent extras");
            mFlagEdit = true;
        }

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        // Inflamos el layout
        View view = inflater.inflate(R.layout.fragment_new_recipe2, container, false);


        // Binding of every element on the screen to his view + spinners
        findViews(view);

        // Toolbar implementation
        Toolbar toolbarNewRecipe = view.findViewById(R.id.toolbarNewRecipe);
        toolbarNewRecipe.setTitle(R.string.new_recipe_title);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbarNewRecipe);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowTitleEnabled(false);


        // Edit or New Recipe
        int blockPosition = 2;
        if (mFlagEdit) {
            mTitleEditText.setText(mRecipe.getTitle());
            mPeopleEditText.setText(String.valueOf(mRecipe.getPeople()));
            mTimeEditText.setText(String.valueOf(mRecipe.getTime()));
            mPreparationEditText.setText(mRecipe.getPreparation());
            mIngredientsList = mRecipe.getIngredients();
            mQuantitiesList = mRecipe.getQuantities();
            mUnitsList = mRecipe.getUnits();

            for (int i = 0; i < mIngredientsList.size(); i++) {

                addBlockEditTexts(blockPosition, i, false);
                blockPosition++;
            }
            addBlockEditTexts(blockPosition, 9999, true);
        } else {
            // Create new block
            /* Crear y situar el primer bloque de editTexts en tiempo real. Le adjuntamos el listener también */
            addBlockEditTexts(blockPosition, 0, true);

            //View inflatedLayout = inflater.inflate(R.layout.block_ingredients,mRootLayout, false);
            //mRootLayout.addView(inflatedLayout,blockPosition);
        }

        // Listener para todos los editTexts excepto el bloque
        mTimeEditText.addTextChangedListener(new RemoveErrorListener(mTilTime));
        mTitleEditText.addTextChangedListener(new RemoveErrorListener(mTilTitle));
        mPeopleEditText.addTextChangedListener(new RemoveErrorListener(mTilPeople));

        // Custom listener para guardar
        mSaveButton.setOnClickListener(new SaveButtonListener());

        // Custom listener para la camara
        mImageButton.setOnClickListener(new CameraIntentListener());

        //Populate FireStore database for testing
        /*for (int i = 0; i < 5; i++) {
            mRecipe.setTitle("Receta " + i);
            mRecipe.setPeople(1);
            mRecipe.setTime(1);
            mIngredientsList.add("patatas");
            mQuantitiesList.add(1);
            mUnitsList.add("Kg");
            mRecipe.setIngredients(mIngredientsList);
            mRecipe.setQuantities(mQuantitiesList);
            mRecipe.setUnits(mUnitsList);

                    FirebaseFirestore.getInstance().collection("Users").document(mUserId).collection("Recipes").add(mRecipe);

            //mRecipesRef.add(mRecipe);

        }*/

        // Navigation Drawer
        mDrawer = getActivity().findViewById(R.id.drawer_layout);

        // Navigation Drawer Icon (Burger)
        ActionBarDrawerToggle toggleBurger = new ActionBarDrawerToggle(
                getActivity(),
                mDrawer,
                toolbarNewRecipe,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);
        mDrawer.addDrawerListener(toggleBurger);
        toggleBurger.syncState();


        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {


            Log.i("patapum", "mPhotoUri_onActivityResult = " + mPhotoUri);

            /*Picasso.get()
                    .load(mPhotoUri)
                    .into(mThumbnailPhoto);*/

            mImageButton.setBackgroundColor(Color.TRANSPARENT);
            mImgBtnText.setVisibility(View.INVISIBLE);


            Glide.
                    with(getActivity())
                    .load(mPhotoUri)
                    .centerCrop()
                    .into(mImageButton);
        }
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        inflater.inflate(R.menu.new_recipe_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {

            case R.id.save_menu_icon:
                new SaveButtonListener().onClick(null);
                return true;

            default:
                return super.onOptionsItemSelected(item);

        }

    }

    private void findViews(View view) {

        mRootLayout = view.findViewById(R.id.linearlayout_ingredients);
        mRootLayout2 = view.findViewById(R.id.tmp_rootlayout);

        mTilTitle = view.findViewById(R.id.til_et_title);
        mTilPeople = view.findViewById(R.id.til_et_people);
        mTilTime = view.findViewById(R.id.til_et_time);

        mTitleEditText = view.findViewById(R.id.et_title);
        mPeopleEditText = view.findViewById(R.id.et_people);
        mTimeEditText = view.findViewById(R.id.et_time);
        mPreparationEditText = view.findViewById(R.id.et_preparation);

        mSaveButton = view.findViewById(R.id.but_save);
        mImageButton = view.findViewById(R.id.ibtn_take_picture);
        mImgBtnText = view.findViewById(R.id.tv_imgbtn);

    }

    private void addBlockEditTexts(int blockPosition, int index, boolean activated) {
        ViewCreator viewCreator = new ViewCreator();
        mRootLayout.addView(viewCreator.newBlockEditTexts(getContext()), blockPosition);

        TextInputEditText ingredientET = viewCreator.getNewIngredientET();
        TextInputEditText quantityET = viewCreator.getNewQuantityET();
        TextInputLayout tilIngredient = viewCreator.getNewTilIngredient();
        TextInputLayout tilQuantity = viewCreator.getNewTilQuantity();
        Spinner spinner = viewCreator.getNewSpinner();


        try { /*edit recipe*/
            if (mFlagEdit && index < mIngredientsList.size()) {
                ingredientET.setText(mIngredientsList.get(index));
                quantityET.setText(String.valueOf(mQuantitiesList.get(index)));
            }
        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
        }

        mIngredientEditTexts.add(ingredientET);
        mQuantityEditTexts.add(quantityET);
        mIngredientTils.add(tilIngredient);
        mQuantityTils.add(tilQuantity);
        mSpinners.add(spinner);


        /* listeners */
        ingredientET.addTextChangedListener(new AddEditTextsListener(
                getContext(),
                blockPosition,
                activated,
                tilIngredient));
        quantityET.addTextChangedListener(new RemoveErrorListener(tilQuantity));
    }

    private File createPhotoFile() {
        // Create a file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        mPhotoName = "IMG_" + timeStamp + ".jpg";
        return new File(getActivity().getFilesDir(), mPhotoName);
    }

    class SaveButtonListener implements View.OnClickListener { //Inner class which implements a custom listener

        @Override
        public void onClick(View v) {

            // init Arrays
            mIngredientsList = new ArrayList<>();
            mQuantitiesList = new ArrayList<>();
            mUnitsList = new ArrayList<>();
            //why here?
            /*
             volvemos a inicializar los arraylist para que se borre el contenido que tenían y no haya conflicto
             cuando el usuario escriba ingredientes, pulse save, validación no pase y cambie ingredientes.
             Si no iniciailzamos de nuevo, la receta se guarda con los ingredientes nuevos + anteriores
             */

            // VALIDATION
            boolean fieldsValidation = true;

            // title
            if (TextUtils.isEmpty(mTitleEditText.getText())) {
                mTilTitle.setError(getString(R.string.title_error));
                fieldsValidation = false;

            } else mTitle = mTitleEditText.getText().toString();

            // people
            if (TextUtils.isEmpty(mPeopleEditText.getText())) {
                mTilPeople.setError(getString(R.string.people_error));
                fieldsValidation = false;
            } else mPeople = Integer.parseInt(mPeopleEditText.getText().toString());

            // time
            if (TextUtils.isEmpty(mTimeEditText.getText())) {
                mTilTime.setError(getString(R.string.time_error));
                fieldsValidation = false;
            } else mTime = Integer.parseInt(mTimeEditText.getText().toString());

            // preparation
            String preparation;
            if (TextUtils.isEmpty(mPreparationEditText.getText())) {
                preparation = "";
            } else preparation = mPreparationEditText.getText().toString();

            // ingredients and quantities
            /*Con este bucle for, recorremos todos los editTexts de ingredientes, cantidades y spinner,
             * cogemos los valores que el usuario ha introducido y vamos poblando la lista de ingredientes, cantidades y spinner,
             * para posteriormente, pasarlos al objeto receta*/
            for (int i = 0; i < mIngredientEditTexts.size(); i++) {
                TextInputEditText et_ingredient = mIngredientEditTexts.get(i);
                TextInputEditText et_quantity = mQuantityEditTexts.get(i);
                Spinner spinner = mSpinners.get(i);

                // ingredients
                if (TextUtils.isEmpty(et_ingredient.getText())) {
                    //empty
                    if (i == 0) {
                        mIngredientTils.get(i).setError(getString(R.string.ingredient0_error));
                        fieldsValidation = false;
                    }
                } else {
                    mIngredientsList.add(et_ingredient.getText().toString());
                    mUnitsList.add(spinner.getSelectedItem().toString());

                }
                // quantities
                if (TextUtils.isEmpty(et_quantity.getText())) {
                    //empty
                    if (i == 0) {
                        mQuantityTils.get(i).setError(getString(R.string.quantity0_error));
                        fieldsValidation = false;
                    }
                } else {
                    mQuantitiesList.add(Integer.valueOf(et_quantity.getText().toString()));
                }

            }


            if (fieldsValidation) { // Ningún editText queda vacío


                mRecipe.setTitle(mTitle);
                mRecipe.setPeople(mPeople);
                mRecipe.setTime(mTime);
                mRecipe.setPreparation(preparation);
                mRecipe.setIngredients(mIngredientsList);
                mRecipe.setQuantities(mQuantitiesList);
                mRecipe.setUnits(mUnitsList);
                mRecipe.setPhotoName(mPhotoName);
                mPhotoName = mRecipe.getPhotoName();

                FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();

                if (mFlagEdit) {
                    // Update recipe
                    firebaseFirestore.collection("Users").document(mUserId).collection("Recetas").document(mRecipeId).set(mRecipe);

                } else {
                    // New Recipe
                    Log.i("patapum", "User ID = " + mUserId);
                    firebaseFirestore.collection("Users").document(mUserId).collection("Recipes").add(mRecipe);

                    FirebaseStorage mStorage = FirebaseStorage.getInstance();
                    StorageReference storageReference = mStorage.getReference().child("Pictures").child(mUserId).child(mPhotoName);
                    storageReference.putFile(mPhotoUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Toast.makeText(getActivity(), "Uploading finished", Toast.LENGTH_LONG).show();

                        }
                    });
                }

                Intent intent = RecipeListActivity.newIntent(getContext()); // intent
                startActivity(intent);


            }

        }
    }

    class CameraIntentListener implements View.OnClickListener {


        @Override
        public void onClick(View v) {

            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            PackageManager packageManager = Objects.requireNonNull(getActivity()).getPackageManager();


            // Comprobamos que el dispositivo tiene una camara antes de darle funcionalidad al boton
            // y que el packagemanager tiene alguna app que pueda manejar este intent. Boolean canTakePhoto
            if (packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY) &&
                    takePictureIntent.resolveActivity(packageManager) != null) {


                File mPhotoFile = createPhotoFile();

                mPhotoUri = FileProvider.getUriForFile(getContext(),
                        "com.example.market4me.fileprovider",
                        mPhotoFile);


                // si le damos un output a la foto, no habrá data en onActivityResult para la miniatura
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, mPhotoUri);

                List<ResolveInfo> cameraActivities = packageManager.queryIntentActivities(takePictureIntent,
                        PackageManager.MATCH_DEFAULT_ONLY);

                for (ResolveInfo activity : cameraActivities) {
                    getActivity().grantUriPermission(activity.activityInfo.packageName,
                            mPhotoUri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION);

                }
                Log.i("patapum", "mPhotoUri_intent = " + mPhotoUri);


                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            } else {
                Toast.makeText(getActivity(), getActivity().getString(R.string.no_camera_error), Toast.LENGTH_SHORT).show();
            }
        }
    }

    class AddEditTextsListener implements TextWatcher { //Inner class which implements a custom listener for text changes.

        Context context;
        int position;
        boolean isActivated;
        TextInputLayout tilIngredient;


        private AddEditTextsListener(Context context, int position, boolean isActivated, TextInputLayout tilIngredient) {
            this.context = context;
            this.position = position;
            this.isActivated = isActivated;
            this.tilIngredient = tilIngredient;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

            if (s.length() > 0) tilIngredient.setError(null);

            //Añadimos bloque EditTexts con listeners
            if (isActivated) {

                position++;
                ViewCreator viewCreator = new ViewCreator();
                viewCreator.newBlockEditTexts(getContext());
                TextInputEditText ingredientET = viewCreator.getNewIngredientET();
                TextInputEditText quantityET = viewCreator.getNewQuantityET();
                TextInputLayout tilIngredient = viewCreator.getNewTilIngredient();
                TextInputLayout tilQuantity = viewCreator.getNewTilQuantity();
                Spinner spinner = viewCreator.getNewSpinner();

                mIngredientEditTexts.add(ingredientET);
                mQuantityEditTexts.add(quantityET);
                mIngredientTils.add(tilIngredient);
                mQuantityTils.add(tilQuantity);
                mSpinners.add(spinner);

                mRootLayout.addView(viewCreator.getTempLinearLayout(), position);

                /* listeners attached */
                ingredientET.addTextChangedListener(new AddEditTextsListener(
                        getContext(),
                        position,
                        isActivated,
                        tilIngredient));
                quantityET.addTextChangedListener(new RemoveErrorListener(tilQuantity));

            }

            isActivated = false;

        }

        @Override
        public void afterTextChanged(Editable s) {

        }

    }

    class RemoveErrorListener implements TextWatcher {

        TextInputLayout mTextInputLayout;

        public RemoveErrorListener(TextInputLayout textInputLayout) {
            mTextInputLayout = textInputLayout;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (s.length() > 0) {
                mTextInputLayout.setError(null);
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    }


}


