package com.example.pokemonapp.view.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.pokemonapp.R;
import com.example.pokemonapp.models.Move;
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

public class MoveDetailFragment extends Fragment implements Callback,Runnable {
    private String id;
    private Move move;
    private TextView titleBar;
    private ImageButton btnGoBack;
    //private Contact.MainPresenter presenter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle saveInstanceState){
        return inflater.inflate(R.layout.move_detail_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle saveInstanceState){
        super.onViewCreated(view, saveInstanceState);
        //set all views
        init(view);
        titleBar.setText(R.string.moveInfo);

        id = getArguments().getString("id");
        int idM = Integer.parseInt(id) + 1;

        getMove(String.valueOf(idM));

        btnGoBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });
    }
    private void init(View view){

        titleBar = view.findViewById(R.id.titleBar);
        btnGoBack = view.findViewById(R.id.goBack);
        move = new Move();
    }
    public void getMove(String id) {
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(Constants.MOVE_URL+id)
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
                move.setAccuracy(Integer.parseInt(json.getString("accuracy")));
                move.setType(json.getJSONObject("type").getString("name"));
                move.setPower(Integer.parseInt(json.getString("power")));
                move.setPowerPoint(Integer.parseInt(json.getString("pp")));

                MoveDetailFragment.this.getActivity().runOnUiThread(this);

            } catch (JSONException | IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void run() {

    }
}
