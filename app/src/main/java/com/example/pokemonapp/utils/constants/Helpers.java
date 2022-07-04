package com.example.pokemonapp.utils.constants;

import android.content.Context;
import android.widget.ImageView;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.pokemonapp.R;
import com.squareup.picasso.Picasso;

public class Helpers {

    public static void loadImage(String url, ImageView pokeImage){
        Picasso.get()
                .load(url)
                //.placeholder(R.mipmap.ic_launcher_pokemon_zero_foreground)
                .into(pokeImage);
    }
    public static void loadFitImage(String url, ImageView pokeImage){
        Picasso.get().load(url).placeholder(R.mipmap.ic_launcher_pokemon_zero_foreground)
                .fit().centerCrop()
                .into(pokeImage);
    }

    public static void callFragment(FragmentManager fragmentManager, Fragment fragment){
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.replace(R.id.fragment_container_view, fragment);
        fragmentTransaction.commit();
    }
    public static String ToUpperName(String name) {
        String na = "";

        for(int i = 0; i < name.length(); i++) {
            if(i == 0) na += String.valueOf(name.charAt(0)).toUpperCase();
            else na += String.valueOf(name.charAt(i));
        }
        return na;
    }

    public static int getColorType(Context context, String type) {
        int color = 0;

        switch (type) {
            case "normal":
                color = ContextCompat.getColor(context, R.color.normal);
                break;
            case "fire":
                color = ContextCompat.getColor(context, R.color.fire);
                break;
            case "water":
                color = ContextCompat.getColor(context, R.color.water);
                break;
            case "grass":
                color = ContextCompat.getColor(context, R.color.grass);
                break;
            case "electric":
                color = ContextCompat.getColor(context, R.color.electric);
                break;
            case "ice":
                color = ContextCompat.getColor(context, R.color.ice);
                break;
            case "fighting":
                color = ContextCompat.getColor(context, R.color.figthing);
                break;
            case "poison":
                color = ContextCompat.getColor(context, R.color.poison);
                break;
            case "ground":
                color = ContextCompat.getColor(context, R.color.groung);
                break;
            case "flying":
                color = ContextCompat.getColor(context, R.color.flying);
                break;
            case "psychic":
                color = ContextCompat.getColor(context, R.color.psychic);
                break;
            case "bug":
                color = ContextCompat.getColor(context, R.color.bug);
                break;
            case "rock":
                color = ContextCompat.getColor(context, R.color.rock);
                break;
            case "ghost":
                color = ContextCompat.getColor(context, R.color.ghost);
                break;
            case "dark":
                color = ContextCompat.getColor(context, R.color.dark);
                break;
            case "dragon":
                color = ContextCompat.getColor(context, R.color.dragon);
                break;
            case "steel":
                color = ContextCompat.getColor(context, R.color.steel);
                break;
            case "fairy":
                color = ContextCompat.getColor(context, R.color.fairy);
                break;
            default:
                color = ContextCompat.getColor(context, R.color.gray_300);
                break;
        }
        return color;
    }
}
