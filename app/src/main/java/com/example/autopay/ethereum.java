package com.example.autopay;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import android.os.Environment;
import android.view.View;
import android.widget.Toast;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.ECKeyPair;
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
import java.security.KeyPairGenerator;
import java.security.Provider;
import java.security.Security;
import android.content.Context.*;
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

    public void connectToEthNetwork(View v) {
        toastAsync("Connecting to Ethereum network...");
        // FIXME: Add your own API key here
        web3 = Web3j.build(new HttpService("https://ropsten.infura.io/v3/91b956a485de4d7681f8c1e82c65b4b9"));
        try {
            Web3ClientVersion clientVersion = web3.web3ClientVersion().sendAsync().get();
            if(!clientVersion.hasError()){
                toastAsync("Connected!");
            }
            else {
                toastAsync(clientVersion.getError().getMessage());
            }
        } catch (Exception e) {
            toastAsync(e.getMessage());
        }
    }

    public String createWallet(View v){


        setupBouncyCastle();
        wp=Environment.getExternalStorageDirectory();
        walletPath=wp.getAbsolutePath();
         String password= "abc123";


        try{
            walletDir = new File(walletPath + "/" );
            String fileName =  WalletUtils.generateLightNewWalletFile(password,walletDir);
            wallet=new File(walletPath+"/"+fileName);
            res_msg="wallet created";
            return res_msg;
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
        String password= "abc123";
        try {
            //wallet1=new File(walletPath+"/"+"UTC--2020-12-21T16-10-19.1Z--ae53d8f385866a6bc876a91908b12ae1e2a1af73.json");
            Credentials credentials = WalletUtils.loadCredentials(password, wallet);
            address=credentials.getAddress();
            return address;
        }
        catch (Exception e){
            address="Error in getting the address";
            return address;
        }
    }

    public void sendTransaction(View v){
        try{
            wallet1=new File(walletPath+"/"+"UTC--2020-12-14T09-48-01.2Z--44910ea2d5263c7a61d22e500d44d7622489fd9b.json");
            Credentials credentials = WalletUtils.loadCredentials(password, wallet1);
            TransactionReceipt receipt = Transfer.sendFunds(web3,credentials,"0x5a1f4e88944b0262760d802d3304578592950462",new BigDecimal(1),Convert.Unit.WEI).sendAsync().get();
            toastAsync("Transaction complete: " +receipt.getTransactionHash());
        }
        catch (Exception e){
            toastAsync(e.getMessage());
        }
    }


    public void showBalance(View v) {
        try {
            EthGetBalance balanceWei = web3.ethGetBalance("0x44910ea2d5263c7a61d22e500d44d7622489fd9b", DefaultBlockParameterName.LATEST).sendAsync().get();
            toastAsync("Balance: " +balanceWei.getBalance());
        }
        catch (Exception e){
            toastAsync(e.getMessage());
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