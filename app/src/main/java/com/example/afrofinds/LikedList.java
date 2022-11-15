package com.example.afrofinds;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.afrofinds.Models.Favorites;
import com.example.afrofinds.Models.Service;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class LikedList extends Fragment {

    RecyclerView recyclerView;
    ArrayList<Favorites> wishList;
    LikedAdapter likedAdapter;
    Button deleteBtn;

   /* Intent intent = new Intent();
    Bundle extras = intent.getExtras();
    String id = extras.getString("id");*/

    boolean isInList = false;

    static String itemName = "";
    static String userId = "";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //  return inflater.inflate(R.layout.fragment_like, container, false);
        View view = inflater.inflate(R.layout.fragment_like, container, false);


        SharedPreferences sharedPref = getContext().getSharedPreferences("afrofinds_settings", Context.MODE_PRIVATE);
        userId = sharedPref.getString("loginName", "0");

        String url = "https://afrofinds-93455-default-rtdb.asia-southeast1.firebasedatabase.app/";

        DatabaseReference databaseReference = FirebaseDatabase.getInstance(url).getReference("Users/"+userId+"/Favorites");

        deleteBtn = view.findViewById(R.id.deleteBtn);

        recyclerView = view.findViewById(R.id.likedList);
        //recyclerView = recyclerView.findViewById(R.id.likedList);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        wishList = new ArrayList<>();
        likedAdapter = new LikedAdapter(getContext(), wishList);
        recyclerView.setAdapter(likedAdapter);

        databaseReference.addValueEventListener(new ValueEventListener() {
            //databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                    Favorites favorites = new Favorites();
                    favorites.setItemName((String)dataSnapshot.getValue());
                    favorites.setImg("test");
                    //dataSnapshot.getValue(Favorites.class);
                    //Favorites favorites = dataSnapshot.getValue(Favorites.class);
                    wishList.add(favorites);
                    //     String key = databaseReference.push().getKey();
                }
                likedAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("test", error.getMessage());

            }
        });

        //String restaurantName = sharedPref.getString("itemName", "1" );
        /*
        databaseReference.addValueEventListener(new ValueEventListener() {

            //databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Favorites favorites = dataSnapshot.getValue(Favorites.class);
                    wishList.add(favorites);
                    //     String key = databaseReference.push().getKey();
                }
                likedAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("test", error.getMessage());

            }
        });
        */
        return view;
    }

/*        deleteBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Context context = getContext();
                LikedList.removeFromFavorite(getContext(), userId,itemName);
                }
        });
        return view;*/

    public static void removeFromFavorite(Context context, String userid, String itemName) {
        //setup data to add in firebase db of current user faves
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("itemName", "" + itemName);

        //save to db
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference( "Users/" + userid + "/Favorites");
        //databaseReference.setValue(hashMap, new DatabaseReference() {
       // databaseReference.child(authProfile.getUid()).child("Favorites").child(id)
              //  .setValue(hashMap)
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

    class LikedViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        ImageView itemImage;

        public LikedViewHolder(@NonNull View itemView) {
            super(itemView);
        }

        public void setDetails(String itemName, String itemDescription, String userImage) {
            name = itemView.findViewById(R.id.name);
            itemImage = itemView.findViewById(R.id.itemImage);

        }

    }
}
