package com.example.autopay;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

public class payment_page extends AppCompatActivity {
    String toaddress;
    EditText payamount,smsg;
    Button send;
    String to_address = "";
    String to_user = "",user="";
    TextView name1;
    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_page);
        toaddress = getIntent().getStringExtra("toaddress");
        name1 =(TextView) findViewById(R.id.name1);

        payamount=findViewById(R.id.sendamount);
        smsg=findViewById(R.id.smsg);
        send=findViewById(R.id.send);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore=FirebaseFirestore.getInstance();

        firebaseFirestore.collection("users").whereEqualTo("walletaddress", toaddress)
                .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                to_user = queryDocumentSnapshots.getDocuments().get(0).getString("name");
                name1.setText(to_user);
            }
        });


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
                    firebaseFirestore.collection("users")
                            .document(firebaseAuth.getCurrentUser().getUid())
                            .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            user = documentSnapshot.getString("name");
                            to_address = documentSnapshot.getString("walletaddress");
                            Log.i("Name",to_user);
                            DocumentReference documentReference=firebaseFirestore.collection("users")
                                    .document(firebaseAuth.getCurrentUser().getUid())
                                    .collection("transactions").document();
                            Map<String,Object> payments=new HashMap<>();
                            payments.put("from_wallet", to_address);
                            payments.put("name", to_user);
                            payments.put("amount", amt);
                            payments.put("transactionhash",address);
                            documentReference.set(payments).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(getApplicationContext(),"Transaction Completed", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(payment_page.this,Dashboard.class));
                                }
                            });
                        }
                    });

                }
                catch (Exception e){
                    String e1=e.toString();
                    Toast.makeText(getApplicationContext(), e1, Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}