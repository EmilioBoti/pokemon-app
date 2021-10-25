package com.example.pokemonapp.utils.constants;

import android.widget.ImageView;

import com.squareup.picasso.Picasso;

public class Helpers {

    public static void loadImage(String url, ImageView pokeImage){
        Picasso.get()
                .load(url)
                //.placeholder(R.mipmap.ic_launcher_defaultimg_foreground)
                .into(pokeImage);
    }
}
