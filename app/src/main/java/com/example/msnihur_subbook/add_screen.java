package com.example.msnihur_subbook;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static android.provider.Telephony.Mms.Part.FILENAME;

public class add_screen extends AppCompatActivity {

    private EditText nameText;
    private EditText chargeText;
    private EditText dateText;
    private EditText commentText;
    private String name;
    private String comment;
    private double charge;
    private String dateString;
    private Date date;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_screen);

        Button submit = findViewById(R.id.submit);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setResult(RESULT_OK);
                nameText = findViewById(R.id.nameOfSub);
                chargeText = findViewById(R.id.monthly_charge);
                commentText = findViewById(R.id.comments);
                dateText = findViewById(R.id.dateOfSub);
                name = nameText.getText().toString();
                comment = commentText.getText().toString();
                try {
                    charge = Double.parseDouble(chargeText.getText().toString());
                }catch(NumberFormatException e){
                    e.printStackTrace();
                }
                dateString = dateText.getText().toString();
                SimpleDateFormat formattedDate = new SimpleDateFormat("yyyy-MM-dd");
                //date = new Date();
                try {
                    date = formattedDate.parse(dateString);
                }catch(java.text.ParseException e ){
                    e.printStackTrace();
                }
                //String name = "Hello";
                //String comment = "Here";
                //Date date = new Date();
                //double charge = 9.75;


                Intent intent = new Intent(view.getContext(), MainActivity.class);
                intent.putExtra("SubName", name);
                intent.putExtra("SubDate",date.getTime());
                intent.putExtra("SubCharge",charge);
                intent.putExtra("SubComment",comment);
                setResult(2, intent);
                finish();

            }
        });
    }

    protected void onDestroy(){
        super.onDestroy();
    }
}
