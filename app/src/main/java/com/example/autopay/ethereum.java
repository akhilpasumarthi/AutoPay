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

    private Web3j web3;
    private final String password = "abc123";
    private String walletPath;
    private File walletDir;
    private File wallet;
    private File wallet1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ethereum);
        setupBouncyCastle();
        walletPath="/storage/emulated/0/documents";


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

    public void createWallet(View v){

        try{
            walletDir = new File(walletPath + "/" );
            String fileName =  WalletUtils.generateLightNewWalletFile(password,walletDir);
            wallet=new File(walletPath+"/"+fileName);

            //toastAsync(walletDir.toString());
            toastAsync("Wallet generated");
        }
        catch (Exception e){
            toastAsync(e.getMessage());
        }
    }

    public void getAddress(View v){
        try {
            wallet1=new File(walletPath+"/"+"UTC--2020-12-14T09-48-01.2Z--44910ea2d5263c7a61d22e500d44d7622489fd9b.json");
            Credentials credentials = WalletUtils.loadCredentials(password, wallet1);
            toastAsync("Your address is " + credentials.getAddress());
        }
        catch (Exception e){
            toastAsync(e.getMessage());
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

    private void setupBouncyCastle() {
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