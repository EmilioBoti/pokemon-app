package com.example.pokemonapp.adapters.viewHolder;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.palette.graphics.Palette;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pokemonapp.R;
import com.example.pokemonapp.adapters.PokemonsAdapter;
import com.example.pokemonapp.models.Pokemon;
import com.example.pokemonapp.utils.common.OnClickItemListener;
import com.example.pokemonapp.utils.constants.Constants;
import com.example.pokemonapp.utils.constants.Helpers;

public class PokeViewHolder extends RecyclerView.ViewHolder {
    public TextView namePoke, numPoke;
    public ImageView imgPoke;
    public RelativeLayout nameContainer;
    private CardView cardView;
    private OnClickItemListener listener;

    public PokeViewHolder(View itemView, OnClickItemListener listener){
        super(itemView);
        this.listener = listener;
        namePoke = itemView.findViewById(R.id.namePoke);
        imgPoke = itemView.findViewById(R.id.imgPoke);
        nameContainer = itemView.findViewById(R.id.datas);
        numPoke = itemView.findViewById(R.id.numPoke);
        cardView = itemView.findViewById(R.id.card);
    }

    public void bindData(Pokemon pokemon) {
        Helpers.loadFitImage(Constants.URL_IMG+pokemon.getId()+".png", imgPoke);
        settingColor();

        namePoke.setText(pokemon.getName().replaceAll("-", " "));
        numPoke.setText("No. "+pokemon.getId());

        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(listener != null && getAbsoluteAdapterPosition() != RecyclerView.NO_POSITION) {
                    listener.onClickItem(pokemon.getId());
                }
            }
        });
    }

    private void settingColor() {
        Bitmap bitmap = ((BitmapDrawable) imgPoke.getDrawable()).getBitmap();

        Palette.from(bitmap).generate(new Palette.PaletteAsyncListener() {
            @Override
            public void onGenerated(@Nullable Palette palette) {
                Palette.Swatch vibrant = palette.getLightVibrantSwatch();
                if (vibrant != null){
                    cardView.setCardBackgroundColor(vibrant.getRgb());
                }
            }
        });
    }
}