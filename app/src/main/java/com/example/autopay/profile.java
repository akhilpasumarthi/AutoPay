package com.example.autopay;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class profile extends AppCompatActivity {

    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth firebaseAuth;
    private TextView name, mobile, email, dob;
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

        firebaseFirestore= FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore.collection("users")
                .document(firebaseAuth.getCurrentUser().getUid()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        name.setText(documentSnapshot.getString("name"));
                        mobile.setText(documentSnapshot.getString("number"));
//                        email.setText(documentSnapshot.getString("email"));
//                        dob.setText(documentSnapshot.getString("dob"));
                    }
                });
    }
    private BottomNavigationView.OnNavigationItemSelectedListener navlistener=
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    switch (item.getItemId()){
                        case R.id.btmhome:
                            return true;
                        case R.id.history:
                            startActivity(new Intent(profile.this,Transactions_list.class));
                            overridePendingTransition(0,0);
                            return true;
                        case R.id.profile:
                            startActivity(new Intent(profile.this,profile.class));
                            overridePendingTransition(0,0);
                            return true;
                    }
                    //getSupportFragmentManager().beginTransaction().replace(R.id.btmfragment,id1);
                    return false;
                }
            };
}