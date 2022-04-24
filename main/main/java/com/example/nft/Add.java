package com.example.nft;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.DatabaseErrorHandler;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Add extends AppCompatActivity {
    private EditText nama,price;
    private Button save;
    private ImageView avatar;
    private FirebaseUser akun =  FirebaseAuth.getInstance().getCurrentUser();;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
//    akun = FirebaseAuth.getInstance().getCurrentUser();
    public String kun = akun.getEmail();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        getSupportActionBar().hide();
        nama = findViewById(R.id.name);
        price = findViewById(R.id.email);
        save = findViewById(R.id.btn_save);
        avatar = findViewById(R.id.avatar);
        avatar.setOnClickListener(v ->{
            image();
        });


        save.setOnClickListener(view -> {
            String owner;
            long priceg=0;
        String name = nama.getText().toString();
        String price1 = price.getText().toString();
        if (TextUtils.isEmpty(name)){
            Toast.makeText(getApplicationContext(),"Please enter name!", Toast.LENGTH_SHORT).show();
//            priceg.set(Integer.parseInt(price1));
        }
        if (TextUtils.isEmpty(price1)){
            Toast.makeText(getApplicationContext(),"Please enter price!", Toast.LENGTH_SHORT).show();
//            priceg.set(Integer.parseInt(price1));
        }
        else{
            db.collection("Users").whereEqualTo("Email",kun).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @SuppressLint("NotifyDataSetChanged")
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                String ki;
                    String owner;
                    long priceg=0;
                    String name = nama.getText().toString();
                    String price1 = price.getText().toString();
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Log.d("weee", document.getId() + " => " + document.getData().get("Name"));
                            String Name;
                            Long eth;
                            Name = (String) document.getData().get("Name");
                            eth = (Long) document.getData().get("Eth");
                            owner = Name;
                            priceg = Integer.parseInt(price1);
                            imagek(name,priceg,owner);
                        }

                    } else {
                        Log.d("ggl", "Error getting documents: ", task.getException());
                    }
                }
            });
//            Log.d("kkk", "onComplete: " +owner);
//            priceg = Integer.parseInt(price1);
//            savedata(name,priceg,owner);
        }

        });

    }

//    public void User(String name){
//        this.owner = name;
//    }
//    public void add()
//    {
//        AtomicInteger priceg= new AtomicInteger();
//        String name = nama.getText().toString();
//        String price1 = price.getText().toString();
//        if (TextUtils.isEmpty(name)){
//            Toast.makeText(Add.this,"Please enter name!", Toast.LENGTH_SHORT).show();
//            priceg.set(Integer.parseInt(price1));
//        }
//        if (TextUtils.isEmpty(price1)){
//            Toast.makeText(Add.this,"Please enter price!", Toast.LENGTH_SHORT).show();
//            priceg.set(Integer.parseInt(price1));
//        }
//        else{
//            Toast.makeText(Add.this,"Please enter price!", Toast.LENGTH_SHORT).show();
////            priceg.set(Integer.parseInt(price1));
////            savedata(name,priceg);
//        }
//    }

    private void imagek(String name1, long price2, String own){
        avatar.setDrawingCacheEnabled(true);
        avatar.buildDrawingCache();
        Bitmap bitmap = ((BitmapDrawable) avatar.getDrawable()).getBitmap();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();

        //upload
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference reference = storage.getReference("images").child("IMG"+new Date().getTime()+".jpeg");

        UploadTask uploadTask = reference.putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                if (taskSnapshot.getMetadata()!=null){
                    taskSnapshot.getMetadata().getReference().getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull Task<Uri> task) {
                        if (task.getResult()!=null){
                                savedata(name1, price2, own, task.getResult().toString());
                            }else {
                            Toast.makeText(getApplicationContext(), "Failed", Toast.LENGTH_SHORT).show();
                        }
                        }
                    });
                }else{
                    Toast.makeText(getApplicationContext(), "Failed", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void savedata(String nama, long price, String owner, String avatar){
        Map<String, Object> nft = new HashMap<>();
        nft.put("Name", nama);
        nft.put("Price", price);
        nft.put("Owner", owner);
        nft.put("Image", avatar);

        db.collection("NFT")
                .add(nft)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Toast.makeText(getApplicationContext(),"Success to Add your NFT", Toast.LENGTH_SHORT).show();
                        Intent k = new Intent(Add.this,mainmenu.class);
                        startActivity(k);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(),"Failed to Add your NFT", Toast.LENGTH_SHORT).show();
                    }
                });
    }
    private void image(){
        final CharSequence[] items = {"Take Photo", "Choose from Library", "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(Add.this);
        builder.setTitle(getString(R.string.app_name));
        builder.setIcon(R.mipmap.ic_launcher);
        builder.setItems(items, (dialog, item)->{
            if (items[item].equals("Take Photo")){
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, 10);
            }
            else if(items[item].equals("Choose from Library")){
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
               startActivityForResult(Intent.createChooser(intent, "Select Image"),20);
            }
            else if(items[item].equals("Cancel")){
                dialog.dismiss();
            }
        });
        builder.show();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data){
        super.onActivityResult(requestCode,resultCode,data);
        if (requestCode == 20 && resultCode == RESULT_OK && data != null){
            final Uri path = data.getData();
            Thread thread = new Thread(() ->{
                try {
                    InputStream inputStream = getContentResolver().openInputStream(path);
                    Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                    avatar.post(() ->{
                        avatar.setImageBitmap(bitmap);
                    });
                }
                catch (IOException e){
                    e.printStackTrace();
                }
            });
            thread.start();
        }

        if (requestCode == 10 && resultCode == RESULT_OK){
            final Bundle extras = data.getExtras();
            Thread thread = new Thread(()->
            {
                Bitmap bitmap = (Bitmap) extras.get("data");
                avatar.post(() ->{
                    avatar.setImageBitmap(bitmap);
                });
            });
            thread.start();
        }
    }

}