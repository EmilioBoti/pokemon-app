package com.example.pokemonapp.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pokemonapp.R;
import com.example.pokemonapp.models.Pokemon;
import com.example.pokemonapp.services.Services;
import com.example.pokemonapp.utils.constants.Constants;
import com.example.pokemonapp.utils.constants.Helpers;

import java.util.ArrayList;

public class PokemonsAdapter extends RecyclerView.Adapter<PokemonsAdapter.ViewHolder> {
    private LayoutInflater inflater;
    private Context context;
    private ArrayList<Pokemon> list;

    public PokemonsAdapter(Context context, ArrayList<Pokemon> list){
        this.inflater = LayoutInflater.from(context);
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.poke_cardview, null);

        return new PokemonsAdapter.ViewHolder(view);
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
        public TextView namePoke;
        public ImageView imgPoke;
        public RelativeLayout nameContainer;

        public ViewHolder(View itemView){
            super(itemView);
            //typesContainer = itemView.findViewById(R.id.typesContainer);
            namePoke = itemView.findViewById(R.id.namePoke);
            imgPoke = itemView.findViewById(R.id.imgPoke);
            nameContainer = itemView.findViewById(R.id.datas);
        }

        public void bindData(Pokemon pokemon) {
            nameContainer.setBackgroundColor(Color.parseColor("gray"));
            namePoke.setText(pokemon.getName());
            Helpers.loadImage(Constants.URL_IMG+pokemon.getId()+".png", imgPoke);
        }
    }
}
