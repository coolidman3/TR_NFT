package com.example.nft;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Signin extends AppCompatActivity {
    private EditText username,email,password,confirm;
    private TextView signup;
    private Button login;
    private FirebaseAuth auth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);
        getSupportActionBar().hide();

        auth = FirebaseAuth.getInstance();
        email = findViewById(R.id.editTextTextPersonName3);
        password = findViewById(R.id.editTextTextPassword3);
        signup = findViewById(R.id.textView14);
        login = findViewById(R.id.button4);

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signup(view);
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { login();
            }
        });
    }

    public void login(){

        String Email = email.getText().toString();
        String pass = password.getText().toString();

        if(TextUtils.isEmpty(Email)){
            Toast.makeText(this,"Enter Email Address!", Toast.LENGTH_SHORT).show();
        }

        if(TextUtils.isEmpty(pass)){
            Toast.makeText(this,"Enter Password!", Toast.LENGTH_SHORT).show();
        }


        else{
            auth.signInWithEmailAndPassword(Email,pass)
                .addOnCompleteListener(Signin.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(Signin.this,"Login Success", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(Signin.this, mainmenu.class));
                        }else{
                            Toast.makeText(Signin.this,"Login Failed. "+task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
        }
    }

    public void signup(View view){
        Intent k = new Intent(this,Register.class);
        startActivity(k);
    }
}