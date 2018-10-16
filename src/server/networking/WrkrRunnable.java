package server.networking;

import java.io.*;
import java.net.Socket;
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
			
			long timetaken = System.currentTimeMillis();
			out.write(("OK\n\nWrkrRunnable: " + this.txtFrmSrvr + " - " + timetaken + "").getBytes());
			out.close();
			
			in.close();
			System.out.println("Your request has processed in time : " + timetaken);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}