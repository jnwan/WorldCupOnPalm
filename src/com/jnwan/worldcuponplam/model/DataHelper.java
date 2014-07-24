package com.jnwan.worldcuponplam.model;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DataHelper extends SQLiteOpenHelper

{

    private String name;

    

    public DataHelper( Context context,  String name,  SQLiteDatabase.CursorFactory c,  int n) {

        super(context, name, c, n);

        this.name = name;

    }

    

    public void close() {

        synchronized (this) {

            super.close();

        }

    }

    

    public void onCreate( SQLiteDatabase sqLiteDatabase) {


                if (name.equals("POINTS")) {

                    sqLiteDatabase.execSQL("drop table if exists point_table ");

                    sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS point_table(_id INTEGER PRIMARY KEY AUTOINCREMENT, rank INTEGER, team VARCHAR, session INTEGER, win INTEGER, tie INTEGER, lose INTEGER, GD INTEGER, point INTEGER)");

                    return;

                }

                if (name.equals("SHOOTER")) {

                    sqLiteDatabase.execSQL("drop table if exists shooter_table ");

                    sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS shooter_table(_id INTEGER PRIMARY KEY AUTOINCREMENT, rank INTEGER, player VARCHAR, national_team VARCHAR, club_team VARCHAR, goal INTEGER)");

                    return;
               }

        sqLiteDatabase.execSQL("drop table if exists assist_table ");

        sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS assist_table(_id INTEGER PRIMARY KEY AUTOINCREMENT, rank INTEGER, player VARCHAR, national_team VARCHAR, club_team VARCHAR, assist INTEGER)");

    }

    

    public void onUpgrade( SQLiteDatabase sqLiteDatabase,  int n,  int n2) {

    }

}