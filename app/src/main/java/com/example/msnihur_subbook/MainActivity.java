/*
 * Copyright (c) 2018. CMPUT 301
 */

package com.example.msnihur_subbook;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Serializable;
import java.lang.reflect.Type;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;


/*
Main Activity that contains all of the onClick functions and the onCreate function.
 */

public class MainActivity extends AppCompatActivity {
    private static final String FILENAME = "NewFileFor301.sav";
    private static final String TOTALNAME = "NewCostFile301.sav";


    protected ArrayList<Subscriptions> sublist;
    private ArrayAdapter<Subscriptions> adapter;
    private double totalCost;
    private ListView oldSubList;
    public int pos;
    private TextView TCost;
    private DecimalFormat decimalFormat = new DecimalFormat(".##");

    /*
    onCreate initializes the arrayList and totcalCost variables, as well as the ListView
     */


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button addButton = findViewById(R.id.addbutton);
        oldSubList = findViewById(R.id.subscripListView);
        oldSubList.setClickable(true);
        TCost = findViewById(R.id.total_value);

        /*
        onClickListener for the add button, allows subscriptions to be added and goes to add screen.
        */
        addButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                setResult(RESULT_OK);
                Intent intent = new Intent(getApplicationContext(), add_screen.class);
                startActivityForResult(intent, 2);
            }}
        );
        /*
        Sets up the dialog box for the Edit/View and Delete functions to be accessed.
         */
        oldSubList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                final Object subscription = oldSubList.getItemAtPosition(position);
                final int index = position;
                final Subscriptions sub = (Subscriptions) subscription;
                AlertDialog.Builder DoYouWantToDeleteorEdit = new AlertDialog.Builder(MainActivity.this, R.style.DoYouWantToDelete);
                DoYouWantToDeleteorEdit.setTitle("DELETE OR EDIT?");
                DoYouWantToDeleteorEdit.setMessage("WOULD YOU LIKE TO DELETE OR EDIT THIS ITEM?");
                /*
                Sets up the delete button and what to do it if is pressed.
                 */
                DoYouWantToDeleteorEdit.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        switch (i) {
                            case DialogInterface.BUTTON_POSITIVE:
                                //double val = sub.getCharge();
                                //totalCost = totalCost - val;
                                sublist.remove(subscription);
                                double val = sub.getCharge();
                                totalCost = totalCost - val;
                                adapter.notifyDataSetChanged();
                                save();
                                TCost.setText(decimalFormat.format(totalCost));
                                break;
                            case DialogInterface.BUTTON_NEGATIVE:
                                break;
                        }
                    }
                });

                /*
                Sets up the View/Edit button and what to do if Edit/View is pressed.
                 */


                DoYouWantToDeleteorEdit.setNegativeButton("Edit/View", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        switch (i) {
                            case DialogInterface.BUTTON_POSITIVE:
                                break;
                            case DialogInterface.BUTTON_NEGATIVE:
                                Intent intent = new Intent(getApplicationContext(), edit_screen.class);
                                intent.putExtra("position", index);
                                //Subscriptions sub = (Subscriptions) subscription;
                                intent.putExtra("toEdit", sub);
                                intent.putExtra("charge",totalCost);
                                startActivityForResult(intent, 1);
                                break;
                        }
                    }
                });
                /*
                Sets up the Cancel button which returns nothing.
                 */
                DoYouWantToDeleteorEdit.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        switch(i) {
                            case DialogInterface.BUTTON_NEUTRAL:
                                break;
                        }
                    }
                });
                DoYouWantToDeleteorEdit.show();
            }
        });
    }
    /*
    Sets up what happens for the activities to return too, REQUEST_CODE == 1 is the adding function
    and REQUEST_CODE == 2 is for the Edit button.
     */

    @Override
    public void onActivityResult(int REQUEST_CODE, int RESULT_CODE, Intent subscription) {
        if(subscription == null) {
        }
       else if (REQUEST_CODE == 2) {
            String SubName = subscription.getStringExtra("SubName");
            String SubComment = subscription.getStringExtra("SubComment");
            Date SubDate = new Date();
            SubDate.setTime(subscription.getLongExtra("SubDate", -1));
            double SubCharge = subscription.getDoubleExtra("SubCharge", 0);
            totalCost = totalCost + SubCharge;
            Subscriptions Sub = new Subscriptions(SubName, SubDate, SubCharge, SubComment);
            sublist.add(Sub);
            save();
            TCost.setText(decimalFormat.format(totalCost));
        }
        else if (REQUEST_CODE == 1) {
            Subscriptions editSub = (Subscriptions) subscription.getSerializableExtra("edit");
            pos = subscription.getIntExtra("position", 0);
            totalCost = subscription.getDoubleExtra("charge", 0);
            sublist.set(pos, editSub);
            adapter.notifyDataSetChanged();
            save();
            TCost.setText(decimalFormat.format(totalCost));
        }
        }

        /*
        The function which starts up the application.
         */
    @Override
    protected void onStart(){
        super.onStart();
        sublist = new ArrayList<Subscriptions>();
        load();
        adapter = new ArrayAdapter<Subscriptions>(MainActivity.this, android.R.layout.simple_list_item_1, android.R.id.text1, sublist);
        oldSubList.setAdapter(adapter);
        TCost.setText(decimalFormat.format(totalCost));
    }
    /*
    Allows the APP to load from the file for sublist and the file for totalCost.
     */
    public void load(){
        try {
            FileInputStream fis = openFileInput(FILENAME);
            BufferedReader in = new BufferedReader(new InputStreamReader(fis));
            Gson gson = new Gson();
            Type listType = new TypeToken<ArrayList<Subscriptions>>(){}.getType();
            sublist = gson.fromJson(in, listType);
        }catch(FileNotFoundException e){
            e.printStackTrace();
            sublist = new ArrayList<Subscriptions>();
        }
        try {
            FileInputStream fis = openFileInput(TOTALNAME);
            BufferedReader in = new BufferedReader(new InputStreamReader(fis));
            Gson gson = new Gson();
            Type d = new TypeToken<Double>(){}.getType();
            totalCost = gson.fromJson(in, d);
        }catch(FileNotFoundException e){
            e.printStackTrace();
            totalCost = 0.0;
        }
    }
/*
Allows the APP to save to a file, both the sublist and the totalCost.
 */
    public void save(){
        try{
            FileOutputStream fos = openFileOutput(FILENAME, Context.MODE_PRIVATE);
            BufferedWriter out = new BufferedWriter(new OutputStreamWriter(fos));
            Gson gson = new Gson();
            gson.toJson(sublist, out);
            out.flush();
        }catch(FileNotFoundException e){
            e.printStackTrace();
        }catch(IOException e){
            e.printStackTrace();
        }
        try{
            FileOutputStream fos = openFileOutput(TOTALNAME, Context.MODE_PRIVATE);
            BufferedWriter out = new BufferedWriter(new OutputStreamWriter(fos));
            Gson gson = new Gson();
            gson.toJson(totalCost,out);
            out.flush();
        }catch(FileNotFoundException e){
            e.printStackTrace();
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    /*
    Sets up for what happens when the APP is killed.
     */

        protected void onDestroy(){
            super.onDestroy();
        }
}



