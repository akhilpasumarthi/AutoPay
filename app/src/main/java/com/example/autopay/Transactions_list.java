package com.example.autopay;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Transactions_list extends AppCompatActivity {
    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth firebaseAuth;
    private RecyclerView flist;
    private FirestoreRecyclerAdapter adapter;
    Dialog dialog;
    TextView amount,fromtxt;
    FirebaseUser userpayaddress;
    String payaddress;
    String selectedID;
    String fromuser;
    String merchant_uid = "";
    String wallet_address = "";
    String from_address = "";
    String storage;
    ProgressBar dialogprogress;
    long r;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transactions_list);
        BottomNavigationView bottomnav=findViewById(R.id.btmnav1);
        bottomnav.setSelectedItemId(R.id.history);
        bottomnav.setOnNavigationItemSelectedListener(navlistener);
        dialog=new Dialog(Transactions_list.this);
        dialog.setContentView(R.layout.custom_dialog);
        dialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.background));
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.setCancelable(false);
        dialog.getWindow().getAttributes().windowAnimations=R.style.animation;
        Button btnpay=dialog.findViewById(R.id.btnpay);
        Button cancel=dialog.findViewById(R.id.cancel);
        amount=dialog.findViewById(R.id.amount);
        fromtxt=dialog.findViewById(R.id.fromtxt);
        dialogprogress=dialog.findViewById(R.id.dialogprogress);
        firebaseFirestore=FirebaseFirestore.getInstance();
        flist=findViewById(R.id.flist);
        firebaseAuth = FirebaseAuth.getInstance();
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(Transactions_list.this, "Cancelled", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });
        btnpay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogprogress.setVisibility(View.VISIBLE);
                btnpay.setEnabled(false);
                ethereum e=new ethereum();
                String msg=e.connectToEthNetwork(v);
                String p=String.valueOf(r);
                Log.i("the userid is",firebaseAuth.getCurrentUser().getUid());
                Log.i("Transaction id",selectedID);
                firebaseFirestore.collection("users")
                        .document(firebaseAuth.getCurrentUser().getUid())
                        .collection("transactions").document(selectedID).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        merchant_uid = documentSnapshot.getString("userid");
                        firebaseFirestore.collection("merchants").document(merchant_uid).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                wallet_address = documentSnapshot.getString("wallet");
                                Log.i("ram ka adda",wallet_address);
                                firebaseFirestore.collection("users")
                                        .document(firebaseAuth.getCurrentUser().getUid()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                    @Override
                                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                                        from_address=documentSnapshot.getString("walletaddress");
                                        storage = documentSnapshot.getString("storagepath");
                                String address=e.sendTransaction(v, r, wallet_address,storage);
                                Map<String,Object> users=new HashMap<>();
                                users.put("transactionhash",address);
                                users.put("status","paid");
                                firebaseFirestore.collection("users")
                                        .document(firebaseAuth.getCurrentUser().getUid())
                                        .collection("transactions").document(selectedID).update(users);
                                //Toast.makeText(Transactions_list.this,msg, Toast.LENGTH_SHORT).show();


                                        DocumentReference documentReference=firebaseFirestore.collection("merchants")
                                                .document(merchant_uid).collection("transactions").document();
                                        Map<String,Object> payments=new HashMap<>();
                                        payments.put("from_wallet", from_address);
                                        payments.put("name",fromuser);
                                        payments.put("amount",r);
                                        payments.put("transactionhash",address);
                                        documentReference.set(payments).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                Toast.makeText(Transactions_list.this, "Transaction successful", Toast.LENGTH_SHORT).show();
                                                dialog.dismiss();
                                            }
                                        });

                                    }
                                });

                            }
                        });

                    }
                 });

                //Toast.makeText(Transactions_list.this, "Success", Toast.LENGTH_SHORT).show();

            }
        });


        Query query=firebaseFirestore.collection("users").document(
                firebaseAuth.getCurrentUser().getUid())
                .collection("transactions").orderBy("timestamp", Query.Direction.DESCENDING);
        FirestoreRecyclerOptions<trasaction> options=new FirestoreRecyclerOptions.Builder<trasaction>()
                .setQuery(query,trasaction.class)
                .build();
         adapter= new FirestoreRecyclerAdapter<trasaction, transactionviewholder>(options) {
            @NonNull
            @Override
            public transactionviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_single, parent,false);
                return new transactionviewholder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull transactionviewholder holder, int position, @NonNull trasaction model) {
//                Log.i("raju",model.getAddress());
//                Log.i("raju1",model.getTo());
                if(model.getAddress().equals("receiver")){
                    holder.list_from.setText("From: " + model.getFrom());

                }else {
                    holder.list_from.setText("To: "+model.getTo());
                }
                holder.amountview.setText("Amount: "+model.getAmount()+"");
                Timestamp ts=new Timestamp(Long.parseLong(String.valueOf(model.getTimestamp())));
                //System.out.println(String.valueOf(ts));
                holder.list_to.setText(String.valueOf(ts));
                if(model.getStatus().equals("unpaid")){
                    holder.status1.setVisibility(View.VISIBLE);
                    holder.paid.setVisibility(View.INVISIBLE);
                    holder.status1.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.show();
                            amount.setText(model.getAmount()+"");
                            fromtxt.setText("Address: "+model.getTo());
                            r=model.getAmount();
                            fromuser=model.getFrom();
                            query.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                @Override
                                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                    List<DocumentSnapshot> temp = queryDocumentSnapshots.getDocuments();
                                    selectedID = temp.get(position).getId();
                                }
                            });
                        }
                    });
                   // holder.img.setImageResource(R.drawable.bunk1);
                }else {
                    holder.status1.setVisibility(View.INVISIBLE);
                    holder.paid.setVisibility(View.VISIBLE);
                    holder.paid.setText(model.getStatus());
                    //holder.img.setImageResource(R.drawable.parking);
                }
            }
        };
         flist.setHasFixedSize(true);
         flist.setLayoutManager(new LinearLayoutManager(this));
         flist.setAdapter(adapter);
    }
    private BottomNavigationView.OnNavigationItemSelectedListener navlistener=
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    switch (item.getItemId()){
                        case R.id.btmhome:
                            startActivity(new Intent(Transactions_list.this,Dashboard.class));
                            overridePendingTransition(0,0);
                            return true;
                        case R.id.history:
                            overridePendingTransition(0,0);
                            return true;
                        case R.id.profile:
                            startActivity(new Intent(Transactions_list.this,profile.class));
                            overridePendingTransition(0,0);
                            return true;
                    }
                    //getSupportFragmentManager().beginTransaction().replace(R.id.btmfragment,id1);
                    return false;
                }
            };

    private class transactionviewholder extends RecyclerView.ViewHolder {
        private TextView list_from,paid,amountview;
        private TextView list_to;
        private Button status1;
        //private ImageView img;
        public transactionviewholder(@NonNull View itemView) {
            super(itemView);
            list_from=itemView.findViewById(R.id.listfrom);
            list_to=itemView.findViewById(R.id.listto);
            paid=itemView.findViewById(R.id.paid);
            status1=itemView.findViewById(R.id.status1);
            amountview=itemView.findViewById(R.id.amountview);
           // img=(ImageView) findViewById(R.id.pic);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }
}