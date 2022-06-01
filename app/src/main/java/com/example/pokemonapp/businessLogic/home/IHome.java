package com.example.pokemonapp.businessLogic.home;

import okhttp3.Call;

public interface IHome {
    interface ViewPresenter {
        void detailsPoke(String input);
        void error(String error);
    }
    interface HomePresenter {
        void requestPoke(String input);
    }
    interface ModelPresenter {
        Call getpokemon(String input);
    }
}
