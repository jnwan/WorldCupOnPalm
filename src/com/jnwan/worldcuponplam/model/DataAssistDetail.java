package com.jnwan.worldcuponplam.model;

public class DataAssistDetail

{

    public int assist;

    public String club_team;

    public String national_team;

    public String player;

    

    public DataAssistDetail(final String player, final String national_team, final String club_team, final int assist) {

        super();

        this.player = player;

        this.national_team = national_team;

        this.club_team = club_team;

        this.assist = assist;

    }

}