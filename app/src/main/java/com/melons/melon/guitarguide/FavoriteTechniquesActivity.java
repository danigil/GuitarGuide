package com.melons.melon.guitarguide;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class FavoriteTechniquesActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {

    //necessities
    CoordinatorLayout coordinatorLayout;
    ConnectivityReceiver connectivityReceiver;

    //page components
    TextView header;

    //page design
    Integer screenheight,screenwidth,headerheight;

    //list
    ListView list;
    ProgressBar progressBar;
    CustomFavListAdapter adap;
    ArrayList<Technique> arrayListFavs;
    SwipeRefreshLayout swipeRefreshLayout;

    //database
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite_techniques);

        coordinatorLayout = findViewById(R.id.colayout);
        connectivityReceiver = new ConnectivityReceiver(coordinatorLayout);

        header = findViewById(R.id.TVFavheader);

        //list
        list = findViewById(R.id.Favlistview);
        arrayListFavs = new ArrayList<>();
        swipeRefreshLayout = findViewById(R.id.Favswiperefresh);

        progressBar = findViewById(R.id.Favprogress);
        progressBar.setVisibility(View.VISIBLE);

        this.databaseReference = FirebaseDatabase.getInstance().getReference("TECHNIQUES").child(MainActivity.username);

        updateList();

        SetMetrics();
        SizeRelate();

        this.swipeRefreshLayout.setOnRefreshListener(this);
    }

    //populate list
    public void updateList(){
        this.databaseReference = FirebaseDatabase.getInstance().getReference("TECHNIQUES").child(MainActivity.username);
        this.databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot TechniqueSnapshot : dataSnapshot.getChildren()){
                    Technique technique = TechniqueSnapshot.getValue(Technique.class);
                    if(technique.isFav)
                        arrayListFavs.add(technique);
                }

                adap = new CustomFavListAdapter(FavoriteTechniquesActivity.this, arrayListFavs, MainActivity.username);
                progressBar.setVisibility(View.INVISIBLE);
                list.setAdapter(adap);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        if (this.swipeRefreshLayout.isRefreshing())
            this.swipeRefreshLayout.setRefreshing(false);
    }

    //page design
    public void SizeRelate(){
        headerheight=header.getLayoutParams().height;
        int remainderheight = screenheight - headerheight - 200;

        ViewGroup.LayoutParams params = swipeRefreshLayout.getLayoutParams();
        params.height=remainderheight;
        swipeRefreshLayout.setLayoutParams(params);
    }
    public void SetMetrics(){
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        this.screenheight= displayMetrics.heightPixels;
        this.screenwidth = displayMetrics.widthPixels;
    }

    @Override
    public void onRefresh() {
        progressBar.setVisibility(View.VISIBLE);

        arrayListFavs.clear();

        CustomFavListAdapter adap = new CustomFavListAdapter(FavoriteTechniquesActivity.this, arrayListFavs, MainActivity.username);
        list.setAdapter(adap);

        updateList();
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

    @Override
    public void onBackPressed() {
        startActivity(new Intent(getApplicationContext(), TechniqueActivity.class));
        finish();
    }
}
