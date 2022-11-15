package com.example.afrofinds;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.afrofinds.Models.Favorites;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ProfileFragment extends Fragment {
    private FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    ImageView avatar;
    TextView phoneNumber, userEmail, userName;
    FloatingActionButton editBtn;

    public ProfileFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_profile, container, false);
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        String url = "https://afrofinds-93455-default-rtdb.asia-southeast1.firebasedatabase.app";
        databaseReference = FirebaseDatabase.getInstance(url).getReference("Users");


        // Initialising the text view and imageview
        avatar = view.findViewById(R.id.userImage);
        userName = view.findViewById(R.id.userName);
        userEmail = view.findViewById(R.id.userEmail);
        phoneNumber = view.findViewById(R.id.phoneNumber);
     //   editBtn = view.findViewById(R.id.editBtn);


        Query query = databaseReference.orderByChild("email");
        //.equalTo(firebaseUser.getEmail());

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                Context context = getActivity();
                SharedPreferences sharedPref = context.getSharedPreferences("afrofinds_settings", Context.MODE_PRIVATE);
                boolean signedIn = sharedPref.getBoolean("signedIn", false);

                if(!signedIn){
                    Toast.makeText(context, "Please sign in!", Toast.LENGTH_SHORT).show();
                    Intent i =  new Intent(context, LoginActivity.class);
                    i.putExtra("src", "profileFragment");
                    startActivity(i);
                }

                else {
                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                        // Retrieving Data from firebase
                        String name = "" + dataSnapshot1.child("username").getValue();
                        String mail = "" + dataSnapshot1.child("email").getValue();
                        String contact = "" + dataSnapshot1.child("PhoneNumber").getValue();
                        //String image = "" + dataSnapshot1.child("image").getValue();
                        // setting data to our text view
                        userName.setText(name);
                        userEmail.setText(mail);
                        phoneNumber.setText(contact);
                        // try {
                        //   Glide.with(getActivity()).load(image).into(userImage);
                        //} catch (Exception e) {

                        //}
                    }
                }
                /*
                if (firebaseAuth.getCurrentUser() == null){
                    Toast.makeText(getContext(), "Please sign in!", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getContext(), LoginActivity.class));
                }

                else {
                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                        // Retrieving Data from firebase
                        String name = "" + dataSnapshot1.child("username").getValue();
                        String mail = "" + dataSnapshot1.child("email").getValue();
                        String contact = "" + dataSnapshot1.child("PhoneNumber").getValue();
                        //String image = "" + dataSnapshot1.child("image").getValue();
                        // setting data to our text view
                        userName.setText(name);
                        userEmail.setText(mail);
                        phoneNumber.setText(contact);
                        // try {
                        //   Glide.with(getActivity()).load(image).into(userImage);
                        //} catch (Exception e) {

                        //}
                    }
                }
                */
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }

        });

        /* On click we will open EditProfileActiity
        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), EditProfilePage.class));
            }
        });*/


        Button logout = (Button) view.findViewById(R.id.logout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                getActivity().finishAffinity();
                startActivity(intent);


            }
        });
        return view;
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
    }

}
