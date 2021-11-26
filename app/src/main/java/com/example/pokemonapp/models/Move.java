package com.example.pokemonapp.models;

public class Move {
    private String name;
    private String type;
    private int power;
    private int accuracy;
    private int powerPoint;
    private String description;

    public Move(){}

    public Move(String name, String type, int power, int accuracy, int powerPoint, String description) {
        this.name = name;
        this.type = type;
        this.power = power;
        this.accuracy = accuracy;
        this.powerPoint = powerPoint;
        this.description = description;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setPower(int power) {
        this.power = power;
    }

    public void setAccuracy(int accuracy) {
        this.accuracy = accuracy;
    }

    public void setPowerPoint(int powerPoint) {
        this.powerPoint = powerPoint;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public int getPower() {
        return power;
    }

    public int getAccuracy() {
        return accuracy;
    }

    public int getPowerPoint() {
        return powerPoint;
    }

    public String getDescription() {
        return description;
    }
}
