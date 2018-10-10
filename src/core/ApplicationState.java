package core;

import java.io.IOException;

import networking.NetworkClient;
public class ApplicationState {
	public interface NetworkConnectedCallback {
		void done(NetworkConnectionStatus status); 
	}
	static public enum NetworkConnectionStatus{
		CONNECTED, BAD, DISCONNECTED
	}
	public static NetworkClient c;

	public static void startNetworkClient(NetworkConnectedCallback cb)  {
		if (c == null) {
			new Thread(new Runnable() {
				
				@Override
				public void run() {
					c = new NetworkClient("localhost", 8080);
					try {
						c.connect();
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
