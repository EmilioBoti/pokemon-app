package com.example.pokemonapp.view.ui.fragments;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.os.Bundle;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pokemonapp.R;
import com.example.pokemonapp.adapters.EvolutionsAdapter;
import com.example.pokemonapp.businessLogic.pokeDetails.DetailPresenter;
import com.example.pokemonapp.businessLogic.pokeDetails.DetailProvider;
import com.example.pokemonapp.businessLogic.pokeDetails.IDetail;
import com.example.pokemonapp.models.Pokemon;
import com.example.pokemonapp.utils.constants.Helpers;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class PokemonFragment extends Fragment implements IDetail.ViewPresenter {
    private ImageView pokeImage;
    private ImageButton goBack;
    private TextView namePoke, pokeDescription, titleBar;
    private LinearLayout typesView, weight,height, baseExp, abilities, habitatLayout;
    private RecyclerView evolutions;
    private ProgressBar progressBar;
    private ScrollView scrollViewContainer;
    private GridLayout moveContainer;
    private DetailProvider model;
    private DetailPresenter presenter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle saveInstanceState){
        return inflater.inflate(R.layout.fragment_pokemon, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        init(view);
        titleBar.setText(R.string.pokeInfo);

        //get datas from home's input(searcher)
        Bundle data = getArguments();
        String id = data.get("idPoke").toString();
        presenter.callPoke(id);
        //event to go back
        goBack.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view1) {
                if (getActivity() != null) getActivity().onBackPressed();
            }
        });

    }

    private void init(View view){
        typesView = view.findViewById(R.id.typePokemon);
        abilities = view.findViewById(R.id.abilitiesPoke);
        weight = view.findViewById(R.id.weight);
        height = view.findViewById(R.id.height);
        baseExp = view.findViewById(R.id.expPoke);
        namePoke = view.findViewById(R.id.namePoke);
        pokeImage = view.findViewById(R.id.pokeImage);
        evolutions = view.findViewById(R.id.evolutions);
        goBack = view.findViewById(R.id.goBack);
        titleBar = view.findViewById(R.id.titleBar);
        pokeDescription = view.findViewById(R.id.pokeDescription);
        habitatLayout = view.findViewById(R.id.habitat);
        progressBar = view.findViewById(R.id.loader);
        scrollViewContainer = view.findViewById(R.id.scrollViewContainerr);
        model = new DetailProvider();
        presenter = new DetailPresenter(this, model);
    }

    private void callAdapter(Context context, ArrayList<Pokemon> listPokemon) {

        if (getActivity() != null){
            PokemonFragment.this.getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    evolutionAdapter(context, listPokemon);
                }
            });
        }
    }

    private void evolutionAdapter(Context context, ArrayList<Pokemon> listPokemon) {
        EvolutionsAdapter evolutionsAdapter = new EvolutionsAdapter(context, listPokemon);
        evolutions.setHasFixedSize(true);
        evolutions.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
        evolutions.setAdapter(evolutionsAdapter);

        evolutionsAdapter.OnClickItemListener(new EvolutionsAdapter.OnClickItemListener() {
            @Override
            public void onclickItem(int post) {
                scrollViewContainer.setVisibility(View.GONE);
                progressBar.setVisibility(View.VISIBLE);
                presenter.callPoke(String.valueOf(post));
            }

            @Override
            public void onclickItem(String name) {

            }
        });
    }

    @Override
    public void setViewData(Pokemon pokemon) {
        if (getActivity() != null) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    setContent(pokemon);
                }
            });
        }
    }

    @Override
    public void setEvolutions(ArrayList<Pokemon> list) {
        if (getActivity() != null) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    callAdapter(getContext(), list);
                }
            });
        }
    }

    private void setContent(Pokemon pokemon) {
        progressBar.setVisibility(View.GONE);
        scrollViewContainer.setVisibility(View.VISIBLE);

        final float DIMEN = 16f;
        TextView weightP, heightP,habitatText, baseExperiance, type, ability;
        pokeImage.setTranslationY(-1000f);
        //load the image
        Helpers.loadImage(pokemon.getSpriteFront(), pokeImage);
        //animation
        ObjectAnimator animatorImg = ObjectAnimator.ofFloat(pokeImage, "translationY", 0f);
        animatorImg.setDuration(700);
        animatorImg.start();

        weight.removeAllViewsInLayout();
        baseExp.removeAllViewsInLayout();
        typesView.removeAllViewsInLayout();
        abilities.removeAllViewsInLayout();
        habitatLayout.removeAllViewsInLayout();
        height.removeAllViewsInLayout();
        //moveContainer.removeAllViewsInLayout();

        double num = (pokemon.getWeight() / 4.54);
        String pound = String.format("%.02f", num);

        weightP = new TextView(getContext());
        weightP.setGravity(Gravity.CENTER_HORIZONTAL);
        weightP.setTextSize(DIMEN);
        weightP.setText(pound+" pounds");

        heightP = new TextView(getContext());
        heightP.setGravity(Gravity.CENTER_HORIZONTAL);
        heightP.setTextSize(DIMEN);
        heightP.setText((pokemon.getHeight() * 10) + " cm");

        habitatText = new TextView(getContext());
        habitatText.setGravity(Gravity.CENTER_HORIZONTAL);
        habitatText.setTextSize(DIMEN);
        habitatText.setText(pokemon.getHabitat());

        baseExperiance = new TextView(getContext());
        baseExperiance.setGravity(Gravity.CENTER_HORIZONTAL);
        baseExperiance.setTextSize(DIMEN);
        baseExperiance.setText(String.valueOf(pokemon.getBaseExperience()));
        String n = pokemon.getName().replaceAll("-", " ");

        namePoke.setText(Helpers.ToUpperName(n));

        habitatLayout.addView(habitatText);
        weight.addView(weightP);
        height.addView(heightP);
        baseExp.addView(baseExperiance);

        for (String types: pokemon.getTypes()) {
            type = new TextView(getContext());
            type.setText(types);
            type.setTextSize(DIMEN);
            type.setGravity(Gravity.CENTER_HORIZONTAL);
            typesView.addView(type);
        }
        for (String elem: pokemon.getAbilities()) {
            ability = new TextView(getContext());
            ability.setText(elem);
            ability.setTextSize(DIMEN);
            ability.setGravity(Gravity.CENTER_HORIZONTAL);
            abilities.addView(ability);
        }
    }
    private ArrayList<String> setElements(JSONArray array, String objName){
        ArrayList<String> listElements = new ArrayList<>();
        try {
            for (int i = 0; i < array.length(); i++){
                JSONObject object = (JSONObject) array.get(i);
                JSONObject type = object.getJSONObject(objName);
                listElements.add(type.getString("name"));
            }
        }catch (JSONException e){
            e.printStackTrace();
        }
        return listElements;
    }
}
