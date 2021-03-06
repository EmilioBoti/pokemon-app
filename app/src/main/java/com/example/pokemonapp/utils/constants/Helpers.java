package com.example.pokemonapp.utils.constants;

import android.widget.ImageView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.pokemonapp.R;
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
    public static String ToUpperName(String name){
        String na = "";

        for(int i = 0; i < name.length(); i++){
            if(i == 0){
                String f = String.valueOf(name.charAt(0)).toUpperCase();
                na += f;
            }else{
                na += String.valueOf(name.charAt(i));
            }
        }
        return na;
    }
}
