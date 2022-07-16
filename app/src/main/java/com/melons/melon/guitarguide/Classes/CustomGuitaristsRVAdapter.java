package com.melons.melon.guitarguide;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class CustomGuitaristsRVAdapter extends RecyclerView.Adapter<CustomGuitaristsRVAdapter.MyViewHolder> {

    //logic
    Context mContext;
    ArrayList<Guitarist> mGuitarists;

    public CustomGuitaristsRVAdapter(Context mContext, ArrayList<Guitarist> mGuitarists) {
        this.mContext = mContext;
        this.mGuitarists = mGuitarists;
    }

    @Override
    public int getItemCount() {
        return this.mGuitarists.size();
    }

    @NonNull
    @Override
    public CustomGuitaristsRVAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view;
        LayoutInflater mInfalter = LayoutInflater.from(mContext);
        view = mInfalter.inflate(R.layout.cardview_item_guitarist,parent,false);
        return new CustomGuitaristsRVAdapter.MyViewHolder(view);
    }

    public void onBindViewHolder(@NonNull CustomGuitaristsRVAdapter.MyViewHolder viewHolder, final int position) {
        Picasso.get().load((this.mGuitarists.get(position)).getUrl()).fit().centerInside().into(viewHolder.imageView);

    }



    public static class MyViewHolder extends RecyclerView.ViewHolder {

        public ImageView imageView;

        public MyViewHolder(View itemView) {
            super(itemView);
            this.imageView = itemView.findViewById(R.id.IVcardview_fame);
        }
    }
}
