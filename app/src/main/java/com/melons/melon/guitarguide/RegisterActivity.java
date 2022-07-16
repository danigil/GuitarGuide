package com.melons.melon.guitarguide;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Iterator;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    //necesseties
    CoordinatorLayout coordinatorLayout;
    ConnectivityReceiver connectivityReceiver;
    String username;

    //page components
    EditText ETemail,ETpassword,ETconfirmpassword;
    Button Registersumbit;

    //database
    DatabaseReference databaseReference;
    ArrayList<User> userArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        coordinatorLayout = findViewById(R.id.colayout);
        connectivityReceiver = new ConnectivityReceiver(coordinatorLayout);

        ETemail = findViewById(R.id.ETRegisteremail);
        ETpassword = findViewById(R.id.ETRegisterpassword);
        ETconfirmpassword = findViewById(R.id.ETRegisterconfirmpassword);

        Registersumbit = findViewById(R.id.ButtonRegisterSubmit);
        Registersumbit.setOnClickListener(this);

        databaseReference = FirebaseDatabase.getInstance().getReference("USERS");
        userArrayList = new ArrayList<>();
        GetUsersFromDatabase();

    }

    public void GetUsersFromDatabase(){
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            public void onCancelled(@NonNull DatabaseError param1DatabaseError) {

            }

            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot UserSnapshot:dataSnapshot.getChildren()) {
                    User user = UserSnapshot.getValue(User.class);
                    userArrayList.add(user);
                }
            }
        });
    }

    public void CheckUser(){
        final String email = ETemail.getText().toString().trim();
        final String password = ETpassword.getText().toString().trim();
        final String confirm = ETconfirmpassword.getText().toString().trim();

        if (email.matches("") && password.trim().matches("") && confirm.matches("")) {
            Toast.makeText(RegisterActivity.this, "Please enter email,password and confirm your password!", Toast.LENGTH_SHORT).show();
        } else if (email.matches("")) {
            Toast.makeText(RegisterActivity.this, "Please enter an email address!", Toast.LENGTH_SHORT).show();
        } else if (!LoginActivity.isValidEmail(email)) {
            Toast.makeText(RegisterActivity.this, "your email is invalid!", Toast.LENGTH_SHORT).show();
        } else if (password.matches("")) {
            Toast.makeText(RegisterActivity.this, "Please enter a password!", Toast.LENGTH_SHORT).show();
        }else if (confirm.matches("")) {
            Toast.makeText(RegisterActivity.this, "Please confirm your password!", Toast.LENGTH_SHORT).show();
        } else if (!confirm.matches(password)) {
            Toast.makeText(RegisterActivity.this, "Passwords have to match!", Toast.LENGTH_SHORT).show();
        } else if (!Adduser()) {
            username = email.split("@")[0];

            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            intent.putExtra("user", username);
            startActivity(intent);
            finish();
        }
    }

    public boolean Adduser() {
        User user = new User(this.ETpassword.getText().toString().trim(),this.ETemail.getText().toString().trim());
        boolean bool = false;
        for (int i = 0; i < this.userArrayList.size(); i++) {
            User checkedUser = this.userArrayList.get(i);
            if (user.getUsername().equals(checkedUser.getUsername()) || checkedUser.getUsername().equals(user.getUsername()))
                bool = true;
        }
        if (!bool) {
            databaseReference.child(this.ETemail.getText().toString().trim().split("@")[0]).setValue(user);
        } else {
            Toast.makeText(this, "User already exists!", Toast.LENGTH_SHORT).show();
        }
        return bool;
    }

    @Override
    public void onClick(View v) {
        CheckUser();
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
        Intent intent = new Intent(getApplicationContext(), WelcomeActivity.class);
        startActivity(intent);
        finish();
    }
}
