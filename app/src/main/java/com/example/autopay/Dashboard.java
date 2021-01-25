package com.example.autopay;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.type.DateTime;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class Dashboard extends AppCompatActivity {
    //public void btn(View view){
       // startActivity(new Intent(Dashboard.this,registration.class));
    //}
    Button signout;
    ImageView qrcodegen;
    TextView balance, todaySpentTextView, monthSpentTextView, fuelTextView, parkingTextView, tollTextView;
    Long today, month;
    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        BottomNavigationView bottomnav = findViewById(R.id.btmnav);
        bottomnav.setSelectedItemId(R.id.btmhome);
        bottomnav.setOnNavigationItemSelectedListener(navlistener);
        balance = findViewById(R.id.textView12);
        todaySpentTextView = findViewById(R.id.textView5);
        monthSpentTextView = findViewById(R.id.textView7);
        fuelTextView = findViewById(R.id.textView9);
        tollTextView = findViewById(R.id.textView10);
        parkingTextView = findViewById(R.id.textView11);
        qrcodegen=(ImageView) findViewById(R.id.qrcodegen);

//        ethereum ether = new ethereum();
//        String bal = ether.showBalance();
//        balance.setText(bal);

        Date date = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 0);// for 6 hour
        calendar.set(Calendar.MINUTE, 0);// for 0 min
        calendar.set(Calendar.SECOND, 0);

        today = calendar.getTimeInMillis();

        Date date1 = new Date();
        Calendar calendar1 = Calendar.getInstance();
        calendar1.setTime(date1);
        calendar1.set(Calendar.DAY_OF_MONTH, 1);
        calendar1.set(Calendar.HOUR_OF_DAY, 0);// for 6 hour
        calendar1.set(Calendar.MINUTE, 0);// for 0 min
        calendar1.set(Calendar.SECOND, 0);

        month = calendar1.getTimeInMillis();

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore=FirebaseFirestore.getInstance();

        firebaseFirestore.collection("users").document(firebaseAuth.getCurrentUser().getUid())
                .collection("transactions").whereGreaterThanOrEqualTo("timestamp", today).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                List<DocumentSnapshot> temp = queryDocumentSnapshots.getDocuments();
                int todaySpent = 0;
                for(int i=0;i<temp.size();i++){
                    todaySpent += Integer.parseInt(temp.get(i).get("amount").toString());
                }
                todaySpentTextView.setText(String.valueOf(todaySpent));
            }
        });

        firebaseFirestore.collection("users").document(firebaseAuth.getCurrentUser().getUid())
                .collection("transactions").whereGreaterThanOrEqualTo("timestamp", month).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                List<DocumentSnapshot> temp = queryDocumentSnapshots.getDocuments();
                int monthSpent = 0;
                for(int i=0;i<temp.size();i++){
                    monthSpent += Integer.parseInt(temp.get(i).get("amount").toString());
                }
                monthSpentTextView.setText(String.valueOf(monthSpent));
            }
        });

        firebaseFirestore.collection("users").document(firebaseAuth.getCurrentUser().getUid())
                .collection("transactions") //.whereGreaterThanOrEqualTo("timestamp", month)
                .whereEqualTo("type", "fuel").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                List<DocumentSnapshot> temp = queryDocumentSnapshots.getDocuments();
                int fuel = 0;
                for(int i=0;i<temp.size();i++){
                    fuel += Integer.parseInt(temp.get(i).get("amount").toString());
                }
                fuelTextView.setText(String.valueOf(fuel));
            }
        });

        firebaseFirestore.collection("users").document(firebaseAuth.getCurrentUser().getUid())
                .collection("transactions") //.whereGreaterThanOrEqualTo("timestamp", month)
                .whereEqualTo("type", "toll").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                List<DocumentSnapshot> temp = queryDocumentSnapshots.getDocuments();
                int toll = 0;
                for(int i=0;i<temp.size();i++){
                    toll += Integer.parseInt(temp.get(i).get("amount").toString());
                }
                tollTextView.setText(String.valueOf(toll));
            }
        });

        firebaseFirestore.collection("users").document(firebaseAuth.getCurrentUser().getUid())
                .collection("transactions") //.whereGreaterThanOrEqualTo("timestamp", month)
                .whereEqualTo("type", "parking").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                List<DocumentSnapshot> temp = queryDocumentSnapshots.getDocuments();
                int parking = 0;
                for(int i=0;i<temp.size();i++){
                    parking += Integer.parseInt(temp.get(i).get("amount").toString());
                }
                parkingTextView.setText(String.valueOf(parking));
            }
        });



        qrcodegen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Dashboard.this,qrcode.class));
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
                            startActivity(new Intent(Dashboard.this,Transactions_list.class));
                            overridePendingTransition(0,0);
                            return true;
                        case R.id.profile:
                            startActivity(new Intent(Dashboard.this,profile.class));
                            return true;
                    }
                    //getSupportFragmentManager().beginTransaction().replace(R.id.btmfragment,id1);
                    return false;
                }
            };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id=item.getItemId();
        if(id == R.id.location){
            startActivity(new Intent(Dashboard.this,MapsActivity.class));
        }else if(id == R.id.scanner){
            startActivity(new Intent(Dashboard.this,QR_Scanner.class));

            //
        }else if(id == R.id.help){
            //
        }
        return true;
    }

}