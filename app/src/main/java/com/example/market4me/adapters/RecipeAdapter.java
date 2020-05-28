package com.example.market4me.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.market4me.R;
import com.example.market4me.models.Recipe;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.firebase.ui.firestore.ObservableSnapshotArray;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;


/*ADAPTER CLASS*/
public class RecipeAdapter extends FirestoreRecyclerAdapter<Recipe, RecipeAdapter.RecipeHolder> {

    private Context mContext;
    private OnItemClickListener mListener;
    private ViewGroup mViewGroupRecycler;
    private Recipe mRecipeDeleted;

    private final String TAG = "patapum";


    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public RecipeAdapter(@NonNull FirestoreRecyclerOptions<Recipe> options, Context context) {
        super(options);
        this.mContext = context;
    }

    @Override
    protected void onBindViewHolder(@NonNull RecipeHolder holder, int position, @NonNull Recipe model) {
        holder.tvTittle.setText(model.getTitle());
        holder.tvTime.setText(String.format("%s: %s hora", mContext.getString(R.string.hint_time), String.valueOf(model.getTime())));
        holder.tvPeople.setText(String.format("%s: %s", mContext.getString(R.string.hint_people), String.valueOf(model.getPeople())));
    }

    @NonNull
    @Override
    public RecipeHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        mViewGroupRecycler = parent;
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_items, parent, false);
        RecipeHolder holder = new RecipeHolder(view);


        return holder;
    }

    public void deleteUndoRecipe(int position) {

        //Snapshot y Document Reference de la receta que hemos hecho swipe
        ObservableSnapshotArray<Recipe> observableSnapshotArray = getSnapshots();
        DocumentSnapshot recipeSnapshotDeleted = observableSnapshotArray.getSnapshot(position);
        final Recipe recipeUndo = recipeSnapshotDeleted.toObject(Recipe.class);
        final DocumentReference documentReference = recipeSnapshotDeleted.getReference();

        //Delete Recipe
        documentReference.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.i(TAG, "OnSuccess; Item deleted");
            }
        });

        //Undo Delete Recipe
        Snackbar undoSnackbar = Snackbar
                .make(mViewGroupRecycler, R.string.recipe_deleted_snackbar, BaseTransientBottomBar.LENGTH_LONG)
                .setAction("undo", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        documentReference.set(recipeUndo);
                    }
                });

        undoSnackbar.show();

    }


    /*HOLDER CLASS*/
    class RecipeHolder extends RecyclerView.ViewHolder {
        TextView tvTittle, tvPeople, tvTime;

        public RecipeHolder(@NonNull View itemView) {
            super(itemView);

            tvTittle = itemView.findViewById(R.id.tv_title);
            tvPeople = itemView.findViewById(R.id.tv_people);
            tvTime = itemView.findViewById(R.id.tv_time);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();

                    if (position != RecyclerView.NO_POSITION && mListener != null) {
                        mListener.onItemClick(getSnapshots().getSnapshot(getAdapterPosition()), position);
                    }
                }
            });

        }


    }

    public interface OnItemClickListener {
        void onItemClick(DocumentSnapshot documentSnapshot, int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;

    }


}
