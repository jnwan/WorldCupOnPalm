package com.jnwan.worldcuponplam.model;

public class BroadcastDetail

{

    public GameTime gametime;

    public String history;

    public int score1;

    public int score2;

    public String team1;

    public String team2;

    public String type;

    

    public BroadcastDetail(final String team1, final String team2, final String type, final String history, final GameTime gametime, final int score1, final int score2) {

        super();

        this.team1 = team1;

        this.team2 = team2;

        this.gametime = gametime;

        this.type = type;

        this.history = history;

        this.score1 = score1;

        this.score2 = score2;

    }

}