package com.example.pokemonapp.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.pokemonapp.R;

public class PokemonFragment extends Fragment {
    private ImageView imageView;
    private ImageButton goBack;
    private LinearLayout linearLayout, expPoke;
    public String name;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle saveInstanceState){
        View view = inflater.inflate(R.layout.fragment_pokemon, container, false);

        linearLayout = view.findViewById(R.id.typePokemon);
        expPoke = view.findViewById(R.id.expPoke);

        imageView = view.findViewById(R.id.image);
        goBack = view.findViewById(R.id.goBack);

        TextView t = new TextView(getContext());
        t.setText("Electric");

        TextView b = new TextView(getContext());
        b.setText("173");
        b.setTextAppearance(R.style.itemStyle);

        //event to go back
        goBack.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view1){
                getActivity().onBackPressed();
            }
        });

        linearLayout.addView(t);
        expPoke.addView(b);
        //imageView.setImageResource(R.mipmap.ic_launcher_raichu_round);
        return view;
    }
}
