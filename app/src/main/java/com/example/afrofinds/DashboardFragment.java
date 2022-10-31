package com.example.afrofinds;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.afrofinds.Models.Service;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class DashboardFragment extends Fragment {
    public Button more1;
    SearchView searchView;
    ArrayList<Service> list;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_dashboard, container, false);

        View view = inflater.inflate(R.layout.fragment_dashboard, container, false);
        String url = "https://afrofinds-93455-default-rtdb.asia-southeast1.firebasedatabase.app/";
        DatabaseReference databaseReference = FirebaseDatabase.getInstance(url).getReference("Restaurants");

        searchView = (SearchView) view.findViewById(R.id.search_view_home);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                firebaseUserSearch();


               // dialog.show();
                return true;
            }

            private void firebaseUserSearch() {


            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });



        Button more1 = (Button) view.findViewById(R.id.more1);
        more1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), RestaurantList.class);
                startActivity(intent);
                getActivity().finish();

            }
        });
        return view;



    }
}


