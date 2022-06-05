package com.example.pokemonapp.components;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import com.example.pokemonapp.R;

import java.util.ArrayList;

public class Property extends LinearLayout {
    private TypedArray attributes;
    private TextView title;
    private LinearLayout container;
    private final Float DIMEN = 16f;

    public Property(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        attributes = context.obtainStyledAttributes(attrs, R.styleable.Property, 0,0);

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.property_layout, this, true);

        String t = attributes.getString(R.styleable.Property_title);
        int space = (int) attributes.getDimension(R.styleable.Property_spaceHorizontal, 0);

        this.setPadding(space, 0, space, 0);

        title = findViewById(R.id.titleProperty);
        container = findViewById(R.id.containerPro);
        title.setText(t);
    }

    public void setViewContent(String value) {
        container.removeAllViewsInLayout();
        container.addView(createTextView(value, DIMEN));
    }

    public void setElements(ArrayList<String> type) {
        container.removeAllViewsInLayout();
        for (String elem : type) container.addView(createTextView(elem, DIMEN));
    }

    private TextView createTextView(String text, Float dimen) {
        TextView textView = new TextView(getContext());
        textView.setGravity(Gravity.CENTER_HORIZONTAL);
        textView.setTextSize(dimen);
        textView.setText(text);
        return textView;
    }
}
