package com.melons.melon.guitarguide;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ManufacturerActivity extends AppCompatActivity {

    //necessities
    CoordinatorLayout coordinatorLayout;
    ConnectivityReceiver connectivityReceiver;

    //database
    DatabaseReference databaseReference;

    //recycler
    RecyclerView recycler;
    ProgressBar progressBar;
    ArrayList<Manufacturer> arrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manufacturer);

        coordinatorLayout = findViewById(R.id.colayout);
        connectivityReceiver = new ConnectivityReceiver(coordinatorLayout);

        recycler = findViewById(R.id.Manurecycler);
        arrayList = new ArrayList<>();

        progressBar = findViewById(R.id.Manuprogress);
        progressBar.setVisibility(View.VISIBLE);

        updateRecycler();
    }

    //populate recycler
    public void updateRecycler(){
        databaseReference = FirebaseDatabase.getInstance().getReference("MANUFACTURERS");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {

            public void onCancelled(@NonNull DatabaseError param1DatabaseError) {

            }

            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot ManuSnapshot : dataSnapshot.getChildren()){
                    Manufacturer manufacturer = ManuSnapshot.getValue(Manufacturer.class);
                    arrayList.add(manufacturer);
                    CustomManufacturersRVAdapter customManufacturersRVAdapter = new CustomManufacturersRVAdapter(ManufacturerActivity.this, getLayoutInflater(), arrayList);
                    recycler.setAdapter(customManufacturersRVAdapter);
                }
                CustomManufacturersRVAdapter customManufacturersRVAdapter = new CustomManufacturersRVAdapter(ManufacturerActivity.this, getLayoutInflater(), arrayList);

                progressBar.setVisibility(View.GONE);
                recycler.setAdapter(customManufacturersRVAdapter);
                recycler.setLayoutManager(new GridLayoutManager(ManufacturerActivity.this,3));
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
