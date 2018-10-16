package common.networking;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

public abstract class Message {
	private byte message_type;
	
	abstract void readMessage(byte[] message);
	
	public byte getMessage_type() {
		return message_type;
	}
	
	protected static String getStringFromBuffer(ByteBuffer bf, int length) {
		byte[] bytes = new byte[length];
		bf.get(bytes);
		String v = new String(bytes, StandardCharsets.UTF_8);
		return v.trim();
	}
	protected static void setString(ByteBuffer bf, String s, int length) {

		if (s.length() > length) {
			s = s.substring(0, length);
		}

		byte[] textBytes = s.getBytes(StandardCharsets.UTF_8);
		byte[] filledBytes = new byte[length];
		System.arraycopy(textBytes, 0, filledBytes, 0, textBytes.length);
		// System.out.println(Arrays.toString(filledBytes));
		bf.put(filledBytes);

	}


	final static byte LOGIN_MESSAGE = 0x02;
}
