package com.challenge.communication;


import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.challenge.boxinggame.R;

import java.util.Set;

public class ConnexionManager extends Activity{
    // Name of the connected device
    private String mConnectedDeviceName = null;

    BluetoothAdapter mBluetoothAdapter;
    BluetoothDevice htcDada;
    BluetoothDevice htcC;

    // message handling constant
    // Intent request codes
    private static final int REQUEST_CONNECT_DEVICE = 1;
    private static final int REQUEST_ENABLE_BT = 2;

    // Message types sent from the BluetoothChatService Handler
    public static final int MESSAGE_STATE_CHANGE = 1;
    public static final int MESSAGE_READ = 2;
    public static final int MESSAGE_WRITE = 3;
    public static final int MESSAGE_DEVICE_NAME = 4;

    // Key names received from the BluetoothCommandService Handler
    public static final String DEVICE_NAME = "BOXER1";

    public static final int MESSAGE_PUNCH_LEFT = 10;
    public static final int MESSAGE_PUNCH_RIGHT = 11;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth);
        TextView tv = (TextView) findViewById(R.id.tv);
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        Button sendOne = (Button)findViewById(R.id.sendOne);
        Button sendTwo = (Button)findViewById(R.id.sendTwo);
        // setting buttons action
        sendOne.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {


                    }
                }
        );

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
                    if (device.getName().equals("BOXER1")) {
                        System.out.println("Boxer1 FOUND");
                        this.htcDada=device;

                        // setting up client and server socket
                        BluetoothServer server = new BluetoothServer(mBluetoothAdapter);
                        server.start();

                        BluetoothClient client = new BluetoothClient(device, mBluetoothAdapter);
                        client.start();
                    }
                    // TODO: change name later for boxer 1 and boxer 2
                    if (device.getName().equals("BOXER2")) {
                        // client only
                        this.htcC=device;
                        System.out.println("Boxer2 FOUND");

                        BluetoothClient client = new BluetoothClient(device, mBluetoothAdapter);
                        client.run();
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
//        BluetoothServer btserv = new BluetoothServer(mBluetoothAdapter);
//        btserv.start();
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
        //BluetoothClient btclient = new BluetoothClient(this.htcDada, mBluetoothAdapter); //le client est HTCC
        //btclient.start();
    }

    // The Handler that gets information back from the BluetoothChatService
//    private final Handler mHandler = new Handler() {
//        @Override
//        public void handleMessage(Message msg) {
//            switch (msg.what) {
//                case MESSAGE_STATE_CHANGE:
//                    switch (msg.arg1) {
//                        case 1:
//
//                            // notify the view that it sucks !
//                            break;
//                    }
//                    break;
//                case MESSAGE_DEVICE_NAME:
//                    // save the connected device's name
//                    mConnectedDeviceName = msg.getData().getString(DEVICE_NAME);
//                    break;
//                case MESSAGE_PUNCH_LEFT:
//                    // WE HAVE TO NOTOFY THE VIEW to avoid left !
//                    break;
//                case MESSAGE_PUNCH_RIGHT:
//                    // WE HAVE TO NOTOFY THE VIEW to avoid right!
//                    break;
//            }
//        }
//    };
}
