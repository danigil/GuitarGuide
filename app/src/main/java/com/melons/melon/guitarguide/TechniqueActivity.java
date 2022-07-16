package com.melons.melon.guitarguide;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class TechniqueActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {

    //necessities
    CoordinatorLayout coordinatorLayout;
    ConnectivityReceiver connectivityReceiver;

    //page components
    TextView header;

    //page design
    Integer screenheight,screenwidth,headerheight;

    //animation
    FloatingActionButton fab1,fab2,fabmain;
    OvershootInterpolator interpolator;
    boolean isMenuOpen;
    float translationY;

    //list
    ListView list;
    ProgressBar progressBar;
    CustomTechListAdapter adap;
    ArrayList<Technique> arrayListFavs;
    ArrayList<Technique> arrayListPublics;
    SwipeRefreshLayout swipeRefreshLayout;

    //database
    DatabaseReference databaseReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_technique);

        coordinatorLayout = findViewById(R.id.colayout);
        connectivityReceiver = new ConnectivityReceiver(coordinatorLayout);

        header = findViewById(R.id.TVTechheader);

        //list
        list = findViewById(R.id.Techlistview);
        arrayListPublics = new ArrayList<>();
        arrayListFavs = new ArrayList<>();
        swipeRefreshLayout = findViewById(R.id.Techswiperefresh);

        progressBar = findViewById(R.id.Techprogress);
        progressBar.setVisibility(View.VISIBLE);

        updateList();

        //initialize fab menu
        initfabmenu();

        SetMetrics();
        SizeRelate();

        swipeRefreshLayout.setOnRefreshListener(this);
    }

    public void updateList() {
        this.databaseReference = FirebaseDatabase.getInstance().getReference("TECHNIQUES").child("public");
        this.databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {

            public void onCancelled(@NonNull DatabaseError param1DatabaseError) {

            }

            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot TechSnapshot : dataSnapshot.getChildren()){
                    Technique technique = TechSnapshot.getValue(Technique.class);
                    arrayListPublics.add(technique);
                }
                databaseReference = FirebaseDatabase.getInstance().getReference("TECHNIQUES").child(MainActivity.username);
                databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {

                    public void onCancelled(@NonNull DatabaseError param2DatabaseError) {

                    }

                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for(DataSnapshot TechSnapshot : dataSnapshot.getChildren()){
                            Technique technique = TechSnapshot.getValue(Technique.class);
                            for (byte b = 0; b < TechniqueActivity.this.arrayListPublics.size(); b++) {
                                String str1 = technique.getMainTitle();
                                String str2 = ((Technique)TechniqueActivity.this.arrayListPublics.get(b)).getMainTitle();
                                if (str1.equals(str2) || str2.equals(str1)) {
                                    TechniqueActivity.this.arrayListPublics.remove(b);
                                    b--;
                                    technique.setFav(true);
                                }
                            }
                        }
                        for(DataSnapshot TechSnapshot : dataSnapshot.getChildren()){
                            Technique technique = TechSnapshot.getValue(Technique.class);
                            TechniqueActivity.this.arrayListPublics.add(technique);
                        }
                        for(DataSnapshot TechSnapshot : dataSnapshot.getChildren()){
                            Technique technique = TechSnapshot.getValue(Technique.class);
                            if (technique.isFav())
                                TechniqueActivity.this.arrayListFavs.add(technique);
                            for (byte b = 0; b < TechniqueActivity.this.arrayListFavs.size(); b++) {
                                String str1 = technique.getMainTitle();
                                String str2 = ((Technique)TechniqueActivity.this.arrayListFavs.get(b)).getMainTitle();
                                if (str1.equals(str2) || str2.equals(str1)) {
                                    TechniqueActivity.this.arrayListFavs.remove(b);
                                    b--;
                                }
                            }
                        }
                        arrayListFavs.addAll(TechniqueActivity.this.arrayListPublics);
                        adap = new CustomTechListAdapter(TechniqueActivity.this, TechniqueActivity.this.arrayListFavs, MainActivity.username);
                        progressBar.setVisibility(View.INVISIBLE);
                        list.setAdapter(adap);
                    }
                });
                adap = new CustomTechListAdapter(TechniqueActivity.this, arrayListFavs, MainActivity.username);
                list.setAdapter(adap);
            }
        });

        if (this.swipeRefreshLayout.isRefreshing())
            this.swipeRefreshLayout.setRefreshing(false);
    }

    //fab menu
    private void initfabmenu() {

        this.fabmain = findViewById(R.id.fabmain);
        this.fab1 = findViewById(R.id.fab1);
        this.fab2 = findViewById(R.id.fab2);

        this.fab1.setAlpha(0.0F);
        this.fab2.setAlpha(0.0F);
        this.fab1.setTranslationY(this.translationY);
        this.fab2.setTranslationY(this.translationY);

        this.fabmain.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                if (TechniqueActivity.this.isMenuOpen) {
                    closeMenu();
                } else {
                    openMenu();
                }
            }
        });

        this.fab1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startActivity(new Intent(TechniqueActivity.this, CreateTechniqueActivity.class));
                finish();
            }
        });

        this.fab2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startActivity(new Intent(TechniqueActivity.this, FavoriteTechniquesActivity.class));
                finish();
            }
        });
    }
    private void openMenu() {
        this.isMenuOpen = !isMenuOpen;
        this.fabmain.animate().alpha(0.7f).setInterpolator(this.interpolator).rotationBy(45.0F).setDuration(300L).start();
        this.fab1.animate().translationY(0.0F).alpha(0.7F).setInterpolator(this.interpolator).setDuration(300L).start();
        this.fab2.animate().translationY(0.0F).alpha(0.7F).setInterpolator(this.interpolator).setDuration(300L).start();
    }
    private void closeMenu() {
        this.isMenuOpen =!isMenuOpen;
        this.fabmain.animate().alpha(0.7f).setInterpolator(this.interpolator).rotationBy(135.0F).setDuration(300L).start();
        this.fab1.animate().translationY(this.translationY).alpha(0.0F).setInterpolator(this.interpolator).setDuration(300L).start();
        this.fab2.animate().translationY(this.translationY).alpha(0.0F).setInterpolator(this.interpolator).setDuration(300L).start();
    }

    //page design
    private void SizeRelate(){
        headerheight=header.getLayoutParams().height;
        int remainderheight = screenheight - headerheight - 200;

        ViewGroup.LayoutParams params = swipeRefreshLayout.getLayoutParams();
        params.height=remainderheight;
        swipeRefreshLayout.setLayoutParams(params);
    }
    private void SetMetrics(){
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        this.screenheight= displayMetrics.heightPixels;
        this.screenwidth = displayMetrics.widthPixels;
    }

    @Override
    public void onRefresh() {
        progressBar.setVisibility(View.VISIBLE);

        arrayListPublics.clear();
        arrayListFavs.clear();

        CustomTechListAdapter adap = new CustomTechListAdapter(TechniqueActivity.this, arrayListFavs, MainActivity.username);
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

}
