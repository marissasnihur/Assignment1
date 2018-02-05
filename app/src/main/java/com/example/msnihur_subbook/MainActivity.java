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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //sublist = new ArrayList<Subscriptions>();
        //ListView oldSubList = new ListView(MainActivity.this);
        Button addButton = findViewById(R.id.addbutton);
        oldSubList = findViewById(R.id.subscripListView);
        oldSubList.setClickable(true);
        TCost = findViewById(R.id.total_value);


        //set up add button to go to add_screen
        addButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                setResult(RESULT_OK);
                Intent intent = new Intent(getApplicationContext(), add_screen.class);
                startActivityForResult(intent, 2);
            }}
        );
        //oldSubList.setClickable(true);
        oldSubList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                final Object subscription = oldSubList.getItemAtPosition(position);
                final int index = position;
                final Subscriptions sub = (Subscriptions) subscription;
                AlertDialog.Builder DoYouWantToDeleteorEdit = new AlertDialog.Builder(MainActivity.this, R.style.DoYouWantToDelete);
                DoYouWantToDeleteorEdit.setTitle("DELETE OR EDIT?");
                DoYouWantToDeleteorEdit.setMessage("WOULD YOU LIKE TO DELETE OR EDIT THIS ITEM?");
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

    @Override
    protected void onStart(){
        super.onStart();
        sublist = new ArrayList<Subscriptions>();
        load();
        adapter = new ArrayAdapter<Subscriptions>(MainActivity.this, android.R.layout.simple_list_item_1, android.R.id.text1, sublist);
        oldSubList.setAdapter(adapter);
        TCost.setText(decimalFormat.format(totalCost));
    }
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

        protected void onDestroy(){
            super.onDestroy();
        }
}



