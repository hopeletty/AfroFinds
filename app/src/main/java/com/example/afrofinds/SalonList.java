package com.example.afrofinds;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.afrofinds.Models.Salon;
import com.example.afrofinds.Models.Service;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class SalonList extends AppCompatActivity {
    RecyclerView recyclerView;
    SalonAdapter salonAdapter;
    ArrayList<Salon> salonList;
    SearchView searchView;

    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_salonlist);

        String url = "https://afrofinds-93455-default-rtdb.asia-southeast1.firebasedatabase.app";
        databaseReference = FirebaseDatabase.getInstance(url).getReference("Salons");

        recyclerView = findViewById(R.id.salonlist);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        salonList = new ArrayList<>();
        salonAdapter = new SalonAdapter(this, salonList);
        recyclerView.setAdapter(salonAdapter);


        databaseReference.addValueEventListener(new ValueEventListener() {
            //databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Salon salon = dataSnapshot.getValue(Salon.class);
                    salonList.add(salon);
                }
                salonAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("test", error.getMessage());

            }
        });

        searchView = findViewById(R.id.search_view_home);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (!TextUtils.isEmpty(query.trim())) {
                    searchItem(query);
                }
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (!TextUtils.isEmpty(newText.trim())) {
                    searchItem(newText);
                }
                return false;
            }
        });
    }

    @SuppressLint("NotifyDataSetChanged")
    private void searchItem(final String s) {
        // final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        ArrayList<Salon> searchList = new ArrayList<>();
        for (int i=0; i<salonList.size(); i++) {

            Salon salon = salonList.get(i);
            if (salon.getName().toLowerCase().contains(s.toLowerCase())) {
                searchList.add(salon);
            }
        }

        salonAdapter = new SalonAdapter(getApplicationContext(), searchList);
        salonAdapter.notifyDataSetChanged();
        recyclerView.setAdapter(salonAdapter);
    }

    class SalonViewHolder extends RecyclerView.ViewHolder {
        TextView name, itemWeb;
        ImageView salon_image;

        public SalonViewHolder(@NonNull View itemView) {
            super(itemView);
        }
        public void setDetails(String itemName, String userImage){
            name = itemView.findViewById(R.id.name);
            itemWeb = itemView.findViewById(R.id.itemWeb);

            salon_image = itemView.findViewById(R.id.salon_image);

        }

    }
}


