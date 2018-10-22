package client.core;

import common.networking.Message;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class NetworkClient {
    String address;
    int port;
    DataInputStream in;
    DataOutputStream out;
    Socket s;

    public NetworkClient(String address, int port) {
        this.address = address;
        this.port = port;
    }

    public void connect() throws IOException {
        s = new Socket(address, port);
        in = new DataInputStream(s.getInputStream());
        out = new DataOutputStream(s.getOutputStream());
    }

    public void send(Message m) throws IOException {
        byte[] bytes = m.getBytes();
        out.write(bytes, 0, bytes.length);
    }

}
