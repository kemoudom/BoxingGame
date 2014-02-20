package com.challenge.communication;


import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.challenge.boxinggame.R;

import java.util.Set;

public class ConnexionManager extends Activity{
    ArrayAdapter mArrayAdapter;
    BluetoothAdapter mBluetoothAdapter;
    BluetoothDevice htcDada;
    BluetoothDevice htcC;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth);
        TextView tv = (TextView) findViewById(R.id.tv);
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        if (mBluetoothAdapter == null) {
            tv.setText("Pas de Bluetooth");
        }
        else
        {
            if (!mBluetoothAdapter.isEnabled()) {
                System.out.println("Enabling BTAdapter");
                mBluetoothAdapter.enable();
            }
            System.out.println("Beginning pairing of devices");
        	/*Set<BluetoothDevice> pairedDevices = blueAdapter.getBondedDevices();
        	for (int i=0 ; i< pairedDevices.toArray().length ; i++) {
        		System.out.println(pairedDevices.toArray()[i].toString());
        	}*/

            //On pr�sume que les trucs ont �t� pair�s avant
            Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();
            String s = "";
            // If there are paired devices
            if (pairedDevices.size() > 0) {
                // Loop through paired devices
                for (BluetoothDevice device : pairedDevices) {
                    //Affichage des devices pair�s
                    s+="NAME:"+device.getName() + "\n" + "adress:"+device.getAddress()+"\n";
                    //Je cherche celui dont j'ai besoin (en fait les 2 tel avec lesquels j'ai teste
                    if (device.getName().equals("HTC Dada")) {
                        System.out.println("HTDADA FOUND");
                        this.htcDada=device;
                    }
                    if (device.getName().equals("HTC Desire C")) {
                        this.htcC=device;
                        System.out.println("HTCC FOUND");
                    }

                }
                tv.setText(s);
            }


        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    //Lancement du server
    public void serverBT(View view) {
        Log.i("ServerBT", "Starting server");
        BluetoothServer btserv = new BluetoothServer(mBluetoothAdapter);
        btserv.start();
    }

    //Lancement du client
    public void clientBT(View view) {
        Log.i("ClientBT", "Starting client");
        if (this.htcDada == null)
        {
            Log.e("ClientBT", "Device is null");
        }
        else
        {
            System.out.println(this.htcDada.toString());
        }
        BluetoothClient btclient = new BluetoothClient(this.htcDada, mBluetoothAdapter); //le client est HTCC
        btclient.start();
    }
}
