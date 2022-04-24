package com.example.nft;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.nft.Adapter.NftAdapter;
import com.example.nft.Model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class detail extends AppCompatActivity {

    private List<User> list = new ArrayList<>();
private TextView Price, Name, histrec;
private Button buy;
private ImageView avatar;
    private FirebaseUser account = FirebaseAuth.getInstance().getCurrentUser();
    public String acc = account.getEmail();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference colref = db.collection("NFT");
    private FirebaseStorage storage = FirebaseStorage.getInstance();
    private NftAdapter nftAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        getSupportActionBar().hide();
        Price = findViewById(R.id.pric);
        Name = findViewById(R.id.namepicc);
        avatar = (ImageView) findViewById(R.id.picnftt);
        histrec = findViewById(R.id.data);
        buy = findViewById(R.id.sell);


        Intent intent= getIntent();
        Glide.with(this).load(intent.getStringExtra("pict")).into(avatar);
        Price.setText(intent.getStringExtra("pri").toString());
        Name.setText(intent.getStringExtra("namea"));
        String id = intent.getStringExtra("ID");

buy.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        long pr = Long.parseLong(intent.getStringExtra("pri").toString());
        hist(pr);
    }
});
loaddata(id);
    }


public void hist(long pr){
        db.collection("Users").whereEqualTo("Email",acc).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()){
                    for (QueryDocumentSnapshot document : task.getResult()){
                        Log.d("punten", document.getId() + " => " + document.getData().get("Name"));
                       String name = (String) document.getData().get("Name");
                       String id = (String) document.getId();
                       long eth = (Long) document.getData().get("Eth");
                       long pem = eth-pr;
                       Log.d("punten", "aww" + eth + pr +" ahahahaha " +id );
                        db.collection("Users").document(id).update("Eth", pem).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Log.d("punten", "Bisa update eth pembeli");
                            }
                        });
                        Intent intent= getIntent();
                        hist2(name);
                    }
                }
            }
        });
}

public void hist2(String name){
        Intent i = getIntent();
        String id = i.getStringExtra("ID");
        String pr = i.getStringExtra("pri").toString();
        long pr12 = Long.parseLong(i.getStringExtra("pri").toString());
        Intent intent= getIntent();
        db.collection("NFT").document(id).update("History", FieldValue.arrayUnion(name)).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    Log.d("punten", "Bisa hist");
                }
            }
        });
        db.collection("NFT").document(id).update("Status", "Sold").addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    Log.d("punten", "update stat");
                }
            }
        });
    db.collection("NFT").document(id).update("Owner", name).addOnCompleteListener(new OnCompleteListener<Void>() {
        @Override
        public void onComplete(@NonNull Task<Void> task) {
            if (task.isSuccessful()){
                Log.d("punten", "Bisa ganti owner");
            }
        }
    });
    db.collection("Users").whereEqualTo("Name", intent.getStringExtra("owner")).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
        @Override
        public void onComplete(@NonNull Task<QuerySnapshot> task) {
            if (task.isSuccessful()){
                for (QueryDocumentSnapshot document : task.getResult()) {
                    String id1;
                    Long eth1, eth2;
                    id1 = (String) document.getId();
                    eth1 = (Long) document.getData().get("Eth");
                    eth2 = (Long)  eth1 + pr12;
                    Log.d("punten", "Bisa ambil owner" + id1 +" ahaha " + eth1 );

                    db.collection("Users").document(id1).update("Eth",eth2 ).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Log.d("punten", "Bisa update eth penjual");
                        }
                    });

                }
            }
        }
    });
    Toast.makeText(getApplicationContext(), "Buying Resource Successfull", Toast.LENGTH_LONG).show();
    startActivity(new Intent(getApplicationContext(), gallery.class));

}












    public void loaddata(String id) {
       db.collection("NFT").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
           @Override
           public void onComplete(@NonNull Task<QuerySnapshot> task) {
               String data = "" ;
               if (task.isSuccessful()){
                   for (QueryDocumentSnapshot doc : task.getResult()){
//                       User user = doc.toObject(User.class);
//                       String name = user.getHistory();




                       Log.d("punten", doc.getId() + "aha" +doc.get("History"));
                       if (id.equals(doc.getId().toString())){
                           Log.d("punten", "aha" + doc.get("History").toString());
                           String name = doc.get("History").toString().substring(1, doc.get("History").toString().length()-1);
                           data += "From : "+ name;

                       }
                   }
                   histrec.setText(data.replaceAll(",", "\n\nFrom : "));
               }
           }
       });
    }
}