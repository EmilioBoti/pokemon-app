package com.example.pokemonapp.businessLogic.pokeDetails;

import com.example.pokemonapp.models.Pokemon;
import com.example.pokemonapp.utils.common.OnClickItemListener;

import java.util.ArrayList;

import okhttp3.Call;

public interface IDetail {
    interface ViewPresenter extends OnClickItemListener {
        void setViewData(Pokemon pokemon);
        void setEvolutions(ArrayList<Pokemon> list);
        void error(String error);
    }
    interface Presenter {
        void callPoke(String id);
        void pokeEvolutions(String id);
        void clickPoke(int id);
    }
    interface ModelPresenter {
        Call getPokemon(String id);
        Call getUrlEvolution(String id);
        Call getEvolutions(String id);
    }
}
