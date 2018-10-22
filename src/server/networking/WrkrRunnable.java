package server.networking;

import common.networking.Message;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.nio.ByteBuffer;

public class WrkrRunnable implements Runnable {
	protected Socket clntSocket = null;
	protected String txtFrmSrvr = null;

	public WrkrRunnable(Socket clntSocket, String txtFrmSrvr) {
		this.clntSocket = clntSocket;
		this.txtFrmSrvr = txtFrmSrvr;
	}

	DataInputStream in;
	DataOutputStream out;

	public void run() {
		try {
			in = new DataInputStream(clntSocket.getInputStream());
			out = new DataOutputStream(clntSocket.getOutputStream());

            while (true) {
                int size = in.readInt();
                System.out.println(size);
                ByteBuffer b = ByteBuffer.allocate(size);
                b.putInt(size);
                while (b.hasRemaining()) {
                    b.put(in.readByte());
                }
                Message m = Message.getMessageFromBuffer(b);
                System.out.println(m.toString());
                System.out.println("processed input");
                if (false)
                    break;
            }

			out.close();
			in.close();
            System.out.println("done");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}