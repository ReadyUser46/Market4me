package com.example.market4me;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;

public class MainActivityTest extends AppCompatActivity {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_main);

        ArrayList<TextInputLayout> tilList = new ArrayList<>();

        final TextView tv1, tv2, tv3;
        tv1 = findViewById(R.id.tv1);
        tv2 = findViewById(R.id.tv2);
        tv3 = findViewById(R.id.tv3);


        LayoutInflater inflater = getLayoutInflater();
        LinearLayout rootLayout = findViewById(R.id.test_rootlayout);
        final TextInputLayout view1 = (TextInputLayout) inflater.inflate(R.layout.et_test, rootLayout, false);
        final TextInputLayout view2 = (TextInputLayout) inflater.inflate(R.layout.et_test, rootLayout, false);
        tilList.add(view1);
        tilList.add(view2);


        // add views
        rootLayout.addView(view1);
        rootLayout.addView(view2);


        // listener in list of tils

        for (TextInputLayout til : tilList) {
            til.getEditText().addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (s.length() > 2) tv1.setText("YAAAAAY");
                    else tv1.setText("jijijijj");
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
        }

        tv2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // get text
                String string1 = view1.getEditText().getText().toString();
                String string2 = view2.getEditText().getText().toString();


                // display text
                tv3.setText(string1 + string2);

            }
        });

    }

}
