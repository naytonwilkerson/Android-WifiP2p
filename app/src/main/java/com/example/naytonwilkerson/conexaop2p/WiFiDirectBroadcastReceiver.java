package com.example.naytonwilkerson.conexaop2p;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pDeviceList;
import android.net.wifi.p2p.WifiP2pManager;
import android.util.Log;
import android.widget.Toast;


import static android.content.ContentValues.TAG;



public class WiFiDirectBroadcastReceiver extends BroadcastReceiver {

    private WifiP2pManager mManager;
    private WifiP2pManager.Channel mChannel;
    private MainActivity mActivity;
    private int intNumber;
    public DISPOSITIVOS   dispositivos = new DISPOSITIVOS();

    String Disp;



    public WiFiDirectBroadcastReceiver(WifiP2pManager manager, WifiP2pManager.Channel channel, MainActivity activity, int intNumber) {
        super();
        this.mManager = manager;
        this.mChannel = channel;
        this.mActivity = activity;
        this.intNumber = intNumber;
    }

    @Override
    public void onReceive(final Context context, final Intent intent) {
        String action = intent.getAction();


        if (WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION.equals(action)) {
            // Verificar se wifi esta ligado e notificar a atividade apropriada
            int state = intent.getIntExtra(WifiP2pManager.EXTRA_WIFI_STATE, -1);
            if (state == WifiP2pManager.WIFI_P2P_STATE_ENABLED) {
                // Wi-Fi Direct esta habilitado
                Toast.makeText(context, "Wi-Fi Direct esta habilitado", Toast.LENGTH_LONG).show();
                Log.e("WifiDIRECT-","Enabled");

            } else {
                // Wi-Fi Direct nao esta habilitado
                Toast.makeText(context, "Wi-Fi Direct Desabilitado", Toast.LENGTH_LONG).show();
                Log.e("WifiDIRECT-", "Disabled");
            }





        } else if (WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION.equals(action)) {
            // Chamar WifiP2pManager.requestPeers() para obter uma lista dos dispositivos disponiveis
            if(mManager != null) {

                this.mManager.requestPeers(this.mChannel, new WifiP2pManager.PeerListListener (){
                    @Override
                    public void onPeersAvailable(WifiP2pDeviceList peers) {
                        // seu codigo
                        Log.d(TAG, String.format("PeerListListener: %d peers available, updating device list", peers.getDeviceList().size()));

                        Disp = peers.getDeviceList().toString();

                       // dispositivos.setNome(Disp);

                        for(WifiP2pDevice d : peers.getDeviceList()){

                            Toast.makeText(context, d.deviceName+"  "+d.primaryDeviceType+"  "+"DISPONIVEIS", Toast.LENGTH_SHORT).show();

                            //Toast.makeText(context, Disp+"Dispositivos disponiveis", Toast.LENGTH_LONG).show();

                        }

                        return;
                    }
                });
            }
                    return;


        } else if (WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION.equals(action)) {
                // Responder a novas conexoes ou desconexoes
            this.mManager.requestPeers(this.mChannel, new WifiP2pManager.PeerListListener() {
                @Override
                public void onPeersAvailable(WifiP2pDeviceList peers) {

                    if (peers.getDeviceList().toString() == "[]") {
                        Toast.makeText(context, "Sem dispositivos Disponiveis", Toast.LENGTH_LONG).show();

                    } else {

                    Log.d(TAG, String.format("PeerListListener: %d peers available, updating device list", peers.getDeviceList().size()));
                    WifiP2pDeviceList list = intent.getParcelableExtra(WifiP2pManager.EXTRA_P2P_DEVICE_LIST);
                    Toast.makeText(context, "Dispositivos Disponiveis"+peers.getDeviceList().toString(), Toast.LENGTH_LONG).show();

                    // peers = intent.getParcelableExtra(WifiP2pManager.EXTRA_DISCOVERY_STATE);
                    // nullconexao
                    //Toast.makeText(context, peers+"conexão alterada 2222", Toast.LENGTH_LONG).show();
                }

                }
            });







            } else if (WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION.equals(action)) {
                // Responder a mudancas de estado deste dispositivo
            }



    }



}

