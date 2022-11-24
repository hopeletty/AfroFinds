package com.example.afrofinds;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class CommunityActivity extends AppCompatActivity {
    TextView comCountry, comEmbassy, comGroup, findMap;
    ImageView comImage;
    Button callButton, likeBtn;
    FloatingActionButton backBtn;
    String url = "https://afrofinds-93455-default-rtdb.asia-southeast1.firebasedatabase.app";

    String countryName = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.community_details);

        comCountry = findViewById(R.id.comCountry);
        comEmbassy = findViewById(R.id.comEmbassy);
        comGroup = findViewById(R.id.comGroup);
        comImage = findViewById(R.id.comImage);
        callButton = findViewById(R.id.callButton);
        backBtn = findViewById(R.id.backBtn);
        findMap = findViewById(R.id.findMap);
        likeBtn = findViewById(R.id.likeBtn);
        //wishlist = findViewById(R.id.wishlist);

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        String id = extras.getString("id");


        DatabaseReference databaseReference = FirebaseDatabase.getInstance(url).getReference("Community");
        databaseReference.child(id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                countryName = dataSnapshot.child("country").getValue().toString();
                String name = dataSnapshot.child("country").getValue().toString();
                String embassy = dataSnapshot.child("embassy").getValue().toString();
//                String group = dataSnapshot.child("group").getValue().toString();
                url = dataSnapshot.child("img").getValue().toString();

//                String phone = dataSnapshot.child("telephone").getValue().toString();
                // String id = dataSnapshot.child("id").getValue().toString();

                comCountry.setText(name);
                comEmbassy.setText(embassy);
                Picasso.get().load(url).into(comImage);
              //  comGroup.setText(group);

               /* callButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //String call = "telephone:"+phone.trim();
                        String call = "tel:"+phone.trim();
                        Intent intent = new Intent(Intent.ACTION_DIAL);
                        intent.setData(Uri.parse(call));
                        startActivity(intent);
                    }
                });*/

                backBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        startActivity(new Intent(CommunityActivity.this, CommunityList.class));
                    }
                });

                /*findMap.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Double latitude = dataSnapshot.child("latitude").getValue(Double.class);
                        Double longitude = dataSnapshot.child("longitude").getValue(Double.class);

                        //   latitude.setLatitude(service.getLatitude());
                        Intent intent = new Intent(CommunityActivity.this, MapsActivity.class);
                        intent.putExtra("name", name);
                        intent.putExtra("latitude", latitude);
                        intent.putExtra("longitude", longitude);
                        startActivity(intent);
                        //finish();
                    }
                });*/

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
                    Toast.makeText(CommunityActivity.this, "Please sign in!", Toast.LENGTH_SHORT).show();
                    Intent i =  new Intent(CommunityActivity.this, LoginActivity.class);
                    i.putExtra("src", "communityActivity");
                    startActivity(i);
                }
                else{
                    String userId = sharedPref.getString("loginName", "1" );
                    CommunityActivity.addToFavorite(getApplicationContext(), userId, countryName);

                }
            }

        });

    }

    public static String getCDateTime(){
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd_HHmmss");
        String dateTime = simpleDateFormat.format(calendar.getTime()).toString();
        return dateTime;
    }

    public static void addToFavorite(Context context, String userid, String countryName) {
        //   FirebaseAuth authProfile = FirebaseAuth.getInstance();
        //setup data to add in firebase db of current user faves
        String url = "https://afrofinds-93455-default-rtdb.asia-southeast1.firebasedatabase.app";
        DatabaseReference databaseReference = FirebaseDatabase.getInstance(url).getReference("Restaurants");
       // Picasso.get().load(url).into(itemImage);
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put(getCDateTime() ,countryName);
  //      hashMap.put(getCDateTime() , itemImage);
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
