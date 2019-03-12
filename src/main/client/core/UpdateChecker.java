package main.client.core;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Properties;

import main.common.constants.Constants;

public class UpdateChecker {
	public static boolean needsUpdate() throws IOException {
		Properties defaultProps = new Properties();
		defaultProps.load(readUpdateFile());
		return Integer.valueOf(defaultProps.getProperty("current_build")) > Constants.buildNumber;
	}

	public static void main(String[] args) {
		try {
			System.out.println(needsUpdate());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private static InputStream readUpdateFile() throws IOException {
		URL url = new URL(Constants.updateURL);
		return url.openStream();
	}

}
