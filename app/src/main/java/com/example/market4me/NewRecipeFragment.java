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

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import static android.app.Activity.RESULT_OK;


public class NewRecipeFragment extends Fragment {

    // MEMBER VARIABLES
    private TextInputEditText mTitleEditText, mPeopleEditText, mTimeEditText, mPreparationEditText;
    private TextInputEditText mIngredientEditText0, mQuantityEditText0;
    private TextInputLayout mTilTitle, mTilPeople, mTilTime, mTilIngredient, mTilQuantity;
    private Spinner mUnitSpinner0;
    private Button mSaveButton;

    private ArrayAdapter<CharSequence> spinnerAdapter;

    private String mTitle;
    private String mPhotoName;
    private int mTime, mPeople;

    private List<String> mIngredientsList;
    private List<Integer> mQuantitiesList;
    private List<String> mUnitsList;
    private List<TextInputEditText> mIngredientEditTexts;
    private List<TextInputEditText> mQuantityEditTexts;
    private List<Spinner> mSpinners;

    private CollectionReference mRecipesRef;

    private ImageButton mImageButton;
    private ImageView mThumbnailPhoto;
    private Uri mPhotoUri;

    private Recipe mRecipe;

    // CONSTANTS
    private static final int REQUEST_IMAGE_CAPTURE = 1;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mRecipe = new Recipe();

        // init arraylists
        mIngredientsList = new ArrayList<>();
        mQuantitiesList = new ArrayList<>();
        mUnitsList = new ArrayList<>();
        mIngredientEditTexts = new ArrayList<>();
        mQuantityEditTexts = new ArrayList<>();
        mSpinners = new ArrayList<>();


        // init Firebase
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        mRecipesRef = db.collection("Recipes");


    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        Log.i("patapum", "Context.getFilesDir =" + getContext().getFilesDir());
        File storageDir = getContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        Log.i("patapum", "Context.getExternalFilesDir =" + storageDir);


        View view = inflater.inflate(R.layout.fragment_new_recipe, container, false);

        findViews(view); // Binding of every element on the screen to his view + spinners

        // Toolbar implementation
        Toolbar toolbarNewRecipe = view.findViewById(R.id.toolbarNewRecipe);
        toolbarNewRecipe.setTitle(R.string.new_recipe_title);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbarNewRecipe);


        mIngredientEditTexts.add(mIngredientEditText0);
        mQuantityEditTexts.add(mQuantityEditText0);
        mSpinners.add(mUnitSpinner0);

        mTimeEditText.addTextChangedListener(new RemoveErrorListener(mTilTime));
        mTitleEditText.addTextChangedListener(new RemoveErrorListener(mTilTitle));
        mPeopleEditText.addTextChangedListener(new RemoveErrorListener(mTilPeople));
        mQuantityEditText0.addTextChangedListener(new RemoveErrorListener(mTilQuantity));
        mIngredientEditText0.addTextChangedListener(new AddEditTextsListener(getContext(), 4, true, view));


        // Custom listener para guardar
        mSaveButton.setOnClickListener(new SaveButtonListener());

        // Custom listener para la camara
        mImageButton.setOnClickListener(new CameraIntentListener());

        // Populate FireStore database for testing
        /*for (int i = 0; i < 20; i++) {
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

        mTilTitle = view.findViewById(R.id.til_et_title);
        mTilPeople = view.findViewById(R.id.til_et_people);
        mTilTime = view.findViewById(R.id.til_et_time);
        mTilIngredient = view.findViewById(R.id.til_et_ingredient0);
        mTilQuantity = view.findViewById(R.id.til_et_quantity0);

        mTitleEditText = view.findViewById(R.id.et_title);
        mPeopleEditText = view.findViewById(R.id.et_people);
        mTimeEditText = view.findViewById(R.id.et_time);
        mIngredientEditText0 = view.findViewById(R.id.et_ingredient0);
        mQuantityEditText0 = view.findViewById(R.id.et_quantity0);
        mPreparationEditText = view.findViewById(R.id.et_preparation);

        mUnitSpinner0 = view.findViewById(R.id.spinner0);
        mSaveButton = view.findViewById(R.id.but_save);
        mImageButton = view.findViewById(R.id.ibtn_take_picture);
        mThumbnailPhoto = view.findViewById(R.id.imageview_thumbnail_photo);

        // SPINNER SETUP
        // Create an ArrayAdapter using the string array and a default spinner layout
        spinnerAdapter = ArrayAdapter.createFromResource(getActivity(), R.array.quantity_units, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mUnitSpinner0.setAdapter(spinnerAdapter);


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


            //el contenido del edittext no es ""? pues la variable toma el valor de lo que hay en el et. Que es ""? pues toma 0
            mPeople = !mPeopleEditText.getText().toString().trim().equals("") ? Integer.parseInt(mPeopleEditText.getText().toString()) : 0;
            mTime = !mTimeEditText.getText().toString().trim().equals("") ? Integer.parseInt(mTimeEditText.getText().toString()) : 0;
            mTitle = mTitleEditText.getText().toString();
            String preparation = mPreparationEditText.getText().toString();

            /*Con este bucle for, recorremos todos los editTexts de ingredientes, cantidades y spinner,
             * cogemos los valores que el usuario ha introducido y vamos poblando la lista de ingredientes, cantidades y spinner,
             * para posteriormente, pasarlos al objeto receta*/
            for (int i = 0; i < mIngredientEditTexts.size(); i++) {
                TextInputEditText et_ingredient = mIngredientEditTexts.get(i);
                TextInputEditText et_quantity = mQuantityEditTexts.get(i);
                Spinner spinner = mSpinners.get(i);

                int tmp_int = !et_quantity.getText().toString().trim().equals("") ? Integer.parseInt(et_quantity.getText().toString()) : 0;
                String tmp_str = et_ingredient.getText().toString();
                if (tmp_str.length() > 0 || tmp_int != 0) {
                    mIngredientsList.add(tmp_str);
                    mQuantitiesList.add(tmp_int);
                    mUnitsList.add(spinner.getSelectedItem().toString());
                }
            }

            if (validateFields()) { // Ningún editText queda vacío o es 0


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
                Intent intent = RecipeListActivity.newIntent(getContext());
                startActivity(intent);
            }

            //volvemos a inicializar los arraylist para que se borre el contenido que tenían y no haya conflicto
            //con el metodo .add en en caso de que el usuario no escriba ningún ingrediente.

            mIngredientsList = new ArrayList<>();
            mQuantitiesList = new ArrayList<>();
            mUnitsList = new ArrayList<>();

        }

        private boolean validateFields() {
            boolean validate = true;


            if (mTitle.length() == 0) {
                mTilTitle.setError(getString(R.string.title_error));
                validate = false;
            }
            if (mPeople == 0) {
                mTilPeople.setError(getString(R.string.people_error));
                validate = false;
            }
            if (mTime == 0) {
                mTilTime.setError(getString(R.string.time_error));
                validate = false;
            }
            if (mIngredientsList.size() == 0) {
                mTilIngredient.setError(getString(R.string.ingredient0_error));
                validate = false;
            }
            if (mQuantitiesList.size() == 0) {
                mTilQuantity.setError(getString(R.string.quantity0_error));
                validate = false;
            }

            return validate;

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
        View view;
        LinearLayout rootLayout;


        private AddEditTextsListener(Context context, int position, boolean isActivated, View view) {
            this.context = context;
            this.position = position;
            this.isActivated = isActivated;
            this.view = view;
            rootLayout = view.findViewById(R.id.linearlayout_ingredients);
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

            if (s.length() > 0) mTilIngredient.setError(null);
            addNewEditTexts();


        }

        @Override
        public void afterTextChanged(Editable s) {

        }

        private void addNewEditTexts() {
            if (isActivated) {

                // LinearLayout temporal
                LinearLayout tempLinearLayout = ViewCreator.linearLayout(getContext(), 0);
                tempLinearLayout.setLayoutParams(ViewCreator.layoutParams(getContext(), -1, -1));


                // nuevo textInputEditText para ingredientes y added to linearLayout temporal
                TextInputEditText newIngredient = new TextInputEditText(getContext());
                LinearLayout.LayoutParams textInputEditTextParams = ViewCreator.layoutParams(getContext(), -1, -1);

                TextInputLayout newTilIngredient = new TextInputLayout(getContext());
                LinearLayout.LayoutParams ingredientTilParams = ViewCreator.layoutParams(getContext(), -1, -2, 1f);

                newTilIngredient.addView(newIngredient, textInputEditTextParams);
                newTilIngredient.setHint("Ingrediente");
                newTilIngredient.setEndIconMode(TextInputLayout.END_ICON_CLEAR_TEXT);


                // nuevo textInputEditText para cantidades y added to linearLayout temporal
                TextInputEditText newQuantity = new TextInputEditText(getContext());
                newQuantity.setInputType(InputType.TYPE_CLASS_NUMBER);
                TextInputLayout newTilQuantity = new TextInputLayout(getContext());
                LinearLayout.LayoutParams quantityTilParams = ViewCreator.layoutParams(getContext(), -1, -2, 2f);

                newTilQuantity.addView(newQuantity, textInputEditTextParams);
                newTilQuantity.setHint("Cantidad");


                // nuevo Spinner para ingredientes y add to linearLayout temporal
                Spinner newSpinner = new Spinner(getContext());
                LinearLayout.LayoutParams spinnerParams = ViewCreator.layoutParams(getContext(), -1, -2, 2.2f);
                newSpinner.setAdapter(spinnerAdapter);

                // Add EditText and spinner to the LinearLayout Horizontal and this LL to the rootView
                tempLinearLayout.addView(newTilIngredient, ingredientTilParams);
                tempLinearLayout.addView(newTilQuantity, quantityTilParams);
                tempLinearLayout.addView(newSpinner, spinnerParams);

                mIngredientEditTexts.add(newIngredient);
                mQuantityEditTexts.add(newQuantity);
                mSpinners.add(newSpinner);

                rootLayout.addView(tempLinearLayout, position);

                // Aumentamos posición y añadimos listener al editText generado.
                position++;
                newIngredient.addTextChangedListener(new AddEditTextsListener(getContext(), position, true, view));


            }

            isActivated = false;
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


