package main.client.core;

import main.common.constants.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.*;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.net.URL;
import java.security.cert.CertificateException;
import java.util.Properties;

/**
 * checks for updates from a hosted text file and zip
 */
public class UpdateChecker {
	final static private Logger logger = LoggerFactory.getLogger(UpdateChecker.class);

	/**
	 * determines if an update is required from the online update file
	 *
	 * @return true or false; whether there is a new version available
	 * @throws IOException if it cant connect
	 */
	public static boolean needsUpdate() throws IOException {
		Properties props = new Properties();
		props.load(readUpdateFile());
		System.out.println(props.toString());
		return Integer.valueOf(props.getProperty("current_build")) > Constants.buildNumber;
	}

	public static void main(String[] args) {
		try {
			System.out.println(needsUpdate());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * utility method to read the file contents from the link
	 * @return a stream representing the file contents
	 * @throws IOException
	 */
	private static InputStream readUpdateFile() throws IOException {
		trustAllHosts();
		URL url = new URL(Constants.updateURL);
		return url.openStream();
	}

	/**
	 * because school computers don't allow java to use SDPBC cert :/<br/>
	 * this is bad because we are about to download AND RUN(!!!) an executable over this now-insecure connection
	 */
	private static void trustAllHosts() {
		try {
			TrustManager[] trustAllCerts = new TrustManager[]{
					new X509ExtendedTrustManager() {
						@Override
						public java.security.cert.X509Certificate[] getAcceptedIssuers() {
							return null;
						}

						@Override
						public void checkClientTrusted(java.security.cert.X509Certificate[] certs, String authType) {
						}

						@Override
						public void checkServerTrusted(java.security.cert.X509Certificate[] certs, String authType) {
						}

						@Override
						public void checkClientTrusted(java.security.cert.X509Certificate[] xcs, String string, Socket socket) throws CertificateException {

						}

						@Override
						public void checkServerTrusted(java.security.cert.X509Certificate[] xcs, String string, Socket socket) throws CertificateException {

						}

						@Override
						public void checkClientTrusted(java.security.cert.X509Certificate[] xcs, String string, SSLEngine ssle) throws CertificateException {

						}

						@Override
						public void checkServerTrusted(java.security.cert.X509Certificate[] xcs, String string, SSLEngine ssle) throws CertificateException {

						}

					}
			};

			SSLContext sc = SSLContext.getInstance("SSL");
			sc.init(null, trustAllCerts, new java.security.SecureRandom());
			HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

			// Create all-trusting host name verifier
			HostnameVerifier allHostsValid = (hostname, session) -> true;
			// Install the all-trusting host verifier
			HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
		} catch (Exception e) {
			logger.error("Error occurred", e);
		}
	}


}
