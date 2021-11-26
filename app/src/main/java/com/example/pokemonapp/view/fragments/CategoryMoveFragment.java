package com.example.pokemonapp.view.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.pokemonapp.R;
import com.example.pokemonapp.adapters.CustomSpinnerAdapter;
import com.example.pokemonapp.services.Services;
import com.example.pokemonapp.utils.constants.Constants;

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

public class CategoryMoveFragment extends Fragment implements AdapterView.OnItemSelectedListener, Callback {
    private Spinner spinner;
    private ArrayList<String> list = null;
    private Context context;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle saveInstanceState){
        return inflater.inflate(R.layout.category_move_fragment, container, false);
    }


    @Override
    public void onViewCreated(View view, Bundle saveInstanceState){
        super.onViewCreated(view, saveInstanceState);
        context = getContext();
        list = new ArrayList<>();

        spinner = view.findViewById(R.id.dropDown);
        spinner.setOnItemSelectedListener(this);

        fillList();

    }

    private void fillList(){
        OkHttpClient client = new OkHttpClient();

        try {
            Request request = Services.getDatas(Constants.CATEGORY_MOVE);
            client.newCall(request).enqueue(this);

        } catch (IOException  | JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onFailure(@NonNull Call call, @NonNull IOException e) {

    }

    @Override
    public void onResponse(@NonNull Call call, @NonNull Response response){
        if(response.isSuccessful()){
            try {
                String bodyjson = response.body().string();
                JSONObject json = new JSONObject(bodyjson);
                JSONArray results = json.getJSONArray("results");

                for (int i = 0; i < results.length(); i++){
                    JSONObject obj = (JSONObject)results.get(i);
                    list.add(obj.getString("name"));
                }
                CategoryMoveFragment.this.getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        initAdapter();
                    }
                });
            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        Bundle data = new Bundle();
        data.putString("idMove", String.valueOf(position));

        MoveFragment moveFragment = new MoveFragment();
        moveFragment.setArguments(data);

        FragmentManager fragmentManager = getParentFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.replace(R.id.moveContainer, moveFragment);
        fragmentTransaction.commit();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        
    }

    private void initAdapter(){
        CustomSpinnerAdapter customSpinnerAdapter = new CustomSpinnerAdapter(context, list);
        spinner.setAdapter(customSpinnerAdapter);
    }

}
