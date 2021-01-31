package com.example.autopay;

import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.WalletUtils;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.response.EthGetBalance;
import org.web3j.protocol.core.methods.response.EthGetTransactionCount;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.protocol.core.methods.response.Web3ClientVersion;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.Transfer;
import org.web3j.utils.Convert;

import java.io.File;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.security.Provider;
import java.security.Security;
import java.util.HashMap;
import java.util.Map;

public class ethereum extends AppCompatActivity {

    public Web3j web3;
    public final String password = "abc123";
    public String walletPath;
    public File walletDir;
    public File wallet;
    public File wallet1;
    public File wp;
    public String res_msg;
    public String address;
    public String address1;
    public String storage;
    String hash="";

    FirebaseFirestore firebaseFirestore;
    FirebaseAuth firebaseAuth;
    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ethereum);
        setupBouncyCastle();
        wp=Environment.getExternalStorageDirectory();
        //walletPath="/storage/emulated/0/documents";
        walletPath=wp.getAbsolutePath();
        //toastAsync(walletPath);

    }

    public String connectToEthNetwork(View v) {
        setupBouncyCastle();
        wp=Environment.getExternalStorageDirectory();
        walletPath=wp.getAbsolutePath();
        FirebaseUser userid= FirebaseAuth.getInstance().getCurrentUser();
        String password= userid.getUid();
        // toastAsync("Connecting to Ethereum network...");
        // FIXME: Add your own API key here
        web3 = Web3j.build(new HttpService("https://ropsten.infura.io/v3/91b956a485de4d7681f8c1e82c65b4b9"));
        try {
            Web3ClientVersion clientVersion = web3.web3ClientVersion().sendAsync().get();
            if(!clientVersion.hasError()){
                String m1="Connected!";
                return m1;
            }
            else {
                String m2=clientVersion.getError().getMessage();
                return m2;
            }
        } catch (Exception e) {
            String m3=e.getMessage();
            return m3;
        }
    }

    public String createWallet(View v){
        setupBouncyCastle();
        wp=Environment.getExternalStorageDirectory();
        walletPath=wp.getAbsolutePath();
        FirebaseUser userid= FirebaseAuth.getInstance().getCurrentUser();
        String password= userid.getUid();
        //String password= "abc123";
        try{
            walletDir = new File(walletPath + "/" );
            String fileName =  WalletUtils.generateLightNewWalletFile(password,walletDir);
            wallet=new File(walletPath+"/"+fileName);
            //res_msg="wallet created";
            //return res_msg;
            Log.i("wallet file",String.valueOf(wallet));
            return String.valueOf(wallet);

            //Toast.makeText(this,"Wallet generated", Toast.LENGTH_LONG).show();
            //toastAsync("Wallet generated");
        }
        catch (Exception e){
            res_msg="error created";
            return res_msg;
            //String e1=e.toString();
           //Toast.makeText(this,e1,Toast.LENGTH_LONG).show();
           //toastAsync(e.getMessage());
        }
    }

    public String getAddress(View v){

        setupBouncyCastle();
        wp=Environment.getExternalStorageDirectory();
        walletPath=wp.getAbsolutePath();
        FirebaseUser userid= FirebaseAuth.getInstance().getCurrentUser();
        String password= userid.getUid();
        //String password= "abc123";
        try {
            firebaseFirestore= FirebaseFirestore.getInstance();
            firebaseAuth = FirebaseAuth.getInstance();
            //wallet1=new File(walletPath+"/"+"UTC--2021-01-24T10-29-55.3Z--1adad368f62d64bb4a6f7af8327ba03dfd7d319a.json");
            Credentials credentials = WalletUtils.loadCredentials(password, wallet);
            address1=credentials.getAddress();
            BigInteger pk=credentials.getEcKeyPair().getPrivateKey();
            String key=pk.toString(16);
            Map<String,Object> upk=new HashMap<>();
            upk.put("privatekey",key);
            firebaseFirestore.collection("users")
                    .document(firebaseAuth.getCurrentUser().getUid()).update(upk).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Log.i("added","Successfully");
                }
            });
            return address1;
        }
        catch (Exception e){
            address1="Error in getting the address";
            return address1;
        }
    }

    public String sendTransaction(View v,long p, String address,String storage){
        setupBouncyCastle();
        wp=Environment.getExternalStorageDirectory();
        walletPath=wp.getAbsolutePath();
        FirebaseUser userid= FirebaseAuth.getInstance().getCurrentUser();
        String password= userid.getUid();
        //String password= "abc123";
        try{
            wallet1=new File(storage);
            Credentials credentials = WalletUtils.loadCredentials(password, wallet1);
            TransactionReceipt receipt = Transfer.sendFunds(web3,credentials,address,new BigDecimal(p),Convert.Unit.SZABO).sendAsync().get();
            hash=receipt.getTransactionHash();
            //wallet1=new File(walletPath+"/"+"UTC--2020-12-14T09-48-01.2Z--44910ea2d5263c7a61d22e500d44d7622489fd9b.json");
            return hash;
        }
        catch (Exception e){
            String msg="Error in getting the address";
            return msg;
            //toastAsync(e.getMessage());
        }
    }


    public String showBalance(View v,String waddress) {
        try {
            Log.i("wallet address",waddress);
            EthGetBalance balanceWei = web3.ethGetBalance(waddress, DefaultBlockParameterName.LATEST).sendAsync().get();
            //toastAsync("Balance: " +balanceWei.getBalance());
            BigInteger bal=balanceWei.getBalance();
            java.math.BigDecimal tokenvalue=Convert.fromWei(String.valueOf(bal),Convert.Unit.SZABO);
            String totalbalance= String.valueOf(tokenvalue);
            Log.i("wallet balance",totalbalance);
            //return address;
            return totalbalance;
        }
        catch (Exception e){
            toastAsync(e.getMessage());
           // address="error";
            //return address;
            return "0.0";
        }
    }

    public void showTransactions(View view) {

        try {
            EthGetTransactionCount result=web3.ethGetTransactionCount("0x44910ea2d5263c7a61d22e500d44d7622489fd9b", DefaultBlockParameter.valueOf("latest")).sendAsync().get();
            toastAsync("No.of Transactions : " + result.getTransactionCount());
        }
        catch (Exception e){
            toastAsync(e.getMessage());
        }
    }
    public void toastAsync(String message) {

       runOnUiThread(() -> {
            Toast.makeText(this, message, Toast.LENGTH_LONG).show();
        });
    }

    public void setupBouncyCastle() {
        final Provider provider = Security.getProvider(BouncyCastleProvider.PROVIDER_NAME);
        if (provider == null) {
            return;
        }
        if (provider.getClass().equals(BouncyCastleProvider.class)) {
            return;
        }
        Security.removeProvider(BouncyCastleProvider.PROVIDER_NAME);
        Security.insertProviderAt(new BouncyCastleProvider(), 1);
    }

}