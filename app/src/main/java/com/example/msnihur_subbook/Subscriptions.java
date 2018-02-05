package com.example.msnihur_subbook;

/**
 * Created by marissasnihur on 2018-01-21.
 */


import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

/*
Implements the subscriptions that the user implements.
 */
public class Subscriptions implements Serializable {
    public String name;
    public Date date;
    public double charge;
    public String comment;

    /*
    Constructor class for the Subscriptions class.
     */

    public Subscriptions(String name, Date date, double charge, String comment){
        this.name = name;
        this.date = date;
        this.charge = charge;
        this.comment = comment;
    }
    /*
    Constructor class for the Subscriptions class.
    */
    public Subscriptions(String name, Date date, double charge){
        this.name = name;
        this.date = date;
        this.charge = charge;
    }
    /*
    Returns the name of the subscription
     */
    public String getName(){
        return name;
    }
    /*
    Returns the date of the subscriptions.
     */
    public Date getDate(){
        return date;
    }
    /*
    Returns the charge that the subscriber uses.
     */
    public double getCharge(){
       return charge;
    }
    /*
    Returns the comment added for the subscription.
     */
    public String getComment(){
        return comment;
    }
    /*
    Sets the name to be a specific length.
     */
    public String setName(String name){
        if(name.length()>20) {
            return "Sorry name is too long, maximum 20 characters";
        }
        else {
            this.name = name;
        }
        return null;
    }
    /*
    Sets the date to be the date entered.
     */
    public void setDate(Date date){
        this.date = date;
    }
    /*
    Sets the charge to be the charge entered.
     */
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
    /*
    Sets the comment to be the comment entered.
     */
    public String setComment(String comment){
        if(comment.length()>30){
            return "Sorry Comment must be under 30 characters";
        }
        else {
            this.comment = comment;
        }
        return null;
    }
/*
Deletes the subscription from the list.
 */
    public void deleteSub(String name, Date date, double charge, String comment){
        this.name = null;
        this.date = null;
        //total = total - this.charge;
        this.charge = Integer.parseInt(null);
        this.comment = null;

    }
    /*
    Takes the Subscription and formats it for the screen.
     */
    public String toString(){
        SimpleDateFormat formattedDate = new SimpleDateFormat("yyyy-MM-dd");
        String dateVal = formattedDate.format(date);
        return "Subscription Name: " + name + "\n   Date: " + dateVal + "\n   Monthly Charge: " + charge + "\n   Additional Comments " + comment;
    }
}

