package com.example.autopay;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class Dashboard extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        BottomNavigationView bottomnav=findViewById(R.id.btmnav);
        bottomnav.setSelectedItemId(R.id.btmhome);
        bottomnav.setOnNavigationItemSelectedListener(navlistener);

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
            //
        }else if(id == R.id.help){
            //
        }
        return true;
    }

}