package com.example.market4me;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.market4me.data.Recipe;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

public class RecipeAdapter extends FirestoreRecyclerAdapter<Recipe, RecipeAdapter.RecipeHolder> {


    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public RecipeAdapter(@NonNull FirestoreRecyclerOptions<Recipe> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull RecipeHolder holder, int position, @NonNull Recipe model) {
        holder.tvTittle.setText(model.getTitle());
        holder.tvTime.setText(String.valueOf(model.getTime()));
        holder.tvPeople.setText(String.valueOf(model.getPeople()));
    }

    @NonNull
    @Override
    public RecipeHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.items_recyclerview,parent, false);
        RecipeHolder holder = new RecipeHolder(view);

        return holder;
    }

    class RecipeHolder extends RecyclerView.ViewHolder {
        TextView tvTittle, tvPeople, tvTime;

        public RecipeHolder(@NonNull View itemView) {
            super(itemView);

            tvTittle = itemView.findViewById(R.id.tv_title);
            tvPeople = itemView.findViewById(R.id.tv_people);
            tvTime = itemView.findViewById(R.id.tv_time);



        }
    }


}
