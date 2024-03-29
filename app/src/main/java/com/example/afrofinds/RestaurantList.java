package com.example.afrofinds;

import static java.security.AccessController.getContext;

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

import com.example.afrofinds.Models.Service;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class RestaurantList extends AppCompatActivity {
    RecyclerView recyclerView;
    RestaurantAdapter restaurantAdapter;
    ArrayList<Service> list;
    SearchView searchView;
    FloatingActionButton backBtn;

    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurantlist);

        String url = "https://afrofinds-93455-default-rtdb.asia-southeast1.firebasedatabase.app";
        databaseReference = FirebaseDatabase.getInstance(url).getReference("Restaurants");

        recyclerView = findViewById(R.id.restaurantlist);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        list = new ArrayList<>();
        restaurantAdapter = new RestaurantAdapter(this, list);
        recyclerView.setAdapter(restaurantAdapter);

        backBtn = findViewById(R.id.backBtn);


        databaseReference.addValueEventListener(new ValueEventListener() {
            //databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Service service = dataSnapshot.getValue(Service.class);
                    list.add(service);
               //     String key = databaseReference.push().getKey();
                }
                restaurantAdapter.notifyDataSetChanged();
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
        ArrayList<Service> searchList = new ArrayList<>();
        for (int i=0; i<list.size(); i++) {

            Service service = list.get(i);
            if (service.getName().toLowerCase().contains(s.toLowerCase()) ||
                    service.getCuisine().toLowerCase().contains(s.toLowerCase())) {
                searchList.add(service);
            }
        }

        restaurantAdapter = new RestaurantAdapter(getApplicationContext(), searchList);
        restaurantAdapter.notifyDataSetChanged();
        recyclerView.setAdapter(restaurantAdapter);
    }

    class ServiceViewHolder extends RecyclerView.ViewHolder {
        TextView name, description, cuisine;
        ImageView service_image;

        public ServiceViewHolder(@NonNull View itemView) {
            super(itemView);
        }
        public void setDetails(String itemName,String itemDescription, String userImage){
            name = itemView.findViewById(R.id.name);
            description = itemView.findViewById(R.id.description);
            cuisine = itemView.findViewById(R.id.cuisine);
            service_image = itemView.findViewById(R.id.service_image);
          //  backBtn = itemView.findViewById(R.id.backBtn);

        }

    }
}


