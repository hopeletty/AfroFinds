package com.example.afrofinds;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.afrofinds.Models.Service;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class ItemActivity extends AppCompatActivity {
    TextView itemName, itemCuisine, itemDescription, findMap;
    ImageView itemImage, menu1, menu2, menu3, menu4;
    Button callButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item);

        itemName = findViewById(R.id.itemName);
        itemDescription = findViewById(R.id.itemDescription);
        itemCuisine = findViewById(R.id.itemCuisine );
        itemImage = findViewById(R.id.itemImage);
        callButton = findViewById(R.id.callButton);
        findMap = findViewById(R.id.findMap);

        callButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:07088791967"));
                startActivity(intent);
            }
        });

        String url = "https://afrofinds-93455-default-rtdb.asia-southeast1.firebasedatabase.app/";
        DatabaseReference databaseReference = FirebaseDatabase.getInstance(url).getReference("Restaurants");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    String name = dataSnapshot.child("name").getValue().toString();
                    String cuisine = dataSnapshot.child("cuisine").getValue().toString();
                    String description = dataSnapshot.child("description").getValue().toString();
                    String url = dataSnapshot.child("img").getValue().toString();

                    itemName.setText(name);
                    itemCuisine.setText(cuisine);
                    itemDescription.setText(description);
                    Picasso.get().load(url).into(itemImage);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        findMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MapsActivity.class);
                startActivity(intent);
                finish();
            }
        });


    }
}