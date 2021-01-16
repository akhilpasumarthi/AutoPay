package com.example.autopay;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.view.LayoutInflater;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import org.web3j.protocol.core.Ethereum;

import java.util.HashMap;
import java.util.Map;

public class profile extends AppCompatActivity {

    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth firebaseAuth;
    private TextView name, mobile, email, dob, gender, balance;
    Button signout;
    ToggleButton toggleButton;
    Map<String,Object> pay=new HashMap<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        BottomNavigationView bottomnav = findViewById(R.id.btmnav2);
        bottomnav.setSelectedItemId(R.id.profile);
        bottomnav.setOnNavigationItemSelectedListener(navlistener);
        name = findViewById(R.id.name);
        mobile = findViewById(R.id.mobile);
        dob = findViewById(R.id.dob);
        email = findViewById(R.id.email);
        gender = findViewById(R.id.gender);
        //balance = findViewById(R.id.balance);

       // View view = findViewById(android.R.id.content).getRootView();
       // ethereum e =new ethereum();
       //e.connectToEthNetwork(view);
       // String text_balance = e.showBalance(view);
       // balance.setText(text_balance);
        firebaseFirestore= FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        toggleButton=(ToggleButton) findViewById(R.id.toggle);
        toggleButton.setOnClickListener(new View.OnClickListener() {
               @Override
              public void onClick(View v) {
                   if (toggleButton.isChecked()){
                       pay.put("paymentstatus","on");
                       Toast.makeText(getApplicationContext(),"ToggleButton is ON",Toast.LENGTH_SHORT).show();
                   }else{

                       pay.put("paymentstatus","off");
                       Toast.makeText(getApplicationContext(),"ToggleButton is OFF",Toast.LENGTH_SHORT).show();
                   }
                   firebaseFirestore.collection("users")
                           .document(firebaseAuth.getCurrentUser().getUid()).update(pay).addOnSuccessListener(new OnSuccessListener<Void>() {
                       @Override
                       public void onSuccess(Void aVoid) {
                           Toast.makeText(profile.this, "Added successfully", Toast.LENGTH_SHORT).show();
                       }
                   });
               }
           });

        signout = findViewById(R.id.signout);
        signout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(profile.this,MainActivity.class);
                startActivity(intent);
            }
        });

        firebaseFirestore.collection("users")
                .document(firebaseAuth.getCurrentUser().getUid()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        name.setText(documentSnapshot.getString("name"));
                        mobile.setText(documentSnapshot.getString("number"));
                        email.setText(documentSnapshot.getString("email"));
                        dob.setText(documentSnapshot.getString("dob"));
                        gender.setText(documentSnapshot.getString("gender"));
                    }
                });
    }
    private BottomNavigationView.OnNavigationItemSelectedListener navlistener=
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    switch (item.getItemId()){
                        case R.id.btmhome:
                            startActivity(new Intent(profile.this,Dashboard.class));
                            return true;
                        case R.id.history:
                            startActivity(new Intent(profile.this,Transactions_list.class));
                            overridePendingTransition(0,0);
                            return true;
                        case R.id.profile:
                            return true;
                    }
                    //getSupportFragmentManager().beginTransaction().replace(R.id.btmfragment,id1);
                    return false;
                }
            };
}