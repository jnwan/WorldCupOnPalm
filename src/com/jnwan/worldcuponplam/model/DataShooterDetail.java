package com.jnwan.worldcuponplam.model;



public class DataShooterDetail

{

    public String club_team;

    public int goal;

    public String national_team;

    public String player;

    

    public DataShooterDetail( String player,  String national_team,  String club_team,  int goal) {

        this.player = player;

        this.national_team = national_team;

        this.club_team = club_team;

        this.goal = goal;

    }

}