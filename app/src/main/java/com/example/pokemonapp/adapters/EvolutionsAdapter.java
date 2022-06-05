package com.example.pokemonapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.pokemonapp.R;
import com.example.pokemonapp.adapters.viewHolder.EvolutionViewHolder;
import com.example.pokemonapp.models.Pokemon;
import com.example.pokemonapp.utils.common.OnClickItemListener;
import java.util.ArrayList;

public class EvolutionsAdapter extends RecyclerView.Adapter<EvolutionViewHolder> {
    private LayoutInflater inflater;
    private ArrayList<Pokemon> listPokemons;
    private Context context;
    private OnClickItemListener listener;

    public EvolutionsAdapter(Context context, ArrayList<Pokemon> listPokemons){
        this.context = context;
        this.inflater = LayoutInflater.from(this.context);
        this.listPokemons = listPokemons;
    }

    @NonNull
    @Override
    public EvolutionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.evolution_card, null);
        return new EvolutionViewHolder(view, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull EvolutionViewHolder holder, int position) {
        holder.bindData(listPokemons.get(position));
    }

    @Override
    public int getItemCount() {
        return listPokemons.size();
    }

    public void onClickItemListener(OnClickItemListener listener){
        this.listener = listener;
    }
}
