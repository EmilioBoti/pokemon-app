package com.example.pokemonapp.components;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

public class EvolutionContainer extends LinearLayout {
    public EvolutionContainer(Context context) {
        super(context);
    }

    public EvolutionContainer(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public EvolutionContainer(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public EvolutionContainer(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

}
