package com.example.autopay;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class Transactions_list extends AppCompatActivity {
    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth firebaseAuth;
    private RecyclerView flist;
    private FirestoreRecyclerAdapter adapter;
    Dialog dialog;
    TextView amount,fromtxt;
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
                Toast.makeText(Transactions_list.this, "Successful", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });

        firebaseFirestore=FirebaseFirestore.getInstance();
        flist=findViewById(R.id.flist);
        firebaseAuth = FirebaseAuth.getInstance();
        Query query=firebaseFirestore.collection("users").document(
                "G5147xYn5uWaOGT6w4M4W83ajgT2")
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
                holder.list_from.setText("From: "+model.getFrom());
                holder.list_to.setText(String.valueOf(model.getTimestamp()));
                if(model.getStatus().equals("unpaid")){
                    holder.status1.setVisibility(View.VISIBLE);
                    holder.paid.setVisibility(View.INVISIBLE);
                    holder.status1.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.show();
                            amount.setText(model.getAmount()+"");
                            fromtxt.setText("From: "+model.getFrom());
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
                            return true;
                        case R.id.history:
                            overridePendingTransition(0,0);
                            return true;
                        case R.id.profile:
                            startActivity(new Intent(Transactions_list.this,profile.class));
                            return true;
                    }
                    //getSupportFragmentManager().beginTransaction().replace(R.id.btmfragment,id1);
                    return false;
                }
            };

    private class transactionviewholder extends RecyclerView.ViewHolder {
        private TextView list_from,paid;
        private TextView list_to;
        private Button status1;
        //private ImageView img;
        public transactionviewholder(@NonNull View itemView) {
            super(itemView);
            list_from=itemView.findViewById(R.id.listfrom);
            list_to=itemView.findViewById(R.id.listto);
            paid=itemView.findViewById(R.id.paid);
            status1=itemView.findViewById(R.id.status1);
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