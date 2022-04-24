package com.example.nft;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.example.nft.Adapter.NftAdapter;
import com.example.nft.Model.User;
import com.example.nft.databinding.ActivityGalleryBinding;
import com.example.nft.databinding.ActivityMainmenuBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;
import java.util.List;

public class gallery extends AppCompatActivity {
    ActivityGalleryBinding binding;
    private RecyclerView recyclerView;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseStorage storage = FirebaseStorage.getInstance();
    private List<User> list = new ArrayList<>();
    private NftAdapter nftAdapter;
    private FirebaseUser account = FirebaseAuth.getInstance().getCurrentUser();
    public String acc = account.getEmail();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);
        getSupportActionBar().setTitle("My Resources");


        binding = ActivityGalleryBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.bottom.setOnItemSelectedListener(item -> {
            switch (item.getItemId()){
                case R.id.home:
                    startActivity(new Intent(gallery.this, mainmenu.class));
                    break;
                case R.id.prof:
                    startActivity(new Intent(gallery.this, profile.class));
                    break;
                case R.id.gall:
                    break;
            }return true;
        });
        recyclerView = findViewById(R.id.recycler);
        nftAdapter = new NftAdapter(getApplicationContext(),list);
        nftAdapter.setDialog(new NftAdapter.Dialog() {
            @Override
            public void onClick(int pos) {
                pind(pos);

            }
        });
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(gridLayoutManager);
        DividerItemDecoration decoration = new DividerItemDecoration(getApplicationContext(),DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(decoration);
        recyclerView.setAdapter(nftAdapter);
        hist();
    }

    public void hist(){
        db.collection("Users").whereEqualTo("Email",acc).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()){
                    for (QueryDocumentSnapshot document : task.getResult()){
                        Log.d("punten", document.getId() + " => " + document.getData().get("Name"));
                        String name = (String) document.getData().get("Name");
                        getdata(name);
                    }
                }
            }
        });
    }



    public void getdata(String name){
        db.collection("NFT").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                list.clear();
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        if (name.equals(document.getString("Owner")) && document.getString("Status").equals("Sold")){
                            com.example.nft.Model.User nft = new User(document.getString("Name"), document.getLong("Price"), document.getString("Owner"), document.getString("Image"));
                            nft.setId(document.getId());
                            list.add(nft);
                        }
                    }
                    nftAdapter.notifyDataSetChanged();
                } else {
                    Log.d("ggl", "Error getting documents: ", task.getException());
                }
            }
        });
    }
    void pind(int pos){
        //Bitmap bmp = Glide.with(getApplicationContext()).asBitmap().load(uri);
        Intent intent = new Intent(this, detcoll.class);
        intent.putExtra("namea",list.get(pos).getName());
        intent.putExtra("owner",list.get(pos).getOwner().toString());
        intent.putExtra("pri",list.get(pos).getPrice().toString());
        intent.putExtra("pict",list.get(pos).getAvatar());
        intent.putExtra("ID", list.get(pos).getId());
        //intent.putExtra("pos23", pos);
        startActivity(intent);
    }
}