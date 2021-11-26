package com.example.pokemonapp.presenter.interfaces;

import com.example.pokemonapp.models.Move;

public interface Contact {

    interface InteractorModel{
        void getMove(String id);
    }
    interface MainPresenter {
        void navigateTo(String id);
        void waitForMove(Move move);
    }
    interface MainView {
        void navigateTo(Move move);
    }
}
