package com.mobile.kuryeapp.kuryeappv01;


import com.github.nkzawa.socketio.client.Socket;
import org.json.JSONObject;

public class SocketIOmethods {

    public static void sendCoords(Socket mSocket, JSONObject coords) {
        if (coords == null) {
            return;
        }
        mSocket.emit("new message", coords);
    }
}
