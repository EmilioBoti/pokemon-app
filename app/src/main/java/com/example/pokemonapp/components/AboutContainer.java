package com.example.pokemonapp.components;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.example.pokemonapp.R;

public class AboutContainer extends LinearLayout {
    private TextView about;

    public AboutContainer(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.about_container, this, true);

        about = findViewById(R.id.pokeDescription);

    }

    public void setAboutPoke(String aboutPoke) {
        if (about != null) {
            about.setText(aboutPoke);
        }
    }

}
