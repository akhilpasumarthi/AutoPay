package com.example.autopay;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class Transactions_list extends AppCompatActivity {
    private FirebaseFirestore firebaseFirestore;
    private RecyclerView flist;
    private FirestoreRecyclerAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transactions_list);
        firebaseFirestore=FirebaseFirestore.getInstance();
        flist=findViewById(R.id.flist);
        Query query=firebaseFirestore.collection("transactions");
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
                holder.list_to.setText(model.getTimestamp());
                if(model.getStatus().equals("unpaid")){
                    holder.status1.setVisibility(View.VISIBLE);
                    holder.paid.setVisibility(View.INVISIBLE);
                }else {
                    holder.status1.setVisibility(View.INVISIBLE);
                    holder.paid.setVisibility(View.VISIBLE);
                    holder.paid.setText(model.getStatus());
                }
            }
        };
         flist.setHasFixedSize(true);
         flist.setLayoutManager(new LinearLayoutManager(this));
         flist.setAdapter(adapter);
    }

    private class transactionviewholder extends RecyclerView.ViewHolder {
        private TextView list_from,paid;
        private TextView list_to;
        private Button status1;
        public transactionviewholder(@NonNull View itemView) {
            super(itemView);
            list_from=itemView.findViewById(R.id.listfrom);
            list_to=itemView.findViewById(R.id.listto);
            paid=itemView.findViewById(R.id.paid);
            status1=itemView.findViewById(R.id.status1);
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