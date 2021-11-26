package com.example.pokemonapp.view.fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pokemonapp.R;
import com.example.pokemonapp.adapters.MoveAdapter;
import com.example.pokemonapp.services.Services;
import com.example.pokemonapp.utils.constants.Constants;
import com.example.pokemonapp.utils.constants.Helpers;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MoveFragment extends Fragment implements Callback {
    private Bundle data;
    private Context context;
    public ArrayList<String> listMove;
    private RecyclerView containerItemMove;
    private TextView titleMove;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_move, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle saveInstanceState){
        super.onViewCreated(view, saveInstanceState);
        context = getContext();
        data = getArguments();

        containerItemMove = view.findViewById(R.id.containerItemMove);

        String id = data.getString("idMove");
        OkHttpClient client = new OkHttpClient();

        try {
            Request request = Services.getDatas(Constants.CATEGORY_MOVE+id);
            client.newCall(request).enqueue(this);

        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onFailure(@NonNull Call call, @NonNull IOException e) {

    }

    @Override
    public void onResponse(@NonNull Call call, @NonNull Response response) {
        if(response.isSuccessful()){
            ArrayList<String> list = new ArrayList<>();
            try {
                String bodyJson = response.body().string();
                JSONObject json = new JSONObject(bodyJson);
                JSONArray moves = json.getJSONArray("moves");

                for (int i = 0; i < moves.length(); i++){
                    JSONObject obj = (JSONObject)moves.get(i);
                    list.add(obj.getString("name"));
                }

                MoveFragment.this.getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        initAdapter(list);
                    }
                });

            } catch (IOException | JSONException  e) {
                e.printStackTrace();
            }
        }
    }

    private void initAdapter(ArrayList<String> listMove) {

        MoveAdapter moveAdapter = new MoveAdapter(context, listMove);
        containerItemMove.setHasFixedSize(true);
        containerItemMove.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
        containerItemMove.setAdapter(moveAdapter);

        moveAdapter.OnClickItemMove(new MoveAdapter.OnClickItemMove() {
            @Override
            public void onClickMove(int pos) {
                /*Toast.makeText(context, "pos: " + pos, Toast.LENGTH_SHORT).show();
                Log.d("move", listMove.toString());*/
                Bundle data = new Bundle();
                data.putString("id", String.valueOf(pos));
                MoveDetailFragment moveDetailFragment = new MoveDetailFragment();
                moveDetailFragment.setArguments(data);
                Helpers.callFragment(getParentFragmentManager(), moveDetailFragment);
            }
        });
    }
}