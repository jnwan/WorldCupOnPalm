package com.jnwan.worldcuponplam.model;



public class MyDate

{

    public int day;

    public int month;

    public int type;

    

    public MyDate( int month,  int day) {

        this.month = month;

        this.day = day;

    }

    

    public int compareTo( MyDate myDate) {

        if (this.month >= myDate.month) {

            if (this.month > myDate.month) {

                return 1;

            }

            if (this.month == myDate.month) {

                if (this.day == myDate.day) {

                    return 0;

                }

                if (this.day > myDate.day) {

                    return 1;

                }

                if (this.day < myDate.day) {

                    return -1;

                }

            }

            return Integer.MIN_VALUE;

        }

        return -1;

    }

    

    @Override

    public String toString() {

        return String.valueOf(month) + String.valueOf(day);

    }

}