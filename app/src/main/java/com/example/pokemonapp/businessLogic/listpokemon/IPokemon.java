package com.example.pokemonapp.businessLogic.listpokemon;

import com.example.pokemonapp.models.Pokemon;

import java.util.ArrayList;

import okhttp3.Call;

public interface IPokemon {
    interface ViewPresenter {
        void showPokemons(ArrayList<Pokemon> list);
    }
    interface Presenter {
        void requestAllPoke(int count);
    }
    interface ModelPresenter {
        Call getAllPokemons(int count);
    }
}
