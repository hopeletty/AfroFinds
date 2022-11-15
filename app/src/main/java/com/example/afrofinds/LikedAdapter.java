package com.example.afrofinds;

import static com.example.afrofinds.LikedList.itemName;
import static com.example.afrofinds.LikedList.userId;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.afrofinds.Models.Favorites;
import com.example.afrofinds.Models.Service;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;

public class LikedAdapter extends RecyclerView.Adapter<LikedAdapter.LikedViewHolder> {
    Context context;
    ArrayList<Favorites> wishList;
    ArrayList<Service> list;

    public LikedAdapter(Context context, ArrayList<Favorites> wishList) {
        this.context = context;
        this.wishList = wishList;
        this.list = list;
    }

    @NonNull
    @Override
    public LikedAdapter.LikedViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.list_liked, parent, false);
        return new LikedAdapter.LikedViewHolder(v);

    }
    String url = "https://afrofinds-93455-default-rtdb.asia-southeast1.firebasedatabase.app";
    DatabaseReference databaseReference = FirebaseDatabase.getInstance(url).getReference("Restaurants");

    @Override
    public void onBindViewHolder(@NonNull LikedViewHolder holder, int position) {
        Favorites favorites = wishList.get(position);
        holder.name.setText(favorites.getItemName());

        url = favorites.getImg();
        Picasso.get().load(url).into(holder.itemImage);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String id = wishList.get(position).getItemName();
                Intent intent = new Intent(context, ItemActivity.class);
                intent.putExtra("id", id);
                context.startActivity(intent);
            }
        });

        holder.deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String itemName = wishList.get(position).getItemName();

                SharedPreferences sharedPref = context.getSharedPreferences("afrofinds_settings", Context.MODE_PRIVATE);
                userId = sharedPref.getString("loginName", "0" );

                LikedList.removeFromFavorite(context, userId,itemName);
            }
        });

        //return view;
    }

    @Override
    public int getItemCount() {
        return wishList.size();
    }


    public class LikedViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        ImageView itemImage;
        Button deleteBtn;

       // Intent intent = getIntent();

     //   private Intent getIntent() {
    //    }

       // Bundle extras = intent.getExtras();
        //String id = extras.getString("id");

        public LikedViewHolder(@NonNull View itemView) {

            super(itemView);
            name = itemView.findViewById(R.id.name);
            itemImage = itemView.findViewById(R.id.itemImage);
            deleteBtn = itemView.findViewById(R.id.deleteBtn);

        }
    }
}
