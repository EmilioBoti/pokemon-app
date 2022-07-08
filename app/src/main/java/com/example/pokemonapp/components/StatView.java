package com.example.pokemonapp.components;

import android.content.Context;
import android.content.res.ColorStateList;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.example.pokemonapp.R;

import java.util.ArrayList;
import java.util.HashMap;

public class StatView extends LinearLayout {
    private TextView statTitle;
    private TextView statValue;
    private SeekBar bar;

    public StatView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.stat_view, this, true);

        statTitle = findViewById(R.id.statTitle);
        statValue = findViewById(R.id.statValue);
        bar = findViewById(R.id.progressBar);

    }

    public void setContentStat(String key, int value, int color) {
        statTitle.setText(key + ":");
        statValue.setText(String.valueOf(value));
        bar.setProgress(value);
        bar.setProgressTintList(ColorStateList.valueOf(color));
    }
}
