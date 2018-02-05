package com.example.msnihur_subbook;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class edit_screen extends AppCompatActivity {

    String Name;
    String Comment;
    double Charge;
    Date date;
    private String newName;
    private String newComment;
    private double newCharge;
    private String newDateString;
    private Date newDate;
    private int pos;
    private double totalCost;

    /*
    Specifies what to do onCreate. Takes in the subscription and outputs
    the editted subscription, changing name,comment,date,charge. Contains the
    edit button onClickListener. Also contains the Go Back Button onClickListener.
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_screen);

        Intent subscription = getIntent();
        final Subscriptions editSub = (Subscriptions) subscription.getSerializableExtra("toEdit");
        pos = subscription.getIntExtra("position", 0);
        totalCost = subscription.getDoubleExtra("charge",0);

        Name = editSub.getName();
        Comment = editSub.getComment();
        Charge = editSub.getCharge();
        date = editSub.getDate();
        totalCost = totalCost - Charge;
        TextView nameText = (TextView) findViewById(R.id.Name);
        TextView CommentText = (TextView) findViewById(R.id.Comment);
        TextView DateText = (TextView) findViewById(R.id.Date);
        TextView ChargeText = (TextView) findViewById(R.id.Charge);

        nameText.setText(Name);
        CommentText.setText(Comment);
        ChargeText.setText(Double.toString(Charge));

        DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String dateText = format.format(date);
        DateText.setText(dateText);

        Button EditButton = (Button) findViewById(R.id.editbutton);
        Button BackButton = (Button) findViewById(R.id.exit_button);

        EditButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText NameValue = (EditText) findViewById(R.id.editName);
                EditText DateValue = (EditText) findViewById(R.id.editDate);
                EditText ChargeValue = (EditText) findViewById(R.id.editCharge);
                EditText CommentValue = (EditText) findViewById(R.id.editComment);

                newName = NameValue.getText().toString();
                newComment = CommentValue.getText().toString();
                try {
                    newCharge = Double.parseDouble(ChargeValue.getText().toString());
                }catch(NumberFormatException e){
                    e.printStackTrace();
                }
                newDateString = DateValue.getText().toString();
                SimpleDateFormat formattedDate = new SimpleDateFormat("yyyy-MM-dd");
                try {
                    newDate = formattedDate.parse(newDateString);
                }catch(java.text.ParseException e ){
                    e.printStackTrace();
                }
                totalCost = totalCost + newCharge;
                editSub.setName(newName);
                editSub.setCharge(newCharge);
                editSub.setDate(newDate);
                editSub.setComment(newComment);

                Log.i(editSub.toString(), "here");
                Log.i(Integer.toString(pos),"YESSIRY");

                Intent intent = new Intent(view.getContext(), MainActivity.class);
                intent.putExtra("position",pos);
                intent.putExtra("edit",editSub);
                intent.putExtra("charge",totalCost);
                setResult(1, intent);
                finish();

            }

        });
        BackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setResult(1, null);
                finish();
            }
        });



    }
}
