package com.example.afrofinds;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

public class SalonActivity extends AppCompatActivity {
    TextView itemName, itemWeb, findMap;
    ImageView itemImage, style1, style2;
    Button callButton, likeBtn;
    String url = "https://afrofinds-93455-default-rtdb.asia-southeast1.firebasedatabase.app";
    String url1 = "https://afrofinds-93455-default-rtdb.asia-southeast1.firebasedatabase.app";
    String url2 = "https://afrofinds-93455-default-rtdb.asia-southeast1.firebasedatabase.app";


    // private FirebaseAuth authProfile;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_salon_item);

        //authProfile = FirebaseAuth.getInstance();

        itemName = findViewById(R.id.itemName);
        itemImage = findViewById(R.id.itemImage);
        itemWeb = findViewById(R.id.itemWeb);
        style1 = findViewById(R.id.style1);
        style2 = findViewById(R.id.style2);
        callButton = findViewById(R.id.callButton);
        findMap = findViewById(R.id.findMap);
        likeBtn = findViewById(R.id.likeBtn);

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        String id = extras.getString("id");


        DatabaseReference databaseReference = FirebaseDatabase.getInstance(url).getReference("Salons");
        databaseReference.child(id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //  String itemUrl = getIntent().getStringExtra("url");

                //for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                String name = dataSnapshot.child("name").getValue().toString();
                String webPage = dataSnapshot.child("webPage").getValue().toString();
                url = dataSnapshot.child("img").getValue().toString();
                url1 = dataSnapshot.child("style1").getValue().toString();
                url2 = dataSnapshot.child("style2").getValue().toString();

                String phone = dataSnapshot.child("phoneNumber").getValue().toString();
                // String id = dataSnapshot.child("id").getValue().toString();

                itemName.setText(name);
                itemWeb.setText(webPage);
                Picasso.get().load(url).into(itemImage);
                Picasso.get().load(url1).into(style1);
                Picasso.get().load(url2).into(style2);

                itemWeb.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String webPage = "webPage" + itemWeb;
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        intent.setData(Uri.parse(webPage));
                        if (intent.resolveActivity(getPackageManager()) != null) {
                            startActivity(intent);
                        }

                    }
                });


                callButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String call = "phoneNumber" + phone.trim();
                        Intent intent = new Intent(Intent.ACTION_DIAL);
                        intent.setData(Uri.parse(call));
                        startActivity(intent);
                    }
                });

                findMap.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Double latitude = dataSnapshot.child("latitude").getValue(Double.class);
                        Double longitude = dataSnapshot.child("longitude").getValue(Double.class);

                        //   latitude.setLatitude(service.getLatitude());
                        Intent intent = new Intent(getApplicationContext(), MapsActivity.class);
                        intent.putExtra("name", name);
                        intent.putExtra("latitude", latitude);
                        intent.putExtra("longitude", longitude);
                        startActivity(intent);
                        finish();
                    }
                });

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        likeBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                FirebaseAuth authProfile = FirebaseAuth.getInstance();

                if (authProfile.getCurrentUser() == null) {
                    Toast.makeText(SalonActivity.this, "Please sign in!", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(SalonActivity.this, LoginActivity.class));
                } else {
                    SalonActivity.addToFavorite(getApplicationContext(), id);

                }
            }

        });

    }

    public static void addToFavorite(Context context, String id) {
        FirebaseAuth authProfile = FirebaseAuth.getInstance();
        //setup data to add in firebase db of current user faves
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("id", "" + id);
        //  hashMap.put("name", "");
        //  hashMap.put("img", ""+itemImage);
        //  hashMap.put("description", ""+itemDescription);

        //save to db
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users");
        databaseReference.child(authProfile.getUid()).child("Favorites").child(id)
                .setValue(hashMap)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(context, "Added to wishlist", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(context, "Failed!" + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
