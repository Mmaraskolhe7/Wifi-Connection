package com.manoj.wifi;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    ListView listView;
    ArrayList<String> wifiList = new ArrayList<>();
   MyAdapter adapter;
   List <ScanResult>scanResults;
  static WifiManager wifiManager;
static int level;
static boolean flag;
static ConnectivityManager connectivityManager ;
WifiConfiguration conf;
LinearLayout linearLayout;
RelativeLayout relativeLayout;
CountDownTimer countDownTimer;
ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        linearLayout = findViewById(R.id.mrellout1);
        listView = findViewById(R.id.listView);
         adapter = new MyAdapter(this,wifiList);
         relativeLayout = findViewById(R.id.mrellout3);
        listView.setAdapter(adapter);
        wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        if(!wifiManager.isWifiEnabled()) wifiManager.setWifiEnabled(true);
        progressDialog = new ProgressDialog(this);
        scanWifi();
           listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
               @Override
               public void onItemClick(final AdapterView<?> parent, View view, int position, long id) {

                   if(!flag) {
                          connect(wifiList.get(position),null);
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
                                   linearLayout.setActivated(false);
                                   progressDialog.dismiss();
                                   parent.setEnabled(false);
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

                   else {
                   Intent intent = new Intent(MainActivity.this,JoinWifi.class);
                   intent.putExtra("SSID",scanResults.get(position).SSID);
                   startActivity(intent);}
               }
           });
           connectivityManager = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
    }
    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
           scanResults = wifiManager.getScanResults();
           unregisterReceiver(this);
           for (ScanResult scanResult:scanResults){
              String capabilities= scanResult.capabilities;
               level = WifiManager.calculateSignalLevel(scanResult.level, 4);
               if (capabilities.toUpperCase().contains("WEP")) {
                   flag = true;
               } else if (capabilities.toUpperCase().contains("WPA")
                       || capabilities.toUpperCase().contains("WPA2")) {
                   // WPA or WPA2 Network
                   flag = true;
               } else {
                         flag = false;
               }
               wifiList.add(scanResult.SSID);

               adapter.notifyDataSetChanged();


           }
        }
    };
    public void scanWifi(){
        wifiList.clear();
        registerReceiver(broadcastReceiver,new IntentFilter(wifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
        wifiManager.startScan();
        Toast.makeText(this, "Scanning", Toast.LENGTH_SHORT).show();
    }
    @Override
    public void onResume()
    {
        super.onResume();

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
        {
            if(checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
            {
                requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 87);
            }
        }
    }
    private void connect(String ssid,String key ){
        conf = new WifiConfiguration();
        conf.SSID = "\"" + ssid + "\"";
        if (key == null) {
            conf.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
        } else {
            conf.preSharedKey = "\"" + key + "\"";
        }
        int netID = wifiManager.addNetwork(conf);
        wifiManager.disconnect();
         wifiManager.enableNetwork(netID, true);
        wifiManager.reconnect();
    }
    public void ok(View view){

    }



}
