package com.example.afrofinds;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.afrofinds.Models.Service;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class LikedList extends Fragment {
    RecyclerView recyclerView;
    ArrayList<Service> list;
    LikedAdapter LikedAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
      //  return inflater.inflate(R.layout.fragment_like, container, false);
        View view = inflater.inflate(R.layout.fragment_like, container, false);

        String url = "https://afrofinds-93455-default-rtdb.asia-southeast1.firebasedatabase.app/";
        DatabaseReference databaseReference = FirebaseDatabase.getInstance(url).getReference("Restaurants");

        recyclerView = (RecyclerView) view.findViewById(R.id.likedList);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

        list = new ArrayList<>();
        LikedAdapter = new LikedAdapter(getContext(),list);
        recyclerView.setAdapter(LikedAdapter);

        databaseReference.addValueEventListener(new ValueEventListener() {
            //databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Service service = dataSnapshot.getValue(Service.class);
                    list.add(service);
                }
                LikedAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("test", error.getMessage());

            }
        });

        return view;

    }
}