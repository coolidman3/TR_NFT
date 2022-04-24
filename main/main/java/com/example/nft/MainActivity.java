package com.example.nft;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    private Button register,sign;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();

        register = findViewById(R.id.button2);
        sign = findViewById(R.id.button);

        sign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                psign(view);
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                preg(view);
            }
        });

    }
    public void preg(View view){
        Intent k = new Intent(this,Register.class);
        startActivity(k);
    }
    public void psign(View view){
        Intent k = new Intent(this,Signin.class);
        startActivity(k);
    }

}