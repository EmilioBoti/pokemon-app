package com.example.pokemonapp.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;

import com.example.pokemonapp.R;

public class PokemonFragment extends Fragment {
    private ImageView imageView;
    private ImageButton goBack;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle saveInstanceState){
        View view = inflater.inflate(R.layout.fragment_pokemon, container, false);

        imageView = view.findViewById(R.id.image);
        goBack = view.findViewById(R.id.goBack);
        //imageView.setImageResource(R.mipmap.ic_launcher_raichu_round);
        return view;
    }
}
