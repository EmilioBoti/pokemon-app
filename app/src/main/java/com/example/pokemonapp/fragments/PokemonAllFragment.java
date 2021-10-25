package com.example.pokemonapp.fragments;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pokemonapp.R;
import com.example.pokemonapp.adapters.PokemonsAdapter;
import com.example.pokemonapp.models.Pokemon;
import com.example.pokemonapp.services.SearchDataThread;
import com.example.pokemonapp.services.Services;
import com.example.pokemonapp.utils.constants.Constants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class PokemonAllFragment extends Fragment {
    private RecyclerView listContainer;
    private ArrayList<String> list;
    private Context context;
    private ArrayList<Pokemon> listPokemon;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle saveInstanceState){
        View view = inflater.inflate(R.layout.fragment_allpokemons, container, false);
        return  view;
    }
    @Override
    public void onViewCreated(View view, Bundle saveInstanceState){
        super.onViewCreated(view, saveInstanceState);

        context = getContext();
        //get views
        getViewer(view);

        
    }

    private void getViewer(View view){
        listContainer = view.findViewById(R.id.pokemonContainer);
        list = new ArrayList<>();
        listPokemon = new ArrayList<>();
    }
    private void callAdapter(ArrayList<Pokemon> list){
        //set adapter
        PokemonsAdapter pokemonsAdapter = new PokemonsAdapter(context, list);
        listContainer.setHasFixedSize(true);
        listContainer.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
        listContainer.setAdapter(pokemonsAdapter);
    }


}
