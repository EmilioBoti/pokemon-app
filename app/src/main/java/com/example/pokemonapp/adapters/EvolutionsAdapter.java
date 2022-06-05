package com.example.pokemonapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pokemonapp.R;
import com.example.pokemonapp.models.Pokemon;
import com.example.pokemonapp.utils.common.OnClickItemListener;
import com.example.pokemonapp.utils.constants.Constants;
import com.example.pokemonapp.utils.constants.Helpers;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class    EvolutionsAdapter extends RecyclerView.Adapter<EvolutionsAdapter.ViewHolder> {
    private LayoutInflater inflater;
    private ArrayList<Pokemon> listPokemons;
    private Context context;
    private OnClickItemListener listener;

    public EvolutionsAdapter(Context context, ArrayList<Pokemon> listPokemons){
        this.inflater = LayoutInflater.from(context);
        this.context = context;
        this.listPokemons = listPokemons;
    }

    @NonNull
    @Override
    public EvolutionsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.evolution_card, null);
        return new EvolutionsAdapter.ViewHolder(view, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull EvolutionsAdapter.ViewHolder holder, int position) {
        holder.bindData(listPokemons.get(position));
    }

    @Override
    public int getItemCount() {
        return listPokemons.size();
    }

    public void onClickItemListener(OnClickItemListener listener){
        this.listener = listener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView pokeImage;
        private TextView pokeName, idPoke;

        public ViewHolder(View itemview, OnClickItemListener listener){
            super(itemview);
            pokeImage = itemview.findViewById(R.id.pokeImg);
            pokeName = itemview.findViewById(R.id.nameP);
            idPoke = itemview.findViewById(R.id.numPoke);
        }

        public void bindData(Pokemon pokemon){
            //pokeImage
            Helpers.loadImage(Constants.URL_IMG+pokemon.getId()+".png", pokeImage);
            pokeName.setText(pokemon.getName().replaceAll("-", " "));
            idPoke.setText("No. "+pokemon.getId());
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(listener != null && getAbsoluteAdapterPosition() != RecyclerView.NO_POSITION){
                        listener.onclickItem(pokemon.getId());
                    }
                }
            });
        }
    }
}
