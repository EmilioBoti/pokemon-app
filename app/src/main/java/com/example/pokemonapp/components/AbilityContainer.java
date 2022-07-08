package com.example.pokemonapp.components;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

import com.example.pokemonapp.R;

import java.util.ArrayList;

public class AbilityContainer extends LinearLayout {
    private LinearLayout abilityContainer;
    private ArrayList<String> abilityList;
    private int color;

    public AbilityContainer(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.ability_container, this, true);

        abilityContainer = findViewById(R.id.abilityContainer);

    }

    public void setAbilityList(ArrayList<String> abilityList, @Nullable Integer color) {
        this.abilityList = abilityList;
        this.color = color;
        addAbilityView();
    }

    private void addAbilityView() {
        if (this.abilityContainer != null) {
            abilityContainer.removeAllViews();
            for (String name: this.abilityList) {
                AbilityView abilityView = new AbilityView(getContext(), null);
                abilityView.setViewAbility(name, this.color);
                abilityContainer.addView(abilityView);
            }
        }
    }
}
