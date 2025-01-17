package com.example.autopay;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;


public class registration extends AppCompatActivity {
    EditText registeruser,age,gmail,pan;
    private RadioGroup radioSexGroup;
    private RadioButton radioSexButton;
    Button createwallet1;
    FirebaseFirestore fstore;
    String number,g;
    FirebaseAuth fauth;
    DatePickerDialog datePickerDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        fauth=FirebaseAuth.getInstance();
        fstore=FirebaseFirestore.getInstance();
        registeruser=findViewById(R.id.registeruser);
        number = getIntent().getStringExtra("number");
        age=findViewById(R.id.age);
        gmail=findViewById(R.id.gmail);
        pan=findViewById(R.id.pan);
        radioSexGroup = (RadioGroup) findViewById(R.id.radioSex);
        int selectedId = radioSexGroup.getCheckedRadioButtonId();
        radioSexButton = (RadioButton) findViewById(selectedId);
        g=radioSexButton.getText().toString();
        createwallet1=(Button) findViewById(R.id.createwallet1);
        age.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // calender class's instance and get current date , month and year from calender
                final Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR); // current year
                int mMonth = c.get(Calendar.MONTH); // current month
                int mDay = c.get(Calendar.DAY_OF_MONTH); // current day
                // date picker dialog
                datePickerDialog = new DatePickerDialog(registration.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                // set day of month , month and year value in the edit text
                                age.setText(dayOfMonth + "/"
                                        + (monthOfYear + 1) + "/" + year);

                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });


        createwallet1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ethereum e=new ethereum();
                String msg=e.createWallet(v);
                String address=e.getAddress(v);
                Toast.makeText(getApplicationContext(),msg, Toast.LENGTH_LONG).show();
                //Toast.makeText(getApplicationContext(),address, Toast.LENGTH_LONG).show();


                String reguser=registeruser.getText().toString().trim();
                String regage=age.getText().toString().trim();
                String ugmail=gmail.getText().toString().trim();
                String upan=pan.getText().toString().trim();
               FirebaseUser userid=FirebaseAuth.getInstance().getCurrentUser();

                DocumentReference documentReference=fstore.collection("users").document(userid.getUid());

                Map<String,Object> user=new HashMap<>();
                user.put("name",reguser);
                user.put("gender",g);
                user.put("dob",regage);
                user.put("number",number);
                user.put("rfid","number");
                user.put("walletaddress",address);
                user.put("privatekey","private");
                user.put("email",ugmail);
                user.put("pan",upan);
                user.put("storagepath",msg);

                documentReference.update(user).addOnSuccessListener(new OnSuccessListener<Void>() {
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