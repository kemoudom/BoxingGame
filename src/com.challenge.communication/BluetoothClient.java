package com.challenge.communication;

import java.io.IOException;
import java.util.UUID;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.util.Log;

class BluetoothClient extends Thread {
    private final BluetoothSocket blueSocket;
    private final BluetoothDevice blueDevice;
    private BluetoothAdapter blueAdapter;

    public BluetoothClient(BluetoothDevice device, BluetoothAdapter blueAdapter_) {
        // On utilise un objet temporaire car blueSocket et blueDevice sont "final"
        BluetoothSocket tmp = null;
        blueDevice = device;
        this.blueAdapter=blueAdapter_;

        // On recupere un objet BluetoothSocket grace à l'objet BluetoothDevice
        try {
            // MON_UUID est l'UUID (comprenez identifiant serveur) de l'application. Cette valeur est nécessaire coté serveur egalement !
            tmp = device.createRfcommSocketToServiceRecord(UUID.fromString("c065af87-b800-4bb3-a932-c4c130f2a50d"));
        } catch (IOException e) { }
        blueSocket = tmp;
    }

    public void start() {
        // On annule la decouverte des peropheriques (inutile puisqu'on est en train d'essayer de se connecter)
        blueAdapter.cancelDiscovery();

        try {
            // On se connecte. Cet appel est bloquant jusqu'à la reussite ou la levee d'une erreur
            blueSocket.connect();
        } catch (IOException connectException) {
            // Impossible de se connecter, on ferme la socket et on tue le thread
            try {
                blueSocket.close();
            } catch (IOException closeException) { }
            Log.i("BTClient", "Fail to connect client socket");
            return;
        }
        // Utilisez la connexion (dans un thread separe) pour faire ce que vous voulez
        ConnectedThreadReading connectedThreadReading = new ConnectedThreadReading(blueSocket);
        connectedThreadReading.start();
        ConnectedThreadWriting connectedThreadWriting = new ConnectedThreadWriting(blueSocket);
        connectedThreadWriting.start();
    }

    // Annule toute connexion en cours et tue le thread
    public void cancel() {
        try {
            blueSocket.close();
        } catch (IOException e) { }
    }
    
    public void manageConnectedSocket(BluetoothSocket blueSocket) {
    	Log.i("BTClient", "ManageConnectedSocket");
        // here we put the hit on buttons !
    }
}