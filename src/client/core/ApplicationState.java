package client.core;

import common.GradeReport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;

import java.util.ArrayList;

/**
 * things needed throughout the lifecycle of the application
 *
 * @author Kurt Wilson
 */
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
    /**
     * images scanned
     */
    public static ArrayList<GradeReport> scannedImages = new ArrayList<>();

    public interface NetworkConnectedCallback {
        void done(NetworkConnectionStatus status);
    }
}
