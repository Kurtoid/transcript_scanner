package client.core;

import java.io.IOException;
import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import common.ScannedPaper;


public class ApplicationState {
    public static NetworkClient connection;
	final static Logger logger = LoggerFactory.getLogger(ApplicationState.class);

    public static void startNetworkClient(NetworkConnectedCallback cb) {
        if (connection == null) {
            new Thread(() -> {
                connection = new NetworkClient("localhost", 8080);
                try {
                    connection.connect();
                    cb.done(NetworkConnectionStatus.CONNECTED);
                } catch (IOException e) {
                    // TODO Auto-generated catch block
					logger.error("problem connecting to server/client");
                    cb.done(NetworkConnectionStatus.DISCONNECTED);

                }

            }).start();

        } else {
			logger.error("Network system already started");
            cb.done(NetworkConnectionStatus.BAD);
        }
    }

    public enum NetworkConnectionStatus {
        CONNECTED, BAD, DISCONNECTED
    }

    public static ArrayList<ScannedPaper> scannedImages = new ArrayList<>();

    public interface NetworkConnectedCallback {
        void done(NetworkConnectionStatus status);
    }

}
