package com.example.pokemonapp.models;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.pokemonapp.presenter.interactor.InteractorMoveImpl;
import com.example.pokemonapp.presenter.interfaces.Contact;
import com.example.pokemonapp.utils.constants.Constants;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ReguestMoveData implements Contact.InteractorModel,Callback {
    private Move move;
    private String url_move = "https://pokeapi.co/api/v2/move/";
    private InteractorMoveImpl presenter;

    public ReguestMoveData(InteractorMoveImpl presenter){
        this.presenter = presenter;
        this.move = new Move();
    }

    @Override
    public void getMove(String id) {
        /*move = new Move("pound","normal", 95, 100, 25, "this is a description");
        presenter.showResult(move);*/
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(this.url_move+id)
                .build();

        client.newCall(request).enqueue(this);

    }

    @Override
    public void onFailure(@NonNull Call call, @NonNull IOException e) {

    }

    @Override
    public void onResponse(@NonNull Call call, @NonNull Response response) {
        if(response.isSuccessful()){
            try {
                String bodyJson = response.body().string();
                JSONObject json = new JSONObject(bodyJson);
                move.setName(json.getString("name"));
                /*move.setAccuracy(Integer.parseInt(json.getString("accuracy")));
                move.setType(json.getJSONObject("type").getString("name"));
                move.setPower(Integer.parseInt(json.getString("power")));
                move.setPowerPoint(Integer.parseInt(json.getString("pp")));*/
                //presenter.waitForMove(move);
                presenter.waitForMove(move);
            } catch (JSONException | IOException e) {
                e.printStackTrace();
            }
        }
    }
}
