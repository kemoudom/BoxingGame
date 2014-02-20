package com.challenge.communication;


import java.io.IOException;
import java.util.UUID;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.util.Log;

public class BluetoothServer extends Thread {
    private final BluetoothServerSocket blueServerSocket;
    private BluetoothAdapter blueAdapter;
    private static String APPNAME = "GameBoxing";

    public BluetoothServer(BluetoothAdapter blueAdapter_) {
    	this.blueAdapter=blueAdapter_;
        // On utilise un objet temporaire qui sera assigne plus tard  blueServerSocket car blueServerSocket est "final"
        BluetoothServerSocket tmp = null;
        try {
            // MON_UUID est l'UUID (comprenez identifiant serveur) de l'application. Cette valeur est n�cessaire c�t� client �galement !
            tmp = blueAdapter.listenUsingRfcommWithServiceRecord(APPNAME, UUID.fromString("c065af87-b800-4bb3-a932-c4c130f2a50d"));
        } catch (IOException e) { }
        blueServerSocket = tmp;
    }

    public void run() {
        BluetoothSocket blueSocket = null;
        // On attend une erreur ou une connexion entrante
        while (true) {
            try {
                blueSocket = blueServerSocket.accept();
            } catch (IOException e) {
                break;
            }
            // Si une connexion est accept�e
            if (blueSocket != null) {
                // On fait ce qu'on veut de la connexion (dans un thread separe), à vous de la creer
                manageConnectedSocket(blueSocket);
                try {
					blueServerSocket.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
                break;
            }
        }
    }
    
    private void manageConnectedSocket(BluetoothSocket blueSocket)
    {
    	Log.i("BTserver","manageConnectedSocked");
    }
    // On stoppe l'�coute des connexions et on tue le thread
    public void cancel() {
        try {
            blueServerSocket.close();
        } catch (IOException e) { }
    }
}