package com.example.msnihur_subbook;

/**
 * Created by marissasnihur on 2018-01-21.
 */
import android.net.wifi.aware.SubscribeConfig;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
public class Subscriptions implements Serializable {
    public String name;
    public Date date;
    public double charge;
    public String comment;
    private static float total;

    public Subscriptions(String name, Date date, double charge, String comment){
        this.name = name;
        this.date = date;
        this.charge = charge;
        this.comment = comment;
    }
    public Subscriptions(String name, Date date, double charge){
        this.name = name;
        this.date = date;
        this.charge = charge;
    }
    public String getName(){
        return name;
    }
    public Date getDate(){
        return date;
    }
    public double getCharge(){
       return charge;
    }
    public String getComment(){
        return comment;
    }
    public String setName(String name){
        if(name.length()>20) {
            return "Sorry name is too long, maximum 20 characters";
        }
        else {
            this.name = name;
        }
        return null;
    }
    public void setDate(Date date){
        this.date = date;
    }
    public String setCharge(double charge){
        if(charge<0){
            return "Sorry monthly charge cannot be a negative number";
        }
        else {
            this.charge = charge;
            //total = total + this.charge;
        }
        return null;
    }
    public String setComment(String comment){
        if(comment.length()>30){
            return "Sorry Comment must be under 30 characters";
        }
        else {
            this.comment = comment;
        }
        return null;
    }

    public void deleteSub(String name, Date date, double charge, String comment){
        this.name = null;
        this.date = null;
        //total = total - this.charge;
        this.charge = Integer.parseInt(null);
        this.comment = null;

    }
    public String toString(){
        SimpleDateFormat formattedDate = new SimpleDateFormat("yyyy-MM-dd");
        String dateVal = formattedDate.format(date);
        return "Subscription Name: " + name + "\n   Date: " + dateVal + "\n   Monthly Charge: " + charge + "\n   Additional Comments " + comment;
    }
}

