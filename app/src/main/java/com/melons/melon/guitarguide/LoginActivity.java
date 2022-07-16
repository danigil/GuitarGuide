package com.melons.melon.guitarguide;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    //necesseties
    CoordinatorLayout coordinatorLayout;
    ConnectivityReceiver connectivityReceiver;
    String username;

    //page components
    EditText ETemail,ETpassword;
    Button Loginsubmit;

    //database
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        coordinatorLayout = findViewById(R.id.colayout);
        connectivityReceiver = new ConnectivityReceiver(coordinatorLayout);

        ETemail = findViewById(R.id.ETLoginemail);
        ETpassword  = findViewById(R.id.ETLoginpassword);

        Loginsubmit = findViewById(R.id.ButtonLoginSubmit);
        Loginsubmit.setOnClickListener(this);
    }

    public void CheckUser(){
        final String email = ETemail.getText().toString().trim();
        final String password = ETpassword.getText().toString().trim();

        if (email.matches("") && password.matches("")) {
            Toast.makeText(LoginActivity.this, "Please enter username & password!", Toast.LENGTH_SHORT).show();
        } else if (email.matches("")) {
            Toast.makeText(LoginActivity.this, "Please enter an email address!", Toast.LENGTH_SHORT).show();
        } else if (!isValidEmail(email)) {
            Toast.makeText(LoginActivity.this, "your email is invalid!", Toast.LENGTH_SHORT).show();
        } else if (password.matches("")) {
            Toast.makeText(LoginActivity.this, "Please enter a password!", Toast.LENGTH_SHORT).show();
        } else {
            this.databaseReference = FirebaseDatabase.getInstance().getReference("USERS");
            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {

                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    Long count = dataSnapshot.getChildrenCount();

                    for (DataSnapshot UserSnapshot : dataSnapshot.getChildren()) {
                        if (dataSnapshot.hasChild(email.split("@")[0])) {
                            User user = UserSnapshot.getValue(User.class);
                            if (user.getUsername().equals(email) && user.getPassword().equals(password)) {
                                username = user.getUsername().trim().split("@")[0];

                                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                intent.putExtra("user", username);
                                startActivity(intent);
                                finish();
                                return;
                            }

                            count--;
                        }
                        else {
                            Toast.makeText(LoginActivity.this, "User doesnt exist", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        if(count==0) {
                            Toast.makeText(LoginActivity.this, "Password Incorrect!", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }
                }

                public void onCancelled(@NonNull DatabaseError param2DatabaseError) {

                }

            });
        }
    }

    public static boolean isValidEmail(String email) {
        if (email == null) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
        }
    }

    @Override
    public void onClick(View v) {
        if(v.getId()==Loginsubmit.getId())
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
