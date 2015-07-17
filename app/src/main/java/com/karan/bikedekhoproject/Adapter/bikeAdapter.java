package com.karan.bikedekhoproject.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.karan.bikedekhoproject.Entity.Bike;
import com.karan.bikedekhoproject.R;

import java.util.ArrayList;

/**
 * Created by karanahuja on 15/07/15.
 */
public class bikeAdapter extends RecyclerView.Adapter<bikeAdapter.BikeViewHolder>
{
        ArrayList<Bike> bikes;
        Context context;
        ImageLoader imageLoader;

     public bikeAdapter(ArrayList<Bike> bikes, Context context, ImageLoader imageLoader){
        this.bikes = bikes;
        this.context = context;
         this.imageLoader = imageLoader;
        }

public static class BikeViewHolder extends RecyclerView.ViewHolder {
    CardView cv;
    TextView bikeName;
    TextView cc;
    TextView kmpl;
    TextView price;
    NetworkImageView imageView; // Volleys extended class which catches OOM exceptions and ensures aggressive GC

    // ImageView personPhoto;
    BikeViewHolder(View itemView) {
        super(itemView);

        cv = (CardView)itemView.findViewById(R.id.cv);
        bikeName = (TextView)itemView.findViewById(R.id.bikeName);
        cc = (TextView)itemView.findViewById(R.id.cc);
        kmpl = (TextView)itemView.findViewById(R.id.kmpl);
        price = (TextView)itemView.findViewById(R.id.price);
        imageView = (NetworkImageView)itemView.findViewById(R.id.bikeImage);
    }
}

    @Override
    public BikeViewHolder onCreateViewHolder(ViewGroup viewGroup, final int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.bike_row, viewGroup, false);
        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });
        BikeViewHolder pvh = new BikeViewHolder(v);
        return pvh;
    }

    @Override
    public int getItemCount() {
        return bikes.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    /*In Main Activity since we parsed in revers, we need to display again in reverse to get it right*/
    @Override
    public void onBindViewHolder(BikeViewHolder bikeViewHolder, int i) {
        bikeViewHolder.bikeName.setText(bikes.get(i).getName());
        bikeViewHolder.cc.setText(bikes.get(i).getCc());
        bikeViewHolder.price.setText("Rs. "+bikes.get(i).getAmount());
        bikeViewHolder.kmpl.setText(bikes.get(i).getKmpl());
        bikeViewHolder.imageView.setImageUrl(bikes.get(i).getImageURL(), imageLoader);





    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

}

