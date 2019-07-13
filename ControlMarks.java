package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class ControlMarks extends AppCompatActivity {
Button Submit;
EditText Email, Subject, Mark;
DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_control_marks);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),Login_Activity.class));
            }
        });
        //toolbar exit button
        Button exit = findViewById(R.id.exit);
        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.exit(0);
            }
        });
        Submit = findViewById(R.id.submit);

        Email = findViewById(R.id.email);
        Subject = findViewById(R.id.subject);
        Mark = findViewById(R.id.mark);
Submit.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
      FirebaseDatabase.getInstance().getReference().child("Users").addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                String checkemail = Email.getText().toString();
 String checksubject = Subject.getText().toString();
                String mark = Mark.getText().toString();
                for (DataSnapshot user : dataSnapshot.getChildren()) {
                    String emailpath = user.child("email").getValue(String.class);
 String Subjectpath = user.child("Subject").getValue(String.class);
                    if (emailpath != null &&Subjectpath!= null && emailpath.equals(checkemail) && Subjectpath.equals(checksubject)) {
                        databaseReference.child("Users").child(Objects.requireNonNull(user.getKey())).child("Subject").child(Subjectpath).child("Mark").setValue(mark);

                       Toast.makeText(getApplicationContext(),"Success", Toast.LENGTH_LONG).show();
                       return;
                    }

                }
                Toast.makeText(getApplicationContext(),"Fail",Toast.LENGTH_LONG).show();


                }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }




    });}}