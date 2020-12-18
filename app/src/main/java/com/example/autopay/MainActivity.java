package com.example.autopay;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.messaging.FirebaseMessaging;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    TextView register,dummy;
    EditText username,password;
    Button userlogin;
    Button payment;
    FirebaseFirestore fstore;
    String userid;
    FirebaseAuth fauth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        register=findViewById(R.id.register);
        username=findViewById(R.id.username);
        password=findViewById(R.id.password);
        userlogin=findViewById(R.id.userlogin);
        fauth=FirebaseAuth.getInstance();
        fstore=FirebaseFirestore.getInstance();
        dummy=findViewById(R.id.dummy);
        payment=findViewById(R.id.ether);
        dummy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,Transactions_list.class));
            }
        });
        userlogin.setOnClickListener(new View.OnClickListener() {
            String user=username.getText().toString().trim();
            String pass=password.getText().toString().trim();
            @Override
            public void onClick(View v) {
               // userid=fauth.getCurrentUser().getUid();
                DocumentReference documentReference=fstore.collection("userd").document("users");
                Map<String,Object> user=new HashMap<>();
                user.put("username","hii");
                user.put("password","hello");
                documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(MainActivity.this, "success", Toast.LENGTH_SHORT).show();
                        FirebaseMessaging.getInstance().getToken()
                                .addOnCompleteListener(new OnCompleteListener<String>() {
                                    @Override
                                    public void onComplete(@NonNull Task<String> task) {
                                        if (!task.isSuccessful()) {
                                            Log.w("myTag", "Fetching FCM registration token failed", task.getException());
                                            return;
                                        }

                                        // Get new FCM registration token
                                        String token = task.getResult();
                                        Log.w("mySuccessTag", token, task.getException());
                                        // Log and toast
                                        Toast.makeText(MainActivity.this, "Token generated", Toast.LENGTH_SHORT).show();
                                    }
                                });
                    }
                });
            }
        });
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mystring=new String("Register");
                SpannableString content = new SpannableString(mystring);
                content.setSpan(new UnderlineSpan(), 0, mystring.length(), 0);
                register.setText(content);
                startActivity(new Intent(MainActivity.this,registration.class));
            }
        });
        payment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this,ethereum.class));
            }
        });
    }
}