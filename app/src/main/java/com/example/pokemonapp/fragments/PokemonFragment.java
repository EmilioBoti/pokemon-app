package com.example.pokemonapp.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.pokemonapp.R;
import com.example.pokemonapp.services.Services;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class PokemonFragment extends Fragment {
    private ImageView imageView;
    private ImageButton goBack;
    private TextView namePoke;
    private LinearLayout linearLayout, expPoke;
    public String name;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle saveInstanceState){
        View view = inflater.inflate(R.layout.fragment_pokemon, container, false);

        linearLayout = view.findViewById(R.id.typePokemon);
        expPoke = view.findViewById(R.id.expPoke);

        namePoke = view.findViewById(R.id.namePoke);
        imageView = view.findViewById(R.id.image);
        goBack = view.findViewById(R.id.goBack);

        OkHttpClient client = new OkHttpClient();
        Services services = new Services();
        
        TextView t = new TextView(getContext());
        t.setText("Electric");

        //event to go back
        goBack.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view1){
                getActivity().onBackPressed();
            }
        });

        return view;
    }
}
