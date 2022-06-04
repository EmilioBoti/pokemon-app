package com.example.pokemonapp.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.palette.graphics.Palette;
import androidx.palette.graphics.Target;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pokemonapp.R;
import com.example.pokemonapp.models.Pokemon;
import com.example.pokemonapp.services.Services;
import com.example.pokemonapp.utils.constants.Constants;
import com.example.pokemonapp.utils.constants.Helpers;

import java.util.ArrayList;
import java.util.List;

public class PokemonsAdapter extends RecyclerView.Adapter<PokemonsAdapter.ViewHolder> {
    private LayoutInflater inflater;
    private Context context;
    private ArrayList<Pokemon> list;
    private OnClickPokeListener listener;

    public interface OnClickPokeListener{
        void onClickPoke(int pos);
    }

    public PokemonsAdapter(Context context, ArrayList<Pokemon> list){
        this.inflater = LayoutInflater.from(context);
        this.context = context;
        this.list = list;
    }

    public void OnClickPokeListener(OnClickPokeListener listener){
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.poke_cardview, null);
        return new PokemonsAdapter.ViewHolder(view, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bindData(list.get(position));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        public LinearLayout typesContainer;
        public TextView namePoke, numPoke;
        public ImageView imgPoke;
        public RelativeLayout nameContainer;
        private CardView cardView;

        public ViewHolder(View itemView, OnClickPokeListener listener){
            super(itemView);
            namePoke = itemView.findViewById(R.id.namePoke);
            imgPoke = itemView.findViewById(R.id.imgPoke);
            nameContainer = itemView.findViewById(R.id.datas);
            numPoke = itemView.findViewById(R.id.numPoke);
            cardView = itemView.findViewById(R.id.card);
        }

        public void bindData(Pokemon pokemon) {
            Helpers.loadFitImage(Constants.URL_IMG+pokemon.getId()+".png", imgPoke);

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

            namePoke.setText(pokemon.getName().replaceAll("-", " "));
            numPoke.setText("No. "+pokemon.getId());

            imgPoke.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(listener != null && getAbsoluteAdapterPosition() != RecyclerView.NO_POSITION) {
                        listener.onClickPoke(pokemon.getId());
                    }
                }
            });
        }
    }
}
