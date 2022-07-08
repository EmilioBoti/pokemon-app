package com.example.pokemonapp.components;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

import com.example.pokemonapp.R;

import java.util.ArrayList;
import java.util.HashMap;

public class StatContainer extends LinearLayout {
    private LinearLayout statsContainer;
    private ArrayList<HashMap<String, Integer>> statList;
    private ArrayList<String> keyList;
    public final String HP = "hp";
    public final String ATTACK = "attack";
    public final String DEFENSE = "defense";
    public final String SPECIAL_ATTACK = "special-attack";
    public final String SPECIAL_DEFENSE = "special-defense";
    public final String SPEEN = "speed";
    private int color;

    public StatContainer(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.stat_container, this, true);

        statsContainer = findViewById(R.id.statsContainer);
        keyList = new ArrayList<>();
        keyList.add(HP);
        keyList.add(ATTACK);
        keyList.add(DEFENSE);
        keyList.add(SPECIAL_ATTACK);
        keyList.add(SPECIAL_DEFENSE);
        keyList.add(SPEEN);

    }

    public void setStatList(ArrayList<HashMap<String, Integer>> statList, @Nullable Integer color) {
        this.statList = statList;
        this.color = color;
        addStatView();
    }

    private void addStatView() {
        if (this.statList != null) {
            statsContainer.removeAllViews();
            for (int i = 0; i < this.statList.size(); i++) {
                StatView stat = new StatView(getContext(), null);
                stat.setContentStat(keyList.get(i), this.statList.get(i).get(keyList.get(i)), color);
                statsContainer.addView(stat);
            }
        }
    }

}
