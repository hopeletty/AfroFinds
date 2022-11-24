package com.example.afrofinds;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.afrofinds.Models.Community;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class CommunityList extends AppCompatActivity {
    RecyclerView recyclerView;
    CommunityAdapter communityAdapter;
    ArrayList<Community> communityList;
    SearchView searchView;
    FloatingActionButton backBtn;

    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_communitylist);

        String url = "https://afrofinds-93455-default-rtdb.asia-southeast1.firebasedatabase.app";
        databaseReference = FirebaseDatabase.getInstance(url).getReference("Community");

        recyclerView = findViewById(R.id.communitylist);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        communityList = new ArrayList<>();
        communityAdapter = new CommunityAdapter(this, communityList);
        recyclerView.setAdapter(communityAdapter);

        backBtn = findViewById(R.id.backBtn);


        databaseReference.addValueEventListener(new ValueEventListener() {
            //databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Community community = dataSnapshot.getValue(Community.class);
                    communityList.add(community);
                    //     String key = databaseReference.push().getKey();
                }
                communityAdapter.notifyDataSetChanged();
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
     /*   backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RestaurantList.this, DashboardFragment.class));
            }
        });*/
    }

    private void searchItem(final String s) {
        // final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        ArrayList<Community> searchList = new ArrayList<>();
        for (int i=0; i<communityList.size(); i++) {

            Community community = communityList.get(i);
            if (community.getCountry().toLowerCase().contains(s.toLowerCase())) {
                searchList.add(community);
            }
        }

        communityAdapter = new CommunityAdapter(getApplicationContext(), searchList);
        communityAdapter.notifyDataSetChanged();
        recyclerView.setAdapter(communityAdapter);
    }

    class CommunityViewHolder extends RecyclerView.ViewHolder {
        TextView country, embassy;
        ImageView community_image;

        public CommunityViewHolder(@NonNull View itemView) {
            super(itemView);
            country = itemView.findViewById(R.id.country);
            embassy = itemView.findViewById(R.id.embassy);
            community_image = itemView.findViewById(R.id.community_image);
            searchView = itemView.findViewById(R.id.search_view_home);
        }
    }
}


