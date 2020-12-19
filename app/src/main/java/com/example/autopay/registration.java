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
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class registration extends AppCompatActivity {
    EditText registeruser,age;
    private RadioGroup radioSexGroup;
    private RadioButton radioSexButton;
    Button createwallet1;
    FirebaseFirestore fstore;
    String number,g;
    FirebaseAuth fauth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fauth=FirebaseAuth.getInstance();
        fstore=FirebaseFirestore.getInstance();
        setContentView(R.layout.activity_registration);
        registeruser=findViewById(R.id.registeruser);
        number = getIntent().getStringExtra("number");
        age=findViewById(R.id.age);
        radioSexGroup = (RadioGroup) findViewById(R.id.radioSex);
        int selectedId = radioSexGroup.getCheckedRadioButtonId();
        radioSexButton = (RadioButton) findViewById(selectedId);
        g=radioSexButton.getText().toString();
        createwallet1=(Button) findViewById(R.id.createwallet1);
        createwallet1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String reguser=registeruser.getText().toString().trim();
                String regage=age.getText().toString().trim();
               FirebaseUser userid=FirebaseAuth.getInstance().getCurrentUser();

                System.out.println(userid);
                System.out.println("akhil");
                DocumentReference documentReference=fstore.collection("user").document(userid.getUid());

                Map<String,Object> user=new HashMap<>();
                user.put("name",reguser);
                user.put("gender",g);
                user.put("age",regage);
                user.put("number","8520949757");
                user.put("rfid","number");
                user.put("wallet","address");
                user.put("privatekey","private");
                user.put("token","tokennumber");
                documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(registration.this, "Created wallet Successful", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

}