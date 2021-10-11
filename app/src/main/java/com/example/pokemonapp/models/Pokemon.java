package com.example.pokemonapp.models;

import java.util.ArrayList;

public class Pokemon {
    private int id;
    private String name;
    private String spriteBack, spriteFront;
    private ArrayList<String> types;
    private double weight;
    private double baseExperience;
    private ArrayList<String> abilities;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSpriteBack() {
        return spriteBack;
    }

    public void setSpriteBack(String spriteBack) {
        this.spriteBack = spriteBack;
    }

    public String getSpriteFront() {
        return spriteFront;
    }

    public void setSpriteFront(String spriteFront) {
        this.spriteFront = spriteFront;
    }

    public ArrayList<String> getTypes() {
        return types;
    }

    public void setTypes(ArrayList<String> types) {
        this.types = types;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public double getBaseExperience() {
        return baseExperience;
    }

    public void setBaseExperience(double baseExperience) {
        this.baseExperience = baseExperience;
    }

    public ArrayList<String> getAbilities() {
        return abilities;
    }

    public void setAbilities(ArrayList<String> abilities) {
        this.abilities = abilities;
    }

}
