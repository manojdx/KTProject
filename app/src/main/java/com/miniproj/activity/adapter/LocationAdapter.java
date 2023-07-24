package com.miniproj.activity.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.miniproj.R;
import com.miniproj.activity.realm.Latilng;

import java.util.ArrayList;
import java.util.List;

public class LocationAdapter extends RecyclerView.Adapter<LocationAdapter.MyviewHolder> {
    private List<Latilng> latLngmodelList;
    Context context;

    public LocationAdapter(List<Latilng> latLngmodelList,Context context) {
        this.latLngmodelList = latLngmodelList;
        this.context = context;
    }

    public void updateList(ArrayList<Latilng> latLngmodelList){
        this.latLngmodelList.clear();
        this.latLngmodelList.addAll(latLngmodelList);
       // notifyDataSetChanged();
    }

    @Override
    public MyviewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.location_list_data, parent, false);
        return new MyviewHolder(itemView);
    }




    @Override
    public void onBindViewHolder(@NonNull MyviewHolder holder, int position) {
        Latilng latLngmodel=latLngmodelList.get(position);
        holder.latVal.setText(latLngmodelList.get(position).getLng());
        holder.lonVal.setText(latLngmodelList.get(position).getLng());
    }


    @Override
    public int getItemCount(){
        return latLngmodelList.size();
    }

     class MyviewHolder extends RecyclerView.ViewHolder {
        TextView latVal;
        TextView lonVal;

        public MyviewHolder(View itemView) {
            super(itemView);
            latVal = itemView.findViewById(R.id.lat_val);
            lonVal = itemView.findViewById(R.id.lon_val);

        }
    }
}


