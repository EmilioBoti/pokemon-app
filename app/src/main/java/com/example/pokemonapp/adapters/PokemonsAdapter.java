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
import com.example.pokemonapp.adapters.viewHolder.PokeViewHolder;
import com.example.pokemonapp.models.Pokemon;
import com.example.pokemonapp.services.Services;
import com.example.pokemonapp.utils.common.OnClickItemListener;
import com.example.pokemonapp.utils.constants.Constants;
import com.example.pokemonapp.utils.constants.Helpers;

import java.util.ArrayList;
import java.util.List;

public class PokemonsAdapter extends RecyclerView.Adapter<PokeViewHolder> {
    private LayoutInflater inflater;
    private Context context;
    private ArrayList<Pokemon> list;
    private OnClickItemListener listener;

    public interface OnClickPokeListener{
        void onClickPoke(int pos);
    }

    public PokemonsAdapter(Context context, ArrayList<Pokemon> list){
        this.inflater = LayoutInflater.from(context);
        this.context = context;
        this.list = list;
    }

    public void OnClickPokeListener(OnClickItemListener listener){
        this.listener = listener;
    }

    @NonNull
    @Override
    public PokeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.poke_cardview, null);
        return new PokeViewHolder(view, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull PokeViewHolder holder, int position) {
        holder.bindData(list.get(position));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

}
