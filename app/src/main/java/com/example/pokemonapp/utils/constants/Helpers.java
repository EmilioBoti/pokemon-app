package com.example.pokemonapp.utils.constants;

import android.content.Context;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.pokemonapp.MainActivity;
import com.example.pokemonapp.R;
import com.example.pokemonapp.fragments.PokemonFragment;
import com.squareup.picasso.Picasso;

public class Helpers {

    public static void loadImage(String url, ImageView pokeImage){
        Picasso.get()
                .load(url)
                .placeholder(R.mipmap.ic_launcher_pokemon_zero_foreground)
                .into(pokeImage);
    }

    public static void callFragment(FragmentManager fragmentManager, Fragment fragment){
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.replace(R.id.fragment_container_view, fragment);
        fragmentTransaction.commit();
    }
}
