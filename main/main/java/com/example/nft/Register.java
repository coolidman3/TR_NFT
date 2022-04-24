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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class Register extends AppCompatActivity {

    private EditText username,email,password,confirm;
    private TextView signin;
    private Button register;
    private FirebaseAuth auth;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        getSupportActionBar().hide();

        auth = FirebaseAuth.getInstance();
        username = findViewById(R.id.editTextTextPersonName);
        email = findViewById(R.id.editTextTextPersonName2);
        password = findViewById(R.id.editTextTextPassword);
        confirm = findViewById(R.id.editTextTextPassword2);
        signin = findViewById(R.id.textView8);
        register = findViewById(R.id.button3);

        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signin(view);
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                register();
            }
        });
    }
    public void register() {

        String userName = username.getText().toString();
        String Email = email.getText().toString();
        String pass = password.getText().toString();
        String confir = confirm.getText().toString();
        long eth = 1000;

        if(TextUtils.isEmpty(userName)){
            Toast.makeText(this,"Enter Username!", Toast.LENGTH_SHORT).show();
        }

        if(TextUtils.isEmpty(Email)){
            Toast.makeText(this,"Enter Email Address!", Toast.LENGTH_SHORT).show();
        }

        if(TextUtils.isEmpty(pass)){
            Toast.makeText(this,"Enter Password!", Toast.LENGTH_SHORT).show();
        }

        if(TextUtils.isEmpty(confir)){
            Toast.makeText(this,"Enter Confrim Password!", Toast.LENGTH_SHORT).show();
        }

        if(pass.equals(confir)==false){
            Toast.makeText(this,"Your Confirm Password not same!", Toast.LENGTH_SHORT).show();
        }

        if(pass.length() < 6){
            Toast.makeText(this,"Password to short, enter minimum 6 characters!", Toast.LENGTH_SHORT).show();
        }

        else{
            auth.createUserWithEmailAndPassword(Email,pass)
                .addOnCompleteListener(Register.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
//                            User user = new User(userName, Email, pass);
                            Map<String, Object> user = new HashMap<>();
                            user.put("Name", userName);
                            user.put("Email", Email);
                            user.put("Eth", eth);

                            db.collection("Users")
                                    .add(user)
                                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                        @Override
                                        public void onSuccess(DocumentReference documentReference) {
                                            Toast.makeText(Register.this,"Registration Success", Toast.LENGTH_SHORT).show();
                                            startActivity(new Intent(Register.this, MainActivity.class));
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(Register.this,"Registration Failed."+e.getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        }else{
                            Toast.makeText(Register.this,"Registration Failed."+task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        }
    public void signin(View view){
        Intent k = new Intent(this,Signin.class);
        startActivity(k);
    }
//    public void savedata(String nama, String email){
//        Map<String, Object> user = new HashMap<>();
//        user.put("Name", nama);
//        user.put("Email", email);
//        user.put("Eth", "1000000");
//
//        db.collection("Users")
//                .add(user)
//                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
//                    @Override
//                    public void onSuccess(DocumentReference documentReference) {
//                        Toast.makeText(Register.this,"Registration Success", Toast.LENGTH_SHORT).show();
//                        startActivity(new Intent(Register.this, MainActivity.class));
//                    }
//                })
//                .addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//                        Toast.makeText(Register.this,"Registration Failed."+e.getMessage(), Toast.LENGTH_SHORT).show();
//                    }
//                });
//    }
    }