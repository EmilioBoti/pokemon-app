package com.example.pokemonapp.view.ui.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pokemonapp.R;
import com.example.pokemonapp.adapters.PokemonsAdapter;
import com.example.pokemonapp.businessLogic.listpokemon.IPokemon;
import com.example.pokemonapp.businessLogic.listpokemon.PokePresenter;
import com.example.pokemonapp.businessLogic.listpokemon.PokesProvider;
import com.example.pokemonapp.models.Pokemon;
import com.example.pokemonapp.utils.constants.Helpers;

import java.util.ArrayList;

public class PokemonAllFragment extends Fragment implements IPokemon.ViewPresenter, PokemonsAdapter.OnClickPokeListener {
    private RecyclerView listContainer;
    private Context context;
    private ProgressBar progressBar;
    private PokesProvider model;
    private PokePresenter presenter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle saveInstanceState){
        return inflater.inflate(R.layout.fragment_allpokemons, container, false);
    }
    @Override
    public void onViewCreated(@NonNull View view, Bundle saveInstanceState){
        super.onViewCreated(view, saveInstanceState);

        context = getContext();
        //get views
        init(view);
    }

    private void init(View view){
        listContainer = view.findViewById(R.id.pokemonContainer);
        progressBar = view.findViewById(R.id.loader);
        
        model = new PokesProvider();
        presenter = new PokePresenter(this, model);
        presenter.requestAllPoke(1118);
    }

    private void callAdapter(ArrayList<Pokemon> list) {
        //set adapter
        PokemonsAdapter pokemonsAdapter = new PokemonsAdapter(context, list);
        listContainer.setHasFixedSize(true);
        listContainer.setLayoutManager(new GridLayoutManager(context, 2, GridLayoutManager.VERTICAL, false));
        listContainer.setAdapter(pokemonsAdapter);

        //event click on pokemon
        pokemonsAdapter.OnClickPokeListener(this);
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void onClickPoke(int pos) {
        Bundle data = new Bundle();
        data.putString("idPoke", String.valueOf(pos));

        PokemonFragment pokemonFragment = new PokemonFragment();
        pokemonFragment.setArguments(data);
        Helpers.callFragment(getParentFragmentManager(),pokemonFragment);
    }

    @Override
    public void showPokemons(ArrayList<Pokemon> list) {
        PokemonAllFragment.this.getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                callAdapter(list);
            }
        });
    }
}