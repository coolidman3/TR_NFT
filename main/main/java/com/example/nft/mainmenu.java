package com.example.nft;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nft.Adapter.NftAdapter;

import com.example.nft.Model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class mainmenu extends AppCompatActivity {
    private TextView user;
    private RecyclerView recyclerView;
    private FloatingActionButton btnadd;
    private FirebaseUser akun;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseStorage storage = FirebaseStorage.getInstance();


    private List<User> list = new ArrayList<>();
    private NftAdapter nftAdapter;
    String owner,lama;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mainmenu);
        getSupportActionBar().hide();
        recyclerView = findViewById(R.id.recycler_view);
        btnadd = findViewById(R.id.btn_add);
//        user = findViewById(R.id.textView12);

        akun = FirebaseAuth.getInstance().getCurrentUser();
        String kun = akun.getEmail();


        db.collection("Users").whereEqualTo("Email",kun).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {

                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Log.d("weee", document.getId() + " => " + document.getData().get("Name"));
                        String Name;
                        Long eth;
                        Name = (String) document.getData().get("Name");
                        eth = (Long) document.getData().get("Eth");
//                        user.setText(Name);
                    }

                } else {
                    Log.d("ggl", "Error getting documents: ", task.getException());
                }
            }
        });




        nftAdapter = new NftAdapter(getApplicationContext(),list);
        nftAdapter.setDialog(new NftAdapter.Dialog() {
            @Override
            public void onClick(int pos) {
                final CharSequence[] dialogItem = {"Buy"};
                AlertDialog.Builder dialog = new AlertDialog.Builder(mainmenu.this);
                dialog.setItems(dialogItem, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        switch (i){
                            case 0:
                                Log.d("punten", "onClick: " +list.get(pos).getId());
                                deleteData(list.get(pos).getId());
                                break;
                        }
                    }
                });
                dialog.show();
            }
        });
//        nftAdapter.setDialog(new NftAdapter.Dialog() {
//            @Override
//            public void onClick(int pos) {
//                final CharSequence[] dialogItem = {"Hapus","Edit"};
//                AlertDialog.Builder dialog = new AlertDialog.Builder(mainmenu.this);
//                dialog.setItems(dialogItem, new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialogInterface, int i) {
//                        switch (i){
//                            case 0:
//                                delete(list.get(pos).getId());
//                        }
//                    }
//                });
//            }
//            });

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.VERTICAL, false);
        DividerItemDecoration decoration = new DividerItemDecoration(getApplicationContext(),DividerItemDecoration.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(decoration);
        recyclerView.setAdapter(nftAdapter);





        btnadd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            add(view);
            }
        });
        getdata();
    }
    public void getdata(){
        db.collection("NFT").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                list.clear();
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Log.d("suc", document.getId() + " => " + document.getData());
                        User nft = new User(document.getString("Name"), document.getLong("Price"), document.getString("Owner"), document.getString("Image"));
                        nft.setId(document.getId());
                        list.add(nft);
                    }
                    nftAdapter.notifyDataSetChanged();
                } else {
                    Log.d("ggl", "Error getting documents: ", task.getException());
                }
            }
        });
    }
    public void add(View view){
        Intent k = new Intent(this,Add.class);
        startActivity(k);
    }

    private void deleteData(String id){
        String kun = akun.getEmail();
        //buying
        db.collection("Users").whereEqualTo("Email",kun).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {

                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Log.d("weee", document.getId() + " => " + document.getData().get("Name"));
                        String Nem;

                        Nem = (String) document.getData().get("Name");
                        Log.d("owner", "onComplete: "+Nem);
                        setOwner(Nem);

//                        savedata(Name,Nem,image);
//                        user.setText(Name);
                        db.collection("NFT").document(id).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if (task.isSuccessful()) {
                                    DocumentSnapshot document = task.getResult();
                                    String Name;
                                    String image;
                                    String ownerlama;
                                    Name = (String) document.getData().get("Name");
                                    image = (String) document.getData().get("Image");
                                    ownerlama = (String) document.getData().get("Owner");
                                    setLama(ownerlama);
                                    Log.d("lama", "onComplete: "+ownerlama);
                                    Log.d("wuheee", "onComplete: "+document.getData().get("Image"));
                                    savedata(Name,owner,image);
                                    hapus(id);

                                } else {
                                    Log.d("ggl", "Error getting documents: ", task.getException());
                                }
                            }
                        });
                    }

                } else {
                    Log.d("ggl", "Error getting documents: ", task.getException());
                }
            }
        });

        //delete


    }

public void hapus(String id){
            db.collection("NFT").document(id)
                .delete()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
//                            StorageReference storageRef = storage.getReference();
//                            StorageReference desertRef = storageRef.child("image");
//
//                            desertRef.delete();
                            Toast.makeText(getApplicationContext(), "Buying Success", Toast.LENGTH_SHORT).show();
                        }

                        getdata();
                    }
                });
}

    public void savedata(String nama, String owner, String avatar){
        Map<String, Object> nft = new HashMap<>();
        nft.put("Name", nama);
        nft.put("Image", avatar);
        db.collection(owner)
                .add(nft)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {

                        Toast.makeText(getApplicationContext(),"Buying Success", Toast.LENGTH_SHORT).show();
                        Intent k = new Intent(mainmenu.this,mainmenu.class);
                        startActivity(k);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(),e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public void savedata2(String nama, long price, String owner, String avatar){

    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getLama() {
        return lama;
    }

    public void setLama(String lama) {
        this.lama = lama;
    }
}