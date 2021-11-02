package com.example.pokemonapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.pokemonapp.R;

import java.util.ArrayList;

public class CustomSpinnerAdapter extends BaseAdapter {
    private ArrayList<String> list;
    private Context context;
    private LayoutInflater inflater;

    public CustomSpinnerAdapter(Context context, ArrayList<String> list){
        this.context = context;
        this.list = list;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return this.list.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = inflater.inflate(R.layout.item_spinner, null);
        TextView name = view.findViewById(R.id.category);
        name.setText(list.get(position));
        return view;
    }
}
