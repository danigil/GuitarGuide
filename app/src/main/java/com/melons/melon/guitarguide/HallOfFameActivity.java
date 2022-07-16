package com.melons.melon.guitarguide;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.RecyclerView;

import android.content.IntentFilter;
import android.os.Bundle;
import android.widget.TextView;

import com.azoft.carousellayoutmanager.CarouselLayoutManager;
import com.azoft.carousellayoutmanager.CarouselZoomPostLayoutListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class HallOfFameActivity extends AppCompatActivity implements CarouselLayoutManager.OnCenterItemSelectionListener {

    //necessities
    CoordinatorLayout coordinatorLayout;
    ConnectivityReceiver connectivityReceiver;

    //database
    DatabaseReference databaseReference;

    //recycler
    RecyclerView recycler;
    ArrayList<Guitarist> arrayList;

    //logic
    TextView TVname,TVdesc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hall_of_fame);

        coordinatorLayout = findViewById(R.id.colayout);
        connectivityReceiver = new ConnectivityReceiver(coordinatorLayout);

        recycler = findViewById(R.id.Famerecycler);
        arrayList = new ArrayList<>();

        TVname = findViewById(R.id.ButtonFamename);
        TVdesc = findViewById(R.id.TVFamedesc);

        setRecycler();
    }

    //populate recycler
    private void setRecycler(){
        this.databaseReference = FirebaseDatabase.getInstance().getReference("GUITARISTS");
        this.databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {

            public void onCancelled(@NonNull DatabaseError param1DatabaseError) {

            }

            public void onDataChange(@NonNull DataSnapshot param1DataSnapshot) {
                for(DataSnapshot GuitaristSnapshot : param1DataSnapshot.getChildren()){
                    Guitarist guitarist = GuitaristSnapshot.getValue(Guitarist.class);
                    arrayList.add(guitarist);
                }
                CustomGuitaristsRVAdapter customGuitaristsRVAdapter = new CustomGuitaristsRVAdapter(HallOfFameActivity.this, arrayList);
                recycler.setAdapter(customGuitaristsRVAdapter);
            }
        });


        CustomGuitaristsRVAdapter customGuitaristsRVAdapter = new CustomGuitaristsRVAdapter(this, arrayList);
        CarouselLayoutManager carouselLayoutManager = new CarouselLayoutManager(0, true);
        carouselLayoutManager.setPostLayoutListener(new CarouselZoomPostLayoutListener());
        recycler.setLayoutManager(carouselLayoutManager);

        carouselLayoutManager.addOnItemSelectionListener(this);

        recycler.setHasFixedSize(true);
        recycler.setAdapter(customGuitaristsRVAdapter);
    }

    @Override
    public void onCenterItemChanged(final int adapterPosition) {
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            public void onCancelled(@NonNull DatabaseError param2DatabaseError) {

            }

            public void onDataChange(@NonNull DataSnapshot param2DataSnapshot) {

                for(DataSnapshot GuitaristSnapshot : param2DataSnapshot.getChildren()){
                    Guitarist guitarist = GuitaristSnapshot.getValue(Guitarist.class);
                    arrayList.add(guitarist);
                }
                if (adapterPosition == -1)
                    return;
                Guitarist guitarist = arrayList.get(adapterPosition);
                TVname.setText(guitarist.getName());
                TVdesc.setText(guitarist.getDescription());
            }
        });
    }

    @Override
    public void onPause() {
        super.onPause();
        connectivityReceiver.i = Integer.valueOf(0);
        unregisterReceiver(connectivityReceiver);
    }

    @Override
    public void onResume() {
        super.onResume();
        registerReceiver(connectivityReceiver, new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE"));
    }


}

