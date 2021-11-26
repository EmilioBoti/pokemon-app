package com.example.pokemonapp.presenter.interactor;

import android.util.Log;
import android.widget.Toast;

import com.example.pokemonapp.models.Move;
import com.example.pokemonapp.models.ReguestMoveData;
import com.example.pokemonapp.presenter.interfaces.Contact;

public class InteractorMoveImpl implements Contact.MainPresenter{
    private Contact.MainView view;
    private Contact.InteractorModel model;


    public InteractorMoveImpl(Contact.MainView view){
        this.view = view;
        this.model = new ReguestMoveData(this);
    }

    @Override
    public void navigateTo(String id) {
        if(view != null){
            model.getMove(id);
        }
    }

    @Override
    public void waitForMove(Move move) {
        if(view != null){
            view.navigateTo(move);
        }
    }
}
