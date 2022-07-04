package com.example.pokemonapp.components;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.Space;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.example.pokemonapp.R;
import com.example.pokemonapp.utils.constants.Helpers;

import java.util.ArrayList;

public class TypeContainer extends LinearLayout {
    private TypedArray attr;
    private LinearLayout container;
    private ArrayList<String> typesList;

    public TypeContainer(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        //attr = context.obtainStyledAttributes(attrs, R.styleable.TypeContainer);

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.type_container_layout, this, true);

        container = findViewById(R.id.typesConatiner);

    }

    public void setTypesList(ArrayList<String> list) {
        this.typesList = list;
    }

    public void setViewType() {
        if (this.typesList != null) {
            container.removeAllViewsInLayout();
            for (int i = 0;  i < typesList.size(); i++) {
                container.addView(createTextView(typesList.get(i), 0f));
                if (i != typesList.size()-1) container.addView(setSpace());
            }
        }
    }

    private Space setSpace() {
        LinearLayout.LayoutParams margin = params();
        margin.height = 50;

        Space space = new Space(getContext());
        space.setLayoutParams(margin);
        return space;
    }
    private TextView createTextView(String type, Float dimen) {
        LinearLayout.LayoutParams width = params();
        width.width = 300;

        TextView textView = new TextView(getContext());
        textView.setGravity(Gravity.CENTER_HORIZONTAL);
        textView.setPadding(8,20,8,20);
        textView.setLayoutParams(width);
        textView.setText(Helpers.ToUpperName(type));
        textView.setTextColor(ContextCompat.getColor(getContext(), R.color.white));
        textView.setBackgroundResource(R.drawable.platform);
        textView.setBackgroundTintList(ColorStateList.valueOf(Helpers.getColorType(getContext(), type)));
        return textView;
    }

    private LinearLayout.LayoutParams params() {
        return new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
    }
}
