package com.example.pokemonapp.businessLogic.home;

import androidx.annotation.NonNull;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class HomePresenter implements IHome.HomePresenter {
    private IHome.ViewPresenter viewPresenter;
    private IHome.ModelPresenter modelPresenter;

    public HomePresenter(IHome.ViewPresenter viewPresenter, IHome.ModelPresenter modelPresenter) {
        this.viewPresenter = viewPresenter;
        this.modelPresenter = modelPresenter;
    }

    @Override
    public void requestPoke(String input) {
        Call call = modelPresenter.getpokemon(input);
        if (call != null) {
            call.enqueue(new Callback() {
                @Override
                public void onFailure(@NonNull Call call, @NonNull IOException e) {
                    viewPresenter.error("Failure.");
                }

                @Override
                public void onResponse(@NonNull Call call, @NonNull Response response) {
                    if (response.isSuccessful()) {
                        viewPresenter.detailsPoke(input);
                    } else {
                        viewPresenter.error("It migth not exist.");
                    }
                }
            });
        }
    }
}
