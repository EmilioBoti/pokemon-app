package com.example.pokemonapp.components;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.example.pokemonapp.R;

public class AbilityView extends LinearLayout {
    private TextView abilityvalue;
    private View bottomBar;

    public AbilityView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.ability_view, this, true);

        abilityvalue = findViewById(R.id.abilityValue);
        bottomBar = findViewById(R.id.bgAbility);

    }

    public void setViewAbility(String name, int color) {
        abilityvalue.setText(name);
        bottomBar.setBackgroundColor(color);
    }
}
