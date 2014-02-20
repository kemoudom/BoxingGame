package com.challenge.communication;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.Set;
import java.util.UUID;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.challenge.boxinggame.R;

@SuppressLint("NewApi")
public class BluetoothActivity extends Activity {
	ArrayAdapter mArrayAdapter;
	BluetoothAdapter mBluetoothAdapter;
	BluetoothDevice htcDada;
	BluetoothDevice htcC;
	private static String APPNAME = "BTAPPDELAMORT";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth);
        TextView tv = (TextView) findViewById(R.id.tv);
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
     
        
        if (mBluetoothAdapter == null) {
            // Le terminal ne possède pas le Bluetooth
        	tv.setText("Pas de Bluetooth");
        }
        else
        {
        	if (!mBluetoothAdapter.isEnabled()) {
        	    System.out.println("Enabling BTAdapter");
        	    mBluetoothAdapter.enable();
        	}
        	System.out.println("Beginning pairing of devices");
        	
        	//On présume que les trucs ont été pairés avant
        	Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();
        	String s = "";
        	// If there are paired devices
        	if (pairedDevices.size() > 0) {
        	    // Loop through paired devices
        	    for (BluetoothDevice device : pairedDevices) {
        	        //Affichage des devices pairés
        	        s+="NAME:"+device.getName() + "\n" + "adress:"+device.getAddress()+"\n";
        	        
        	        
        	        //Je cherche celui dont j'ai besoin (en fait les 2 tel avec lesquels j'ai testé
        	        if (device.getName().equals("BOXER2")) {	// server
        	        	System.out.println("Server");
        	        	this.htcDada=device;
        	        	BluetoothSocket socket = getSocketFromServer();
        	        	Log.e("SOCKET Server==> ", "Connected: "+socket.isConnected());
        	        	// Si une connexion est acceptée
        	            if (socket != null) {
        	                // On fait ce qu'on veut de la connexion (dans un thread séparé), à vous de la créer
        	            	Log.e("Success", "Connected");
        	            	try {
								PrintWriter pr = new PrintWriter(socket.getOutputStream());
								pr.write("asdasdasdads\n");
								//pr.println("abcd");
								//pr.flush();
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
        	            	
        	        	}else{
        	        		Log.e("Failed", "Connected");
        	        	}
        	        }else
        	            if (device.getName().equals("BOXER1")) {	// clients
        	        	this.htcC=device;
        	        	System.out.println("HTCC FOUND");
        	        	BluetoothSocket socket =getSocketFromClient();
        	        	Log.e("SOCKET Server==> ", "Connected: ");
        	        	try {
        	        		InputStreamReader is = new InputStreamReader(socket.getInputStream());
        	        		while (true) {
        	        			char[] buffer = new char[4];
        	        			is.read(buffer);
        	        			System.out.println(buffer.toString());
        	        		}
        	        		/*BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            	        	String str;
            	        	
							while((str = br.readLine())!=null){
								Log.e("Read from server","Text: "+str);
							}*/
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
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
    	Log.i("ServerBT","Starting server");
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
    
    public BluetoothSocket getSocketFromServer(){
    
        // On utilise un objet temporaire qui sera assigné plus tard à blueServerSocket car blueServerSocket est "final"
        BluetoothServerSocket tmp = null;
        try {
            // MON_UUID est l'UUID (comprenez identifiant serveur) de l'application. Cette valeur est nécessaire côté client également !
            tmp = mBluetoothAdapter.listenUsingRfcommWithServiceRecord(APPNAME, UUID.fromString("c065af87-b800-4bb3-a932-c4c130f2a50d"));
        } catch (IOException e) { }
        BluetoothSocket blueSocket;
		try {
			blueSocket = tmp.accept();
			return  blueSocket;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
		return null;
    }
    
    public BluetoothSocket getSocketFromClient(){
    	BluetoothSocket tmp = null;
        try {
            // MON_UUID est l'UUID (comprenez identifiant serveur) de l'application. Cette valeur est nécessaire côté client également !
            tmp = htcC.createRfcommSocketToServiceRecord(UUID.fromString("c065af87-b800-4bb3-a932-c4c130f2a50d"));
        } catch (IOException e) { }
    	//mBluetoothAdapter.cancelDiscovery();
    	try {
            // On se connecte. Cet appel est bloquant jusqu'à la réussite ou la levée d'une erreur
    		tmp.connect();
        } catch (IOException connectException) {
            /*// Impossible de se connecter, on ferme la socket et on tue le thread
            try {
            	tmp.close();
            } catch (IOException closeException) { }*/
        	connectException.printStackTrace();

        }
    	
    	return tmp;
    }
}
