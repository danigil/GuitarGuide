package com.melons.melon.guitarguide;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class CustomTechListAdapter extends ArrayAdapter<Technique>{

    //necesseties
    Context context;
    String username;

    //database
    DatabaseReference databaseReference;

    //logic
    ArrayList<Technique> listItemArrayList;

    public CustomTechListAdapter(Context context, ArrayList<Technique> arrayList, String username){
        super(context,R.layout.techlist,arrayList);

        this.context=context;
        this.listItemArrayList = arrayList;
        this.username=username;
    }

        @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        final Technique technique = this.listItemArrayList.get(position);

        LayoutInflater inflater = LayoutInflater.from(context);
        convertView = inflater.inflate(R.layout.techlist,parent,false);

        ImageView imageView = convertView.findViewById(R.id.IVtechlist);
        final ImageButton imageButton = convertView.findViewById(R.id.IBtechlistfav);
        TextView MainTitle = convertView.findViewById(R.id.TVtechlistmain);
        TextView SubTitle = convertView.findViewById(R.id.TVtechlistsub);

        switch(technique.getSubTitle()){
            case "Basic":
                imageView.setImageResource(R.drawable.basic);
                break;
            case "Advanced":
                imageView.setImageResource(R.drawable.advanced);
                break;
            case "Finesse":
                imageView.setImageResource(R.drawable.finesse);
                break;
        }

        if(technique.isFav) {
            imageButton.setImageResource(R.drawable.ic_star);
        }
        else {
            imageButton.setImageResource(R.drawable.ic_star_border);
        }

        MainTitle.setText(technique.getMainTitle());
        SubTitle.setText(technique.getSubTitle());

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                databaseReference = FirebaseDatabase.getInstance().getReference("TECHNIQUES").child(username).child(technique.getMainTitle());
                if(technique.isFav){
                    databaseReference.removeValue();
                    technique.isFav=false;
                    imageButton.setImageResource(R.drawable.ic_star_border);
                }
                else {
                    databaseReference.setValue(new Technique(true, technique.getMainTitle(), technique.getSubTitle()));
                    technique.isFav=true;
                    imageButton.setImageResource(R.drawable.ic_star);
                    Toast.makeText(context, "Technique favorited!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return convertView;
    }


}
