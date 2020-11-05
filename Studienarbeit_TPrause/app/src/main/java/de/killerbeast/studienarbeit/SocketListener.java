package de.killerbeast.studienarbeit;

import android.util.Log;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.Objects;

import de.killerbeast.studienarbeit.interfaces.Interface_SocketListener;

public class SocketListener implements Runnable {

    private final Socket socket;
    private final Interface_SocketListener caller;

    public SocketListener(Interface_SocketListener caller, Socket socket) {


        Log.wtf("socketlistener", "created");

        this.caller = caller;
        this.socket = socket;

    }

    private void startListener() {

        try {

            BufferedReader br = null;
            if (socket.isConnected() && !socket.isClosed()) br = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            while (socket.isConnected()) {

                if (!socket.isClosed()) {

                    String msg = Objects.requireNonNull(br).readLine();

                    if (msg != null && !msg.equals("")) {

                        if (msg.contains("Chat is active")) {
                            caller.received(msg);
                            break;
                        }

                        caller.received(msg);

                    }

                } else {

                    caller.warn();

                }

            }

        } catch (IOException e) {

            e.printStackTrace();

        }

    }

    @Override
    public void run() {
        startListener();
    }

    public void disconnect() {

        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
