package com.example.pokemonapp.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.SearchView;
import android.widget.Toast;

import com.example.pokemonapp.MainActivity;
import com.example.pokemonapp.R;

public class HomeFragment extends Fragment {
    private ImageButton btnSearch;
    public SearchView searchView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        searchView = view.findViewById(R.id.search);
        btnSearch = view.findViewById(R.id.btnSearch);


        btnSearch.setOnClickListener(new View.OnClickListener(){
           @Override
           public void onClick(View view){
               callFragment(searchView.getQuery().toString());
           }
        });

        return view;
    }
    private void callFragment(String idName){

        //save data of input
        Bundle dataInput = new Bundle();
        dataInput.putString("data", idName);

        FragmentManager fragmentManager = getParentFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        PokemonFragment pokemonFragment = new PokemonFragment();
        pokemonFragment.setArguments(dataInput); //send data to PokemonFragment

        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.replace(R.id.fragment_container_view, pokemonFragment, null);
        fragmentTransaction.commit();
    }
    @Override
    public void onDestroy(){
        super.onDestroy();
        getActivity().finish();
    }
}