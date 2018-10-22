package client.core;

import java.io.IOException;

public class ApplicationState {
	public interface NetworkConnectedCallback {
		void done(NetworkConnectionStatus status); 
	}

	public enum NetworkConnectionStatus {
		CONNECTED, BAD, DISCONNECTED
	}

	public static NetworkClient connection;

	public static void startNetworkClient(NetworkConnectedCallback cb)  {
		if (connection == null) {
			new Thread(new Runnable() {
				
				@Override
				public void run() {
					connection = new NetworkClient("localhost", 8080);
					try {
						connection.connect();
						cb.done(NetworkConnectionStatus.CONNECTED);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						cb.done(NetworkConnectionStatus.DISCONNECTED);

					}
				
				}
			}).start();
			
		} else {
			System.err.println("Network system already started");
			cb.done(NetworkConnectionStatus.BAD);
		}
	}
}
