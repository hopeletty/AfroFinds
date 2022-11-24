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

import com.example.afrofinds.Models.Community;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class CommunityAdapter extends RecyclerView.Adapter<CommunityAdapter.CommunityViewHolder> {
    Context context;
    ArrayList<Community> communityList;
    SearchView searchView;

    public CommunityAdapter(Context context, ArrayList<Community> communityList) {
        this.context = context;
        this.communityList = communityList;
    }


    @NonNull
    @Override
    public CommunityAdapter.CommunityViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.community_layout, parent, false);
        return new CommunityAdapter.CommunityViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull CommunityAdapter.CommunityViewHolder holder, int position) {
        Community community = communityList.get(position);
        holder.country.setText(community.getCountry());
        holder.embassy.setText(community.getEmbassy());
        Picasso.get().load(community.getImg()).into(holder.community_image);


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String id = communityList.get(position).getId();
                Intent intent = new Intent(context, CommunityActivity.class);
                intent.putExtra("id", id);
                context.startActivity(intent);
                //finish();
            }
        });
    }


    @Override
    public int getItemCount() {
        return communityList.size();
    }

    public class CommunityViewHolder extends RecyclerView.ViewHolder {
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
