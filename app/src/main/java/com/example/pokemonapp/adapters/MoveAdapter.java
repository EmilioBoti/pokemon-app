package com.example.pokemonapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pokemonapp.R;

import java.util.ArrayList;

public class MoveAdapter extends RecyclerView.Adapter<MoveAdapter.ViewHolder> {
    public LayoutInflater inflater;
    public Context context;
    public ArrayList<String> listMove;


    public MoveAdapter(Context context, ArrayList<String> listMove){
        this.inflater = LayoutInflater.from(context);
        this.listMove = listMove;
        this.context = context;
    }

    @NonNull
    @Override
    public MoveAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.move_card, null);
        return new MoveAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MoveAdapter.ViewHolder holder, int position) {
        holder.bindData(listMove.get(position));
    }

    @Override
    public int getItemCount() {
        return listMove.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView moveName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            moveName = itemView.findViewById(R.id.moveName);
        }

        public void bindData(String s) {
            moveName.setText(s);
        }
    }
}
