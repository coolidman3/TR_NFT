package com.example.nft;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import java.util.Date;
import static android.os.Environment.DIRECTORY_DOWNLOADS;
public class detcoll extends AppCompatActivity {
    private Button sell, download;
    private ImageView avatar;
    private EditText price, name;
    private FirebaseStorage storage = FirebaseStorage.getInstance();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detcoll);
        sell = findViewById(R.id.sell);
        download = findViewById(R.id.donlod);
        avatar = findViewById(R.id.picnftt);
        price = findViewById(R.id.pricc);
        name = findViewById(R.id.namepicc);

        Intent intent = getIntent();
        Glide.with(this).load(intent.getStringExtra("pict")).into(avatar);
        //long pricee = Long.parseLong(intent.getStringExtra("pri").toString());
        price.setText(intent.getStringExtra("pri").toString());
        name.setText(intent.getStringExtra("namea"));

sell.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        jual(intent.getStringExtra("ID"));
    }
});

download.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        downloadfile(intent.getStringExtra("pict"));
        Log.d("punten", "onSuccess: "+intent.getStringExtra("pict"));
    }
});



    }
private void jual(String id){
        String nama = name.getText().toString();
        Long pricee = (Long) Long.parseLong(price.getText().toString());

db.collection("NFT").document(id).update("Status", "Sale").addOnCompleteListener(new OnCompleteListener<Void>() {
    @Override
    public void onComplete(@NonNull Task<Void> task) {
        Log.d("punten", "ini harga" + pricee);
        Log.d("punten", "ini nama" + nama);
    }
});
    db.collection("NFT").document(id).update("Name", nama).addOnCompleteListener(new OnCompleteListener<Void>() {
        @Override
        public void onComplete(@NonNull Task<Void> task) {

        }
    });
    db.collection("NFT").document(id).update("Price", pricee).addOnCompleteListener(new OnCompleteListener<Void>() {
        @Override
        public void onComplete(@NonNull Task<Void> task) {

        }
    });
startActivity(new Intent(getApplicationContext(), gallery.class));
}
    public void downloadfile(String id){
        StorageReference httpsReference = storage.getReferenceFromUrl(id);

        httpsReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Log.d("uri", "onSuccess: "+uri);
                String url = uri.toString();
                download(getApplicationContext(),"IMG"+new Date().getTime(),".jpeg",DIRECTORY_DOWNLOADS,url);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
            }
        });
    }
    public void download(Context context, String filename, String filext, String destinationdirectory, String url){
        DownloadManager downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        Uri uri = Uri.parse(url);
        DownloadManager.Request request = new DownloadManager.Request(uri);

        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setDestinationInExternalFilesDir(context, destinationdirectory, filename + filext);
        downloadManager.enqueue(request);
    }
}

