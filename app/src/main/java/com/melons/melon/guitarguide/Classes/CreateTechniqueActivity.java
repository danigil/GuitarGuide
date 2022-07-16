package com.melons.melon.guitarguide;

import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class CreateTechniqueActivity extends AppCompatActivity implements View.OnClickListener {

    //necessities
    ConnectivityReceiver connectivityReceiver;
    CoordinatorLayout coordinatorLayout;

    //page components
    EditText ETcreate;
    RadioButton RB1,RB2,RB3;
    Button Buttoncreate;
    ImageView IVcreate;

    //logic
    Technique new_item;
    String category;

    //database
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_list_item);

        coordinatorLayout = findViewById(R.id.colayout);
        connectivityReceiver = new ConnectivityReceiver(coordinatorLayout);

        ETcreate = findViewById(R.id.ETCreateMainTitle);
        IVcreate = findViewById(R.id.IVCreate);
        Buttoncreate = findViewById(R.id.ButtonCreateSubmit);

        Buttoncreate.setOnClickListener(this);
    }

    public void CheckRB(View v){
        RB1 = findViewById(R.id.RBCreate1);
        RB2 = findViewById(R.id.RBCreate2);
        RB3 = findViewById(R.id.RBCreate3);

        if(v.getId() == RB1.getId()) {
            category ="Basic";
            IVcreate.setImageResource(R.drawable.basic);

            RB1.setTextColor(getResources().getColor(R.color.colorPrimary));
            RB1.setButtonTintList(getResources().getColorStateList(R.color.colorPrimary));

            RB2.setTextColor(Color.WHITE);
            RB2.setButtonTintList(getResources().getColorStateList(R.color.WHITE));

            RB3.setTextColor(Color.WHITE);
            RB3.setButtonTintList(getResources().getColorStateList(R.color.WHITE));
        }

        if(v.getId() == RB2.getId()) {
            category ="Advanced";
            IVcreate.setImageResource(R.drawable.advanced);

            RB1.setTextColor(getResources().getColor(R.color.WHITE));
            RB1.setButtonTintList(getResources().getColorStateList(R.color.WHITE));

            RB2.setTextColor(getResources().getColor(R.color.colorPrimary));
            RB2.setButtonTintList(getResources().getColorStateList(R.color.colorPrimary));

            RB3.setTextColor(Color.WHITE);
            RB3.setButtonTintList(getResources().getColorStateList(R.color.WHITE));
        }

        if(v.getId() == RB3.getId()) {
            category ="Finesse";
            IVcreate.setImageResource(R.drawable.finesse);

            RB1.setTextColor(getResources().getColor(R.color.WHITE));
            RB1.setButtonTintList(getResources().getColorStateList(R.color.WHITE));

            RB2.setTextColor(Color.WHITE);
            RB2.setButtonTintList(getResources().getColorStateList(R.color.WHITE));

            RB3.setTextColor(getResources().getColor(R.color.colorPrimary));
            RB3.setButtonTintList(getResources().getColorStateList(R.color.colorPrimary));
        }

    }

    @Override
    public void onClick(View v) {
            if (ETcreate.getText().toString().trim().matches("") || ETcreate.getText() == null) {
                Toast.makeText(CreateTechniqueActivity.this, "Please enter a technique name!", Toast.LENGTH_SHORT).show();
                return;
            }
            if (this.category == "" || this.category ==null) {
                Toast.makeText(CreateTechniqueActivity.this, "Please select a category!", Toast.LENGTH_SHORT).show();
            } else {
                new_item = new Technique(false, ETcreate.getText().toString(), this.category);

                databaseReference = FirebaseDatabase.getInstance().getReference("TECHNIQUES").child(MainActivity.username).child(ETcreate.getText().toString());
                databaseReference.setValue(this.new_item);

                Intent intent = new Intent(CreateTechniqueActivity.this, TechniqueActivity.class);
                intent.putExtra("user", MainActivity.username);
                Toast.makeText(getApplicationContext(), "Technique added successfully!", Toast.LENGTH_SHORT).show();
                startActivity(intent);
                finish();
            }
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

