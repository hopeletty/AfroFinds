package com.example.afrofinds;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.afrofinds.Models.Service;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class RestaurantAdapter extends RecyclerView.Adapter<RestaurantAdapter.ServiceViewHolder> {
    Context context;
    ArrayList<Service> list;

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
        String a = service.getCuisine();
        holder.cuisine.setText(a);
        //String url = "https://firebasestorage.googleapis.com/v0/b/afrofinds-93455.appspot.com/o/braai.jpg?alt=media&token=cc6b07b3-9a7d-4310-814f-fc98442e6161";
        String url = service.getImg();
        Picasso.get().load(url).into(holder.service_image);
        //Picasso.get().load(service.getImg()).into(holder.service_image);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context,ItemActivity.class);
                context.startActivity(intent);
                intent.putExtra("url", url);

            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    public class ServiceViewHolder extends RecyclerView.ViewHolder {
        TextView name, description, cuisine;
        ImageView service_image;

        public ServiceViewHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.name);
            description = itemView.findViewById(R.id.description);
            cuisine = itemView.findViewById(R.id.cuisine);
            service_image = itemView.findViewById(R.id.service_image);

        }

    }

}
