package com.example.market4me;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

public class HomeDashBoardFragment extends Fragment {

    // Activity to Fragment Communication
    public static HomeDashBoardFragment newInstance() {
        HomeDashBoardFragment fragment = new HomeDashBoardFragment();

        //Bundle args = new Bundle();
        //fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dashboard, container, false);

        // Navigation Drawer
        DrawerLayout mDrawer = getActivity().findViewById(R.id.drawer_layout);

        // Navigation Drawer Icon (Burger)
        ActionBarDrawerToggle toggleBurger = new ActionBarDrawerToggle(
                getActivity(),
                mDrawer,

                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);
        mDrawer.addDrawerListener(toggleBurger);
        toggleBurger.syncState();


        // Cards
        CardView card1 = view.findViewById(R.id.card1);
        CardView card2 = view.findViewById(R.id.card2);

        // CardListeners
        card1.setOnClickListener(new CardListener(RecipeListActivity.class));
        card2.setOnClickListener(new CardListener(NewRecipeActivity.class));

        return view;
    }


    class CardListener implements View.OnClickListener {

        private Class classTarget;

        public CardListener(Class classTarget) {
            this.classTarget = classTarget;
        }

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(getContext(), classTarget);
            startActivity(intent);

        }
    }

}
