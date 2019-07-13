package com.example.myapplication;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import io.paperdb.Paper;

public class Login_Activity extends AppCompatActivity  {


    private EditText emailTV, passwordTV;
    private Button loginBtn, btnRegister;
    private ProgressBar progressBar;
    private CheckBox rememberme;
    private FirebaseAuth mAuth;

    @Override
    protected void onStart() {
        super.onStart();
if(mAuth.getCurrentUser()!= null && emailTV.getText().toString().trim().length()>0 && passwordTV.getText().toString().trim().length()>0 ) {
String emailchecked  = Paper.book().read(prevalent.UserEmailKey);
String passchecked = Paper.book().read(prevalent.UserPasswordKey);
emailTV.setText(emailchecked);
passwordTV.setText(passchecked);
loginBtn.performClick();

}
else {mAuth.signOut();
    Paper.book().destroy();}

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_);

mAuth = FirebaseAuth.getInstance();
        initializeUI();
        Paper.init(this);
btnRegister.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {

        Intent intent = new Intent(Login_Activity.this, Register.class);
        startActivity(intent);

    }
});
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginUserAccount();
            }
        });
    }



    private void loginUserAccount() {
        progressBar.setVisibility(View.VISIBLE);

        String email, password;
        email = emailTV.getText().toString();
        password = passwordTV.getText().toString();

        if (TextUtils.isEmpty(email)) {
            Toast.makeText(getApplicationContext(), "Please enter email...", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(password)) {
            Toast.makeText(getApplicationContext(), "Please enter password!", Toast.LENGTH_LONG).show();
            return;
        }


        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            DatabaseReference firebaseDatabase = FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Role");
                            firebaseDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    if (dataSnapshot.getValue().equals("Student")){
                                        Toast.makeText(getApplicationContext(), "Login successful!", Toast.LENGTH_LONG).show();
                                        progressBar.setVisibility(View.GONE);
                                        Intent intent = new Intent(Login_Activity.this, MainPageActivity.class);
                                        startActivity(intent);
                                    }
                                    else {
                                        Toast.makeText(getApplicationContext(), "Login successful!", Toast.LENGTH_LONG).show();
                                        progressBar.setVisibility(View.GONE);
                                        Intent intent = new Intent(Login_Activity.this, MainActivity.class);
                                        startActivity(intent);
                                    }

                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });

                            }

                        else {
                            Toast.makeText(getApplicationContext(), "Login failed! Please try again later", Toast.LENGTH_LONG).show();
                            progressBar.setVisibility(View.GONE);
                        }
                    }
                });

        if(rememberme.isChecked()){

            Paper.book().write(prevalent.UserEmailKey, email);
            Paper.book().write(prevalent.UserPasswordKey, password);

        }
    }



    private void initializeUI() {
        emailTV = findViewById(R.id.email);
        passwordTV = findViewById(R.id.password);
btnRegister = findViewById(R.id.btnRegister);
        loginBtn = findViewById(R.id.BtnLogin);
rememberme = findViewById(R.id.rememberme);
        progressBar = findViewById(R.id.progressBar);
    }

}

