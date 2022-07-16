package com.melons.melon.guitarguide;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class CustomManufacturersRVAdapter extends RecyclerView.Adapter<CustomManufacturersRVAdapter.MyViewHolder> {

    //logic
    Context mContext;
    LayoutInflater layoutInflater;
    ArrayList<Manufacturer> mManufacturers;

    public CustomManufacturersRVAdapter(Context mContext, LayoutInflater layoutInflater, ArrayList<Manufacturer> mManufacturers) {
        this.mContext = mContext;
        this.layoutInflater = layoutInflater;
        this.mManufacturers = mManufacturers;
    }

    @Override
    public int getItemCount() {
        return this.mManufacturers.size();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view;
        LayoutInflater mInfalter = LayoutInflater.from(mContext);
        view = mInfalter.inflate(R.layout.cardview_item_manu,parent,false);
        return new MyViewHolder(view);
    }

    public void onBindViewHolder(@NonNull MyViewHolder viewHolder, final int position) {
        Picasso.get().load((this.mManufacturers.get(position)).getUrl()).fit().centerInside().into(viewHolder.imageButton);

        viewHolder.imageButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                final AlertDialog alertDialog = (new AlertDialog.Builder(mContext)).create();
                View view = layoutInflater.inflate(R.layout.dialog_manufacturers, null);

                ((TextView)view.findViewById(R.id.TVManudialog_name)).setText((mManufacturers.get(position)).getName());

                TextView textView = view.findViewById(R.id.TVManudialog_desc);
                textView.setText((mManufacturers.get(position)).getDescription().replace("_n", "\n"));

                view.findViewById(R.id.ButtonManudialog_close).setOnClickListener(new View.OnClickListener() {

                    public void onClick(View param2View) {
                        alertDialog.dismiss();
                    }
                });
                alertDialog.setView(view);
                alertDialog.show();
            }
        });
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        public ImageButton imageButton;

        public MyViewHolder(View itemView) {
            super(itemView);
            this.imageButton = itemView.findViewById(R.id.IBcardview_manu);
        }
    }


}
