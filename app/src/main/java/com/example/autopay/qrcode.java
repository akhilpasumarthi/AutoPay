package com.example.autopay;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.zxing.WriterException;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;

public class qrcode extends AppCompatActivity {
    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth firebaseAuth;
    ImageView qrImage;
    String qr;
    TextView code;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrcode);
        firebaseFirestore= FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        qrImage = findViewById(R.id.qrPlaceHolder);
        code=(TextView) findViewById(R.id.code);
        firebaseFirestore.collection("users")
                .document(firebaseAuth.getCurrentUser().getUid()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
              //  qr=documentSnapshot.getString("walletaddress");
                code.setText(documentSnapshot.getString("walletaddress"));
                qr=code.getText().toString();
                QRGEncoder qrgEncoder = new QRGEncoder(qr,null, QRGContents.Type.TEXT,500);
                try {
                    Bitmap qrBits = qrgEncoder.encodeAsBitmap();
                    qrImage.setImageBitmap(qrBits);
                } catch (WriterException e) {
                    e.printStackTrace();
                }

            }
        });

        //String data = code.getText().toString();
        //Log.i("the code is",qr);



    }
}