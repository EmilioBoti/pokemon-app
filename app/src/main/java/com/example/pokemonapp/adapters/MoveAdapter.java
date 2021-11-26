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
    public OnClickItemMove listener;

    public interface OnClickItemMove{
        void onClickMove(int pos);
    }

    public MoveAdapter(Context context, ArrayList<String> listMove){
        this.inflater = LayoutInflater.from(context);
        this.listMove = listMove;
        this.context = context;
    }
    public void OnClickItemMove(OnClickItemMove listener){
        this.listener = listener;
    }

    @NonNull
    @Override
    public MoveAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.move_card, null);
        return new MoveAdapter.ViewHolder(view, listener);
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

        public ViewHolder(@NonNull View itemView, OnClickItemMove listener) {
            super(itemView);
            moveName = itemView.findViewById(R.id.moveName);
        }

        public void bindData(String s) {
            moveName.setText(s);

            moveName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(listener != null && getAbsoluteAdapterPosition() != RecyclerView.NO_POSITION){
                        listener.onClickMove(getAbsoluteAdapterPosition());
                    }
                }
            });
        }
    }
}
