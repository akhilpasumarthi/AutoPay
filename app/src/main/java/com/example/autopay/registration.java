package com.example.autopay;

import androidx.appcompat.app.AppCompatActivity;

import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.ArrayAdapter;
import android.widget.EditText;

import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

public class registration extends AppCompatActivity {
    EditText registeruser,age;
    private RadioGroup radioSexGroup;
    private RadioButton radioSexButton;
    Button createwallet1,btn;
    FirebaseFirestore fstore;
    String number,g;
    FirebaseAuth fauth;
    //public void dash(View view){
     //   startActivity(new Intent(registration.this,Dashboard.class));
    //}
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        fauth=FirebaseAuth.getInstance();
        fstore=FirebaseFirestore.getInstance();
        registeruser=findViewById(R.id.registeruser);
        number = getIntent().getStringExtra("number");
        age=findViewById(R.id.age);
        radioSexGroup = (RadioGroup) findViewById(R.id.radioSex);
        int selectedId = radioSexGroup.getCheckedRadioButtonId();
        radioSexButton = (RadioButton) findViewById(selectedId);
        g=radioSexButton.getText().toString();
        createwallet1=(Button) findViewById(R.id.createwallet1);
        btn=(Button) findViewById(R.id.btn1);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // CollectionReference cities = fstore.collection("user");
               // Query query = cities.whereEqualTo("name", "prashanth");
               ethereum r=new ethereum();
               r.createWallet(v);
            }
        });
        createwallet1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String reguser=registeruser.getText().toString().trim();
                String regage=age.getText().toString().trim();
               FirebaseUser userid=FirebaseAuth.getInstance().getCurrentUser();

                System.out.println(userid);
                System.out.println("akhil");
                DocumentReference documentReference=fstore.collection("users").document(userid.getUid());

                Map<String,Object> user=new HashMap<>();
                user.put("name",reguser);
                user.put("gender",g);
                user.put("age",regage);
                user.put("number",number);
                user.put("rfid","number");
                user.put("wallet","address");
                user.put("privatekey","private");
                documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(registration.this, "Created wallet Successful", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(registration.this,Dashboard.class));
                    }
                });
            }
        });
    }

}