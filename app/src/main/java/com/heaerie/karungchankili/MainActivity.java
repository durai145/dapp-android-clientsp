package com.heaerie.karungchankili;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import org.web3j.crypto.CipherException;
import org.web3j.crypto.WalletUtils;
import org.web3j.protocol.Web3j;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.protocol.core.methods.response.Web3ClientVersion;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.RawTransactionManager;
import org.web3j.tx.TransactionManager;
import org.web3j.tx.Transfer;
import org.web3j.utils.Convert;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;


public class MainActivity extends AppCompatActivity {

    private final static String PRIVATE_KEY = "5E4AABBA8DEF19E8EF5D78C5F02828C5AA6FCAECB2D8A2417A0DCC769203F597";

    private final static BigInteger GAS_LIMIT = BigInteger.valueOf(6721975L);
    private final static BigInteger GAS_PRICE = BigInteger.valueOf(20000000000L);

    private final static String RECIPIENT = "0xaaF8CcF67D31F256E56188B31D26bCBB79092f6F";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Thread thread = new Thread(new Runnable() {

            @Override
            public void run() {
                try  {
                    mainProcess();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        try {
            thread.start();
        } catch (Exception e) {
            System.out.print(e);
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

     public void mainProcess() throws Exception {
        // Web3j web3j = Web3j.build(new HttpService("http://192.168.1.152:8635"));
         Web3j web3j = Web3j.build(new HttpService("https://rinkeby.infura.io/ether"));


        TransactionManager transactionManager = new RawTransactionManager(
                web3j,
                getCredentialsFromPrivateKey()
        );


         CholaTransfer transfer = new CholaTransfer(web3j, transactionManager);

        TransactionReceipt transactionReceipt = transfer.sendFunds(
                RECIPIENT,
                BigDecimal.valueOf(0.001),
                Convert.Unit.ETHER,
                GAS_PRICE,
                GAS_LIMIT
        ).send();

        System.out.print("Transaction = " + transactionReceipt.getTransactionHash());
    }

    private void printWeb3Version(Web3j web3j) {
        Web3ClientVersion web3ClientVersion = null;
        try {
            web3ClientVersion = web3j.web3ClientVersion().send();
        } catch (IOException e) {
            e.printStackTrace();
        }
        String web3ClientVersionString = web3ClientVersion.getWeb3ClientVersion();
        System.out.println("Web3 client version: " + web3ClientVersionString);
    }

    private Credentials getCredentialsFromWallet() throws IOException, CipherException {
        return WalletUtils.loadCredentials(
                "passphrase",
                "wallet/path"
        );
    }



    private Credentials getCredentialsFromPrivateKey() {
        return Credentials.create(PRIVATE_KEY);
    }
}
