package com.example.market4me.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.market4me.R;
import com.example.market4me.models.Recipe;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.firebase.ui.firestore.ObservableSnapshotArray;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;

public class RecipeAdapter extends FirestoreRecyclerAdapter<Recipe, RecipeAdapter.RecipeHolder> {

    Context mContext;

    private OnItemClickListener mListener;

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
        holder.tvTime.setText(String.format("%s: %s hora", mContext.getString(R.string.hint_time),String.valueOf(model.getTime())));
        holder.tvPeople.setText(String.format("%s: %s", mContext.getString(R.string.hint_people), String.valueOf(model.getPeople())));
    }

    @NonNull
    @Override
    public RecipeHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.items_recyclerview, parent, false);
        RecipeHolder holder = new RecipeHolder(view);

        return holder;
    }


    public void deleteRecipe(int position) {
        ObservableSnapshotArray<Recipe> observableSnapshotArray = getSnapshots();
        DocumentReference documentReference = observableSnapshotArray.getSnapshot(position).getReference();
        documentReference.delete();
    }

    class RecipeHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
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

        @Override
        public void onClick(View v) {

            getSnapshots().getSnapshot(getAdapterPosition());

            Toast.makeText(v.getContext(), "position: " + getAdapterPosition(), Toast.LENGTH_SHORT).show();
        }
    }

    public interface OnItemClickListener {
        void onItemClick(DocumentSnapshot documentSnapshot, int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;

    }


}
