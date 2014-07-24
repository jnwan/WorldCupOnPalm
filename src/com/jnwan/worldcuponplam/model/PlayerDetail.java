package com.jnwan.worldcuponplam.model;

public class PlayerDetail

{

    public String birth;

    public String briefInfo;

    public String club;

    public String wheight;

    public String name;

    public int photo;

    public String position;


    

    public PlayerDetail( int photo,  String name,  String position,  String birth,  String wheight,  String club) {

        super();

        this.position = position;

        this.name = name;

        this.photo = photo;

        this.birth = birth;

        this.club = club;

        this.wheight = wheight;

    }

    

    @Override

    public String toString() {

        if (this.name.length() > 5) {

            return this.name.substring(0, 5)+"...";

        }

        return this.name;

    }

}