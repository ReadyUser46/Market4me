package com.example.market4me;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.market4me.models.Recipe;
import com.example.market4me.utils.ViewCreator;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.w3c.dom.Text;

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

    private CollectionReference mRecipesRef;

    private ImageButton mImageButton;
    private ImageView mThumbnailPhoto;
    private Uri mPhotoUri;

    private Recipe mRecipe;

    private boolean mFlagExtras;
    private int blockPosition;

    // CONSTANTS
    private static final int REQUEST_IMAGE_CAPTURE = 1;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // init Firebase
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        mRecipesRef = db.collection("Recipes");

        // init arraylists
        mIngredientEditTexts = new ArrayList<>();
        mQuantityEditTexts = new ArrayList<>();
        mIngredientTils = new ArrayList<>();
        mQuantityTils = new ArrayList<>();
        mSpinners = new ArrayList<>();


        // get intent extras
        mRecipe = (Recipe) getActivity().getIntent().getSerializableExtra(NewRecipeActivity.EXTRA_RECIPE_OBJECT2);
        if (mRecipe == null) {
            mRecipe = new Recipe();

            mIngredientsList = new ArrayList<>();
            mQuantitiesList = new ArrayList<>();
            mUnitsList = new ArrayList<>();

        } else {
            Log.i("patapum", "Hay intent extras");
            mFlagExtras = true;
            mIngredientsList = mRecipe.getIngredients();
            mQuantitiesList = mRecipe.getQuantities();
            mUnitsList = mRecipe.getUnits();
        }

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        // Inflamos el layout
        View view = inflater.inflate(R.layout.fragment_new_recipe, container, false);


        // Binding of every element on the screen to his view + spinners
        findViews(view);

        // Toolbar implementation
        Toolbar toolbarNewRecipe = view.findViewById(R.id.toolbarNewRecipe);
        toolbarNewRecipe.setTitle(R.string.new_recipe_title);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbarNewRecipe);

        // Create new block
        /* Cuando creamos la view, al mismo tiempo, creamos y situamos el primer bloque de editTexts
         *  Le adjuntamos el listener también */

        blockPosition = 3;
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

        mRootLayout.addView(viewCreator.getTempLinearLayout(), blockPosition);

        /* listeners attached */
        ingredientET.addTextChangedListener(new AddEditTextsListener(
                getContext(),
                blockPosition,
                true,
                tilIngredient));
        quantityET.addTextChangedListener(new RemoveErrorListener(tilQuantity));

        // Editar Receta
        /*if (mFlagExtras) {
            mTitleEditText.setText(mRecipe.getTitle());
            mPeopleEditText.setText(String.valueOf(mRecipe.getPeople()));
            mTimeEditText.setText(String.valueOf(mRecipe.getTime()));
            mPreparationEditText.setText(mRecipe.getPreparation());
            mIngredientEditText0.setText(mIngredientsList.get(0));
            mQuantityEditText0.setText(String.valueOf(mQuantitiesList.get(0)));

            mIngredientEditTexts.add(mIngredientEditText0);
            mQuantityEditTexts.add(mQuantityEditText0);

            ViewCreator viewCreator = new ViewCreator();
            blockPosition = 4;
            for (int i = 1; i < mIngredientsList.size(); i++) {

                // new block
                viewCreator.newBlockEditTexts(getContext(), spinnerAdapter);
                TextInputEditText newIngredientET = viewCreator.getNewIngredientET();
                TextInputEditText newQuantityET = viewCreator.getNewQuantityET();
                Spinner newSpinner = viewCreator.getNewSpinner();

                newIngredientET.setText(mIngredientsList.get(i));
                newQuantityET.setText(String.valueOf(mQuantitiesList.get(i)));

                mIngredientEditTexts.add(newIngredientET);
                mQuantityEditTexts.add(newQuantityET);
                mSpinners.add(newSpinner);

                LinearLayout rootLayout = view.findViewById(R.id.linearlayout_ingredients);
                rootLayout.addView(viewCreator.getTempLinearLayout(), blockPosition);
                int nextBlockPosition = blockPosition + 1;

                // Aumentamos posición y añadimos listener al editText generado.
                newIngredientET.addTextChangedListener(new AddEditTextsListener(getContext(), nextBlockPosition, false, view));
                blockPosition++;

            }

            viewCreator.newBlockEditTexts(getContext(), spinnerAdapter);
            TextInputEditText newIngredientET = viewCreator.getNewIngredientET();
            TextInputEditText newQuantityET = viewCreator.getNewQuantityET();
            Spinner newSpinner = viewCreator.getNewSpinner();

            mIngredientEditTexts.add(newIngredientET);
            mQuantityEditTexts.add(newQuantityET);
            mSpinners.add(newSpinner);

            LinearLayout rootLayout = view.findViewById(R.id.linearlayout_ingredients);
            rootLayout.addView(viewCreator.getTempLinearLayout(), blockPosition);

            // Aumentamos posición y añadimos listener al editText generado.
            blockPosition++;
            newIngredientET.addTextChangedListener(new AddEditTextsListener(getContext(), blockPosition, true, view));
        }*/

        // Listener para todos los editTexts excepto el bloque
        mTimeEditText.addTextChangedListener(new RemoveErrorListener(mTilTime));
        mTitleEditText.addTextChangedListener(new RemoveErrorListener(mTilTitle));
        mPeopleEditText.addTextChangedListener(new RemoveErrorListener(mTilPeople));

        // Custom listener para guardar
        mSaveButton.setOnClickListener(new SaveButtonListener());

        // Custom listener para la camara
        mImageButton.setOnClickListener(new CameraIntentListener());

        /*Populate FireStore database for testing
        for (int i = 0; i < 20; i++) {
            mRecipe.setTitle("Receta " + i);
            mRecipe.setPeople(1);
            mRecipe.setTime(1);
            mIngredientsList.add("patatas");
            mQuantitiesList.add(1);
            mUnitsList.add("Kg");
            mRecipe.setIngredients(mIngredientsList);
            mRecipe.setQuantities(mQuantitiesList);
            mRecipe.setUnits(mUnitsList);

            mRecipesRef.add(mRecipe);

        }*/

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

            Glide.
                    with(getActivity())
                    .load(mPhotoUri)
                    .into(mThumbnailPhoto);

            if (mPhotoUri != null) {


                FirebaseStorage mStorage = FirebaseStorage.getInstance();
                StorageReference storageReference = mStorage.getReference().child("Pictures").child(mPhotoName);
                storageReference.putFile(mPhotoUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Toast.makeText(getActivity(), "Uploading finished", Toast.LENGTH_LONG).show();

                    }
                });
            }
        }
    }

    private void findViews(View view) {

        mRootLayout = view.findViewById(R.id.linearlayout_ingredients);

        mTilTitle = view.findViewById(R.id.til_et_title);
        mTilPeople = view.findViewById(R.id.til_et_people);
        mTilTime = view.findViewById(R.id.til_et_time);

        mTitleEditText = view.findViewById(R.id.et_title);
        mPeopleEditText = view.findViewById(R.id.et_people);
        mTimeEditText = view.findViewById(R.id.et_time);
        mPreparationEditText = view.findViewById(R.id.et_preparation);

        mSaveButton = view.findViewById(R.id.but_save);
        mImageButton = view.findViewById(R.id.ibtn_take_picture);
        mThumbnailPhoto = view.findViewById(R.id.imageview_thumbnail_photo);

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

                mRecipesRef.add(mRecipe); // upload la receta a fireStore


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

                blockPosition++;
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

                mRootLayout.addView(viewCreator.getTempLinearLayout(), blockPosition);
                //int nextBlockPosition = blockPosition + 1;

                /* listeners attached */
                ingredientET.addTextChangedListener(new AddEditTextsListener(
                        getContext(),
                        blockPosition,
                        true,
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


