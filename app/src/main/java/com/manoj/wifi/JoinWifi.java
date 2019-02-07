package com.manoj.wifi;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class JoinWifi extends AppCompatActivity {
WifiConfiguration conf;
EditText passKey;
RelativeLayout relativeLayout;
CountDownTimer countDownTimer;
ProgressDialog progressDialog;
WifiManager wifi;
TextView title2;
LinearLayout linearLayout;
Button join;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_wifi);
       passKey= findViewById(R.id.passkey);
         linearLayout = findViewById(R.id.rellout1);
         relativeLayout = findViewById(R.id.rellout3);
        progressDialog = new ProgressDialog(this);
        Intent intent = getIntent();
        wifi = MainActivity.wifiManager;
      join = findViewById(R.id.join);
      title2= findViewById(R.id.title2);
     title2.setText("Enter Password for "+"\""+ intent.getStringExtra("SSID")+"\""+" WiFi Network");


    }
    public void join(View view){
        Intent intent = getIntent();
connect(intent.getStringExtra("SSID"),passKey.getText().toString());

        progressDialog.setMax(100);
        progressDialog.show();
        countDownTimer=new CountDownTimer(25000,1000){
            @Override
            public void onTick(long millisUntilFinished) {

                ConnectivityManager connectivityManager = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo networkInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
                if(networkInfo.isConnected()){
                    relativeLayout.setVisibility(View.VISIBLE);
                    linearLayout.setAlpha(0.2f);
                    passKey.setEnabled(false);
                    join.setEnabled(false);
                    progressDialog.dismiss();
                }

            }

            @Override
            public void onFinish() {
                ConnectivityManager connectivityManager = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo    networkInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
                if(!networkInfo.isConnected()){
                    Toast.makeText(getApplicationContext(), "Unable To Connect", Toast.LENGTH_SHORT).show();

                    progressDialog.dismiss();
                }
                progressDialog.dismiss();

            }
        }.start();

    }
    private void connect(String ssid,String key ){
        conf = new WifiConfiguration();
        conf.SSID = "\"" + ssid + "\"";
        if (key == null) {
            conf.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
        } else {
            conf.preSharedKey = "\"" + key + "\"";
        }
        int netID = wifi.addNetwork(conf);
wifi.disconnect();
        wifi.enableNetwork(netID, true);
        wifi.reconnect();

    }
    public void ok(View view){

    }





}
