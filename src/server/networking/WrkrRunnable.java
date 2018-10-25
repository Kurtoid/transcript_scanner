package server.networking;

import common.User;
import common.networking.LoginMessage;
import common.networking.Message;
import server.database.UserDBO;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.sql.SQLException;
import java.util.Arrays;

public class WrkrRunnable implements Runnable {
	protected Socket clntSocket = null;
	protected String txtFrmSrvr = null;
	DataInputStream in;
	DataOutputStream out;

	public WrkrRunnable(Socket clntSocket, String txtFrmSrvr) {
		this.clntSocket = clntSocket;
		this.txtFrmSrvr = txtFrmSrvr;
	}

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
				System.out.println(Arrays.toString(b.array()));
				Message m = Message.getMessageFromBuffer(b);
				if (m.getMessageType() == Message.MESSAGE_TYPE.LOGIN_MESSAGE) {
					UserDBO uDB = new UserDBO();
					LoginMessage lm = (LoginMessage) m;
					try {
						User u = uDB.loginUser(lm.username, lm.hash);
						System.out.println("user found");
					} catch (SQLException e) {
						System.err.println("user not found");
						e.printStackTrace();
					}
				}
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