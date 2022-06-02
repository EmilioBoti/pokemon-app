package com.example.pokemonapp.businessLogic.pokeDetails;

import com.example.pokemonapp.models.Pokemon;

import java.util.ArrayList;

import okhttp3.Call;

public interface IDetail {
    interface ViewPresenter {
        void setViewData(Pokemon pokemon);
        void setEvolutions(ArrayList<Pokemon> list);
    }
    interface Presenter {
        void callPoke(String id);
        void pokeEvolutions(String id);
    }
    interface ModelPresenter {
        Call getPokemon(String id);
        Call getUrlEvolution(String id);
        Call getEvolutions(String id);
    }
}
