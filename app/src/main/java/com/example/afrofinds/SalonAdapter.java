package com.example.afrofinds;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.afrofinds.Models.Salon;
import com.example.afrofinds.Models.Service;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class SalonAdapter extends RecyclerView.Adapter<SalonAdapter.SalonViewHolder> {
    Context context;
    ArrayList<Salon> salonList;
    SearchView searchView;
    
    public SalonAdapter(Context context, ArrayList<Salon> salonList) {
        this.context = context;
        this.salonList = salonList;
    }


    @NonNull
    @Override
    public SalonAdapter.SalonViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.salon_items_layout, parent, false);
        return new SalonAdapter.SalonViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull SalonViewHolder holder, int position) {
        Salon salon = salonList.get(position);
        holder.name.setText(salon.getName());
//        holder.itemWeb.setText(salon.getWebPage());

        Picasso.get().load(salon.getImg()).into(holder.salon_image);


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String id = salonList.get(position).getId();
                Intent intent = new Intent(context, SalonActivity.class);
                intent.putExtra("id", id);
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return salonList.size();
    }

    public class SalonViewHolder extends RecyclerView.ViewHolder {
        TextView name, itemWeb;
        ImageView salon_image;

        public SalonViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            salon_image = itemView.findViewById(R.id.salon_image);
         //   itemWeb = itemView.findViewById(R.id.itemWeb);
            searchView = itemView.findViewById(R.id.search_view_home);
        }
    }
}
