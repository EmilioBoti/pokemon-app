package com.example.pokemonapp.components;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pokemonapp.R;
import com.example.pokemonapp.adapters.EvolutionsAdapter;
import com.example.pokemonapp.businessLogic.pokeDetails.IDetail;
import com.example.pokemonapp.models.Pokemon;
import com.example.pokemonapp.utils.common.OnClickItemListener;

import java.util.ArrayList;

public class EvolutionContainer extends LinearLayout implements OnClickItemListener {
    private TextView title;
    private RecyclerView container;
    private ArrayList<Pokemon> list = null;
    private IDetail.Presenter presenter;

    public EvolutionContainer(Context context) {
        super(context);
    }

    public EvolutionContainer(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        TypedArray arr = context.obtainStyledAttributes(attrs, R.styleable.evolutionAtrrs, 0, 0);
        String titleText = arr.getString(R.styleable.evolutionAtrrs_titleText);

        this.setGravity(Gravity.CENTER_HORIZONTAL);

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.evolutioin_container, this, true);

        title = findViewById(R.id.titleContainer);
        container = findViewById(R.id.evolutions);
        title.setText(titleText);
    }

    public void setListener(IDetail.Presenter presenter) {
        this.presenter = presenter;
    }

    public void setAdapter(ArrayList<Pokemon> list) {
        if (list.isEmpty()){
            this.setVisibility(View.GONE);
        }else {
            EvolutionsAdapter evolutionsAdapter = new EvolutionsAdapter(this.getContext(), list);
            container.setHasFixedSize(true);
            container.setLayoutManager(new LinearLayoutManager(this.getContext(), LinearLayoutManager.HORIZONTAL, false));
            container.setAdapter(evolutionsAdapter);
            evolutionsAdapter.onClickItemListener(this);
        }
    }

    @Override
    public void onClickItem(int post) {
        presenter.clickPoke(post);
    }

    @Override
    public void onClickItem(String name) {

    }
}
