package com.example.myapplication;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;


public class Register extends AppCompatActivity {

    private EditText emailTV, passwordTV, confirmPasswordTV;
    private Button regBtn;
    private ProgressBar progressBar;
    private RadioGroup radioGroup;
private Spinner spinnertv ;
    private FirebaseAuth mAuth;
    private RadioButton radioButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initializeUI();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new         Intent(getApplicationContext(),Login_Activity.class));
            }
        });
        Button exit = findViewById(R.id.exit);
        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.exit(1);
            }
        });
        ArrayAdapter<String> RoleAdapter = new ArrayAdapter<String>(Register.this, android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.roles))
        ;

        spinnertv.setAdapter(RoleAdapter);
      spinnertv.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
          @Override
          public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
              String state = parent.getSelectedItem().toString();
              if (state.equals("Student")){

                 radioGroup.setVisibility(View.VISIBLE);
              }
              else {

                  radioGroup.setVisibility(View.GONE);
              }
          }

          @Override
          public void onNothingSelected(AdapterView<?> parent) {

          }
      });

RoleAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mAuth = FirebaseAuth.getInstance();




        regBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerNewUser();
            }
        });
    }

    private void registerNewUser() {
        progressBar.setVisibility(View.VISIBLE);

        final String email, password, confirmpassword;
        final String Role, Subject ;

        email = emailTV.getText().toString();
        password = passwordTV.getText().toString();
confirmpassword = confirmPasswordTV.getText().toString();

Role = spinnertv.getSelectedItem().toString();

        final int subject = radioGroup.getCheckedRadioButtonId();
        radioButton = findViewById(subject);
      Subject =  radioButton.getText().toString();
        if (TextUtils.isEmpty(email)) {
            Toast.makeText(getApplicationContext(), "Please enter email...", Toast.LENGTH_LONG).show();
            return;
        }
        if (TextUtils.isEmpty(password)) {
            Toast.makeText(getApplicationContext(), "Please enter password!", Toast.LENGTH_LONG).show();
            return;
        }
        if (TextUtils.isEmpty(confirmpassword) ) {
            Toast.makeText(getApplicationContext(), "Please enter password!", Toast.LENGTH_LONG).show();
            return;
        }
        if( !confirmpassword.equals(password)){
            Toast.makeText(getApplicationContext(), "Passwords does not match!", Toast.LENGTH_LONG).show();
            return;

        }
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(getApplicationContext(), "Registration successful!", Toast.LENGTH_LONG).show();
                            progressBar.setVisibility(View.GONE);

                            User user = new User(email, Role, Subject);
                            FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        Intent intent = new Intent(Register.this, Login_Activity.class);
                                        startActivity(intent);
                                    } else {
                                        Toast.makeText(getApplicationContext(), "Fail", Toast.LENGTH_LONG).show();
                                    }
                                }
                            });

                        }
                        else {
                            Toast.makeText(getApplicationContext(), "Registration failed! Please try again later", Toast.LENGTH_LONG).show();
                            progressBar.setVisibility(View.GONE);
                        }
                    }
                });
    }



    private void initializeUI() {
        emailTV = findViewById(R.id.email);
        passwordTV = findViewById(R.id.password);
spinnertv = findViewById(R.id.spinner1);
        regBtn = findViewById(R.id.register);
        progressBar = findViewById(R.id.progressBar);
radioGroup = findViewById(R.id.radiogroup);
confirmPasswordTV = findViewById(R.id.comfirmpassword);
    }
}