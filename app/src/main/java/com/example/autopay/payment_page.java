package com.example.autopay;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class payment_page extends AppCompatActivity {
    String toaddress;
    EditText payamount,smsg;
    Button send;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_page);
        toaddress = getIntent().getStringExtra("toaddress");
        payamount=findViewById(R.id.sendamount);
        smsg=findViewById(R.id.smsg);
        send=findViewById(R.id.send);
        Log.i("toaddress",toaddress);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Long amt=Long.parseLong(payamount.getText().toString());
                    ethereum e = new ethereum();
                    String net = e.connectToEthNetwork(v);
                    //Toast.makeText(getApplicationContext(), net, Toast.LENGTH_LONG).show();
                    String address = e.sendTransaction(v,amt, toaddress);
                    Toast.makeText(getApplicationContext(),"Transaction Completed", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(payment_page.this,Dashboard.class));
                }
                catch (Exception e){
                    String e1=e.toString();
                    Toast.makeText(getApplicationContext(), e1, Toast.LENGTH_LONG).show();

                }
            }
        });
    }
}