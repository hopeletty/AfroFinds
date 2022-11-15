package com.example.afrofinds;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.afrofinds.Models.Service;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class RestaurantAdapter extends RecyclerView.Adapter<RestaurantAdapter.ServiceViewHolder> {
    Context context;
    ArrayList<Service> list;
    SearchView searchView;

//    String url = "https://afrofinds-93455-default-rtdb.asia-southeast1.firebasedatabase.app/";
  //  DatabaseReference databaseReference = FirebaseDatabase.getInstance(url).getReference("Restaurants");


    public RestaurantAdapter(Context context, ArrayList<Service> list) {
        this.context = context;
        this.list = list;
    }


    @NonNull
    @Override
    public RestaurantAdapter.ServiceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.service_items_layout, parent, false);
        return new ServiceViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RestaurantAdapter.ServiceViewHolder holder, int position) {

        Service service = list.get(position);
        holder.name.setText(service.getName());
        holder.description.setText(service.getDescription());
     //   holder.callButton.setText(service.getTelephone());
        String a = service.getCuisine();
        holder.cuisine.setText(a);
        Picasso.get().load(service.getImg()).into(holder.service_image);


       holder.itemView.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               String id = list.get(position).getId();
               Intent intent = new Intent(context, ItemActivity.class);
               intent.putExtra("id", id);
               context.startActivity(intent);
               //finish();
            }
       });
    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ServiceViewHolder extends RecyclerView.ViewHolder {
        TextView name, description, cuisine;
        ImageView service_image, menu1, menu2, menu3, menu4;

        public ServiceViewHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.name);
            description = itemView.findViewById(R.id.description);
            cuisine = itemView.findViewById(R.id.cuisine);
            service_image = itemView.findViewById(R.id.service_image);
            menu1 = itemView.findViewById(R.id.menu1);
            menu2 = itemView.findViewById(R.id.menu2);
            menu3= itemView.findViewById(R.id.menu3);
            menu4 = itemView.findViewById(R.id.menu4);
            searchView = itemView.findViewById(R.id.search_view_home);


        }
    }
}
