package com.example.autopay;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
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

import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class Dashboard extends AppCompatActivity {
    public void pic(View view){
        startActivity(new Intent(Dashboard.this,ethereum.class));
    }
    ImageView qrcodegen;
    TextView showbal;
    TextView todaySpentTextView, monthSpentTextView, fuelTextView, parkingTextView, tollTextView;
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
        qrcodegen=(ImageView) findViewById(R.id.qrcodegen);
        showbal=(TextView)findViewById(R.id.showbal);
        firebaseFirestore= FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        todaySpentTextView = findViewById(R.id.textView5);
        monthSpentTextView = findViewById(R.id.textView7);
        fuelTextView = findViewById(R.id.textView9);
        tollTextView = findViewById(R.id.textView10);
        parkingTextView = findViewById(R.id.textView11);

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
                .collection("transactions").whereGreaterThanOrEqualTo("timestamp", today).whereEqualTo("status", "paid").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                List<DocumentSnapshot> temp = queryDocumentSnapshots.getDocuments();
                int todaySpent = 0;
                System.out.println("size: " + temp.size());
                for(int i=0;i<temp.size();i++){
                    todaySpent += Integer.parseInt(temp.get(i).get("amount").toString());
                }
                todaySpentTextView.setText(String.valueOf(todaySpent));
            }
        });

        firebaseFirestore.collection("users").document(firebaseAuth.getCurrentUser().getUid())
                .collection("transactions").whereGreaterThanOrEqualTo("timestamp", month).whereEqualTo("status", "paid").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
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
                .collection("transactions").whereGreaterThanOrEqualTo("timestamp", month)
                .whereEqualTo("type", "fuel").whereEqualTo("status", "paid").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
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
                .collection("transactions").whereGreaterThanOrEqualTo("timestamp", month)
                .whereEqualTo("type", "toll").whereEqualTo("status", "paid").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
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
                .collection("transactions").whereGreaterThanOrEqualTo("timestamp", month)
                .whereEqualTo("type", "parking").whereEqualTo("status", "paid").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
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

        firebaseFirestore.collection("users")
                .document(firebaseAuth.getCurrentUser().getUid()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                String waddress=documentSnapshot.getString("walletaddress");
                View v = null;
                ethereum e=new ethereum();
                String msg=e.connectToEthNetwork(v);
                String bal=e.showBalance(v,waddress);
                //int balance1=Integer.parseInt(bal)*1000;
                showbal.setText(bal);
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
                            overridePendingTransition(0,0);
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