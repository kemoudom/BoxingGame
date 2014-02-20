package com.challenge.communication;


import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.os.Handler;
import android.util.Log;

public class BluetoothServer extends Thread {

    private final BluetoothServerSocket blueServerSocket;
    private BluetoothAdapter blueAdapter;
    private static String APPNAME = "GameBoxing";
    private ConnectedServerThread mConnectedThread;

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

    public void start() {
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

                // Start the thread to manage the connection and perform transmissions
                mConnectedThread = new ConnectedServerThread(blueSocket);
                mConnectedThread.start();

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

    // server implementation ! ! !

    class ConnectedServerThread extends Thread {

        private final BluetoothSocket mmSocket;
        private final InputStream mmInStream;
        private final OutputStream mmOutStream;
        private Handler mHandler;

        public ConnectedServerThread(BluetoothSocket socket) {
            mmSocket = socket;
            InputStream tmpIn = null;
            OutputStream tmpOut = null;

            // Get the input and output streams, using temp objects because
            // member streams are final
            try {
                tmpIn = socket.getInputStream();
                tmpOut = socket.getOutputStream();
            }
            catch (IOException e) {
                e.printStackTrace();
            }

            mmInStream = tmpIn;
            mmOutStream = tmpOut;
        }

        public void start() {
            byte[] buffer = new byte[1024];  // buffer store for the stream
            int bytes; // bytes returned from read()

            // Keep listening to the InputStream until an exception occurs
            while (true) {
                try {
                    // Read from the InputStream
                    bytes = mmInStream.read(buffer);
                    // Send the obtained bytes to the UI activity
//                    mHandler.obtainMessage(MESSAGE_READ, bytes, -1, buffer)
//                            .sendToTarget();
                } catch (IOException e) {
                    break;
                }
            }
        }

        /* Call this from the main activity to send data to the remote device */
        public void write(byte[] bytes) {
            try {
                mmOutStream.write(bytes);
            } catch (IOException e) { }
        }

        /* Call this from the main activity to shutdown the connection */
        public void cancel() {
            try {
                mmSocket.close();
            } catch (IOException e) { }
        }
    }
}