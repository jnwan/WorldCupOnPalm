package com.jnwan.worldcuponplam.model;



public class DataPointDetail

{

    public int GD;

    public int loses;

    public int point;

    public int sessions;

    public String teamGroup;

    public String teamName;

    public int ties;

    public int wins;

    

    public DataPointDetail(final String teamName, final String teamGroup, final int point, final int sessions, final int wins, final int ties, final int loses, final int gd) {

        super();

        this.teamName = teamName;

        this.teamGroup = teamGroup;

        this.point = point;

        this.sessions = sessions;

        this.wins = wins;

        this.ties = ties;

        this.loses = loses;

        this.GD = gd;

    }
  
  public String toString()
  {
     return null;
  }
}
