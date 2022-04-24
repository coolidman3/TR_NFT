package com.example.nft;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.nft.databinding.ActivityMainmenuBinding;
import com.example.nft.databinding.ActivityProfileBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class profile extends AppCompatActivity {
    ActivityProfileBinding Binding;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseUser account = FirebaseAuth.getInstance().getCurrentUser();
    public String acc = account.getEmail();
    private TextView ETH, Name, email;
    private Button logot;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        Binding = ActivityProfileBinding.inflate(getLayoutInflater());
        setContentView(Binding.getRoot());


        Binding.botom.setSelectedItemId(R.id.prof);
        Binding.botom.setOnItemSelectedListener(item ->  {
            switch (item.getItemId()){
                case R.id.home:
                    startActivity(new Intent(getApplicationContext(), mainmenu.class));
                    break;
                case R.id.prof:

                    break;
                case R.id.gall:
                    startActivity(new Intent(getApplicationContext(), gallery.class));
                    break;
            }

            return true;
        });




            ETH = findViewById(R.id.eth);
        Name = findViewById(R.id.Name);
        email = findViewById(R.id.email);
        logot = findViewById(R.id.logot);
        logot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
logot();
            }
        });
        hist();
    }



    public void hist(){
        Intent intent = getIntent();
        db.collection("Users").whereEqualTo("Email",acc).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()){
                    for (QueryDocumentSnapshot document : task.getResult()){
                        Log.d("punten", document.getId() + " awe " + document.getData().get("Name"));
                        Name.setText("Hello " + document.getData().get("Name").toString());
                        ETH.setText("Your ETH = " + document.getData().get("Eth").toString());
                        email.setText("Your Email's " + acc);
                    }
                }
            }
        });
    }
    public void logot(){
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(getApplicationContext(), MainActivity.class));

    }

}