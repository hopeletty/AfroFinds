package com.example.afrofinds;

import android.app.Activity;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.afrofinds.Models.Service;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class ItemActivity extends AppCompatActivity {
    TextView itemName, itemCuisine, itemDescription, findMap, wishlist;
    ImageView itemImage, menu1, menu2, menu3, menu4;
    Button callButton, likeBtn;
    FloatingActionButton backBtn;
    String url = "https://afrofinds-93455-default-rtdb.asia-southeast1.firebasedatabase.app";
    String url1 = "https://afrofinds-93455-default-rtdb.asia-southeast1.firebasedatabase.app";
    String url2 = "https://afrofinds-93455-default-rtdb.asia-southeast1.firebasedatabase.app";
    String url3 = "https://afrofinds-93455-default-rtdb.asia-southeast1.firebasedatabase.app";
    String url4 = "https://afrofinds-93455-default-rtdb.asia-southeast1.firebasedatabase.app";
    //             https://afrofinds-93455-default-rtdb.asia-southeast1.firebasedatabase.app

    String restaurantName = "";
   // private FirebaseAuth authProfile;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item);

        //authProfile = FirebaseAuth.getInstance();

        itemName = findViewById(R.id.itemName);
        itemDescription = findViewById(R.id.itemDescription);
        itemCuisine = findViewById(R.id.itemCuisine );
        itemImage = findViewById(R.id.itemImage);
        menu1 = findViewById(R.id.menu1);
        menu2 = findViewById(R.id.menu2);
        menu3= findViewById(R.id.menu3);
        menu4 = findViewById(R.id.menu4);
        callButton = findViewById(R.id.callButton);
        backBtn = findViewById(R.id.backBtn);
        findMap = findViewById(R.id.findMap);
        likeBtn = findViewById(R.id.likeBtn);
        wishlist = findViewById(R.id.wishlist);

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        String id = extras.getString("id");

        //latitude.setLatitude(service.getLatitude());

        //String id = getIntent().getStringExtra("id");

        DatabaseReference databaseReference = FirebaseDatabase.getInstance(url).getReference("Restaurants");
        databaseReference.child(id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
              //  String itemUrl = getIntent().getStringExtra("url");

                //for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                restaurantName = dataSnapshot.child("name").getValue().toString();
                String name = dataSnapshot.child("name").getValue().toString();
                String cuisine = dataSnapshot.child("cuisine").getValue().toString();
                String description = dataSnapshot.child("description").getValue().toString();
                url = dataSnapshot.child("img").getValue().toString();
                url1 = dataSnapshot.child("menu1").getValue().toString();
                url2 = dataSnapshot.child("menu2").getValue().toString();
                url3 = dataSnapshot.child("menu3").getValue().toString();
                url4 = dataSnapshot.child("menu4").getValue().toString();
                String phone = dataSnapshot.child("telephone").getValue().toString();
               // String id = dataSnapshot.child("id").getValue().toString();

                itemName.setText(name);
                itemCuisine.setText(cuisine);
                itemDescription.setText(description);
                Picasso.get().load(url).into(itemImage);
                Picasso.get().load(url1).into(menu1);
                Picasso.get().load(url2).into(menu2);
                Picasso.get().load(url3).into(menu3);
                Picasso.get().load(url4).into(menu4);

                callButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //String call = "telephone:"+phone.trim();
                        String call = "tel:"+phone.trim();
                        Intent intent = new Intent(Intent.ACTION_DIAL);
                        intent.setData(Uri.parse(call));
                        startActivity(intent);
                    }
                });

                backBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        startActivity(new Intent(ItemActivity.this, RestaurantList.class));
                    }
                });
                wishlist.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        startActivity(new Intent(ItemActivity.this, LikedList.class));
                    }
                });

                findMap.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Double latitude = dataSnapshot.child("latitude").getValue(Double.class);
                        Double longitude = dataSnapshot.child("longitude").getValue(Double.class);

                     //   latitude.setLatitude(service.getLatitude());
                        Intent intent = new Intent(ItemActivity.this, MapsActivity.class);
                        intent.putExtra("name", name);
                        intent.putExtra("latitude", latitude);
                        intent.putExtra("longitude", longitude);
                        startActivity(intent);
                        //finish();
                    }
                });

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        likeBtn.setOnClickListener(new View.OnClickListener (){

            @Override
            public void onClick(View view) {
            //    FirebaseAuth authProfile = FirebaseAuth.getInstance();
                Context context = getApplicationContext();
                SharedPreferences sharedPref = context.getSharedPreferences("afrofinds_settings", Context.MODE_PRIVATE);
                boolean signedIn = sharedPref.getBoolean("signedIn", false);

                if(!signedIn){
                    Toast.makeText(ItemActivity.this, "Please sign in!", Toast.LENGTH_SHORT).show();
                    Intent i =  new Intent(ItemActivity.this, LoginActivity.class);
                    i.putExtra("src", "itemActivity");
                    startActivity(i);
                }
                else{
                    String userId = sharedPref.getString("loginName", "1" );
                    //String restaurantName = sharedPref.getString("itemName", "1" );
                    ItemActivity.addToFavorite(getApplicationContext(), userId, restaurantName, itemImage);

                }
            }

        });

    }

    public static String getCDateTime(){
        Calendar  calendar = Calendar.getInstance();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd_HHmmss");
        String dateTime = simpleDateFormat.format(calendar.getTime()).toString();
        return dateTime;
    }

    public static void addToFavorite(Context context, String userid, String restaurantName, ImageView itemImage) {
        //   FirebaseAuth authProfile = FirebaseAuth.getInstance();
        //setup data to add in firebase db of current user faves
        String url = "https://afrofinds-93455-default-rtdb.asia-southeast1.firebasedatabase.app";
        DatabaseReference databaseReference = FirebaseDatabase.getInstance(url).getReference("Restaurants");
        Picasso.get().load(url).into(itemImage);
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put(getCDateTime() , restaurantName);
        hashMap.put(getCDateTime() , itemImage);
        //hashMap.put("itemName", "" + restaurantName);
        //  hashMap.put("name", "");
        //  hashMap.put("img", ""+itemImage);
        //  hashMap.put("description", ""+itemDescription);

        databaseReference = LoginActivity.databaseReference.child("Users/" + userid + "/Favorites");
        //databaseReference = LoginActivity.databaseReference.child("Users/"+restaurantName+"/Favorites");

        //("https://afrofinds-93455-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference();
        databaseReference.setValue(hashMap, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                if (databaseError != null) {
                    System.out.println("Data could not be saved " + databaseError.getMessage());
                } else {
                    System.out.println("Data saved successfully.");
                }
            }
        });

    }

}