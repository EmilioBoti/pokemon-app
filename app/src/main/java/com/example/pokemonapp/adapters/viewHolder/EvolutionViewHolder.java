package com.example.pokemonapp.adapters.viewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.example.pokemonapp.R;
import com.example.pokemonapp.models.Pokemon;
import com.example.pokemonapp.utils.common.OnClickItemListener;
import com.example.pokemonapp.utils.constants.Constants;
import com.example.pokemonapp.utils.constants.Helpers;

public class EvolutionViewHolder extends RecyclerView.ViewHolder {
    private ImageView pokeImage;
    private TextView pokeName, idPoke;
    private OnClickItemListener listener;

    public EvolutionViewHolder(View itemview, OnClickItemListener listener) {
        super(itemview);
        pokeImage = itemview.findViewById(R.id.pokeImg);
        pokeName = itemview.findViewById(R.id.nameP);
        idPoke = itemview.findViewById(R.id.numPoke);
        this.listener = listener;
    }

    public void bindData(Pokemon pokemon) {
        Helpers.loadImage(Constants.URL_IMG+pokemon.getId()+".png", pokeImage);
        pokeName.setText(pokemon.getName().replaceAll("-", " "));
        idPoke.setText("No. "+pokemon.getId());

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(listener != null && getAbsoluteAdapterPosition() != RecyclerView.NO_POSITION){
                    listener.onClickItem(pokemon.getId());
                }
            }
        });
    }
}
