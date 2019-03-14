package com.example.naytonwilkerson.conexaop2p;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
import android.net.wifi.WifiManager;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pDeviceList;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static com.example.naytonwilkerson.conexaop2p.R.layout.activity_main;


public class MainActivity extends Activity {

    WifiP2pManager mManager;
    WifiP2pManager.Channel mChannel;
    BroadcastReceiver mReceiver;
    IntentFilter intentFilter;
    WifiP2pDeviceList peerList;
    WifiManager wifiManager;
    Button btnOnOff, btnInform, btnbusca;
    // ListView listView;
    private Spinner spn1, spn2, spn3;


    // private List<WifiP2pDevice> peers = new ArrayList<WifiP2pDevice>();
    //private List<String> arrayList = Arrays.asList("android_metadata", "test1", "test5", "test4", /*...*/ "test21", "test12");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(activity_main);

        btnOnOff = (Button) findViewById(R.id.onOff);
        btnInform = (Button) findViewById(R.id.sendBut);
        btnbusca = (Button) findViewById(R.id.busca);

        wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);

        mManager = (WifiP2pManager) getSystemService(Context.WIFI_P2P_SERVICE);
        mChannel = mManager.initialize(this, getMainLooper(), null);
        mReceiver = new WiFiDirectBroadcastReceiver(mManager, mChannel, this, 15);

        intentFilter = new IntentFilter();
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION);
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION);
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION);
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION);


        exqListener();


        btnbusca.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mManager.discoverPeers(mChannel, new WifiP2pManager.ActionListener() {
                    @Override
                    public void onSuccess() {
                        Toast.makeText(MainActivity.this, "Iniciando Descoberta",
                                Toast.LENGTH_SHORT).show();

                    }

                    @Override
                    public void onFailure(int reasonCode) {
                        Toast.makeText(MainActivity.this, "Acione WIFI para iniciar descoberta " + reasonCode,
                                Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });


    }


    public void preencherSpinner(Collection<WifiP2pDevice> peers) {
        Toast.makeText(this, "Preenchendo a lista de dispositivos: "+peers.size(), Toast.LENGTH_SHORT).show();
        //List<String> lista = new ArrayList<String>();
        TextView view = findViewById(R.id.textView);
        for (WifiP2pDevice d : peers) {
            view.append("\n"+d.deviceName+" "+d.deviceAddress+"\n");
            Toast.makeText(this, "entrou "+peers.size(), Toast.LENGTH_SHORT).show();
            //lista.add(d.deviceName);

            //Toast.makeText(this, d.deviceName + "  " + d.primaryDeviceType + "  " + "DISPONIVEIS", Toast.LENGTH_SHORT).show();

            //Toast.makeText(context, Disp+"Dispositivos disponiveis", Toast.LENGTH_LONG).show();

        }

    }


    private void exqListener() {

        btnOnOff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (wifiManager.isWifiEnabled()) {
                    wifiManager.setWifiEnabled(false);
                    btnOnOff.setText("WIFI OFF");
                } else {
                    wifiManager.setWifiEnabled(true);
                    btnOnOff.setText("WIFI ON");
                }
            }
        });


    }


    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(mReceiver, intentFilter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(mReceiver);
    }
}
