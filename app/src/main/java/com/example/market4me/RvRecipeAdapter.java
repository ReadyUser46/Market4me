package com.example.market4me;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.market4me.data.Recipe;

import java.util.List;

public class RvRecipeAdapter extends RecyclerView.Adapter<RvRecipeAdapter.RecipeViewHolder> {

     private List<Recipe> mRecipeList;

    @NonNull
    @Override
    public RecipeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.items_recyclerview,parent,false);
        RecipeViewHolder holder = new RecipeViewHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeViewHolder viewHolder, int position) {

        viewHolder.title.setText(mRecipeList.get(position).getTitle());
        viewHolder.time.setText(mRecipeList.get(position).getTime());
        viewHolder.people.setText(mRecipeList.get(position).getPeople());

    }

    @Override
    public int getItemCount() {
        return mRecipeList.size();
    }



    public static class RecipeViewHolder extends RecyclerView.ViewHolder {

        private TextView title, people, time;

        public RecipeViewHolder(@NonNull View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.tv_title);
            people = itemView.findViewById(R.id.tv_people);
            time = itemView.findViewById(R.id.tv_time);

        }
    }



}
