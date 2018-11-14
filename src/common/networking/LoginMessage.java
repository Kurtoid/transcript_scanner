package common.networking;

import common.constants.Constants;

import javax.xml.bind.DatatypeConverter;
import java.nio.ByteBuffer;
import java.util.Arrays;

public class LoginMessage extends Message {
	public final int message_size = Integer.BYTES + 1 + common.constants.Constants.MAX_USERNAME_LENGTH
			+ common.constants.Constants.PASSWORD_HASH_LENGTH + common.constants.Constants.USER_LOGIN_TOKEN_LENGTH;
	public String username;
	public byte[] hash;
	public String token;
	ByteBuffer out;
	public LoginMessage(String username, byte[] hash) {
		this();
		this.username = username;
		this.hash = hash;
	}

	public LoginMessage(ByteBuffer b) {
		this();
		readMessage(b);
	}
	public LoginMessage() {
		messageType = MESSAGE_TYPE.LOGIN_MESSAGE;
	}
	@Override
	void readMessage(ByteBuffer bf) {
		username = getStringFromBuffer(bf, common.constants.Constants.MAX_USERNAME_LENGTH);
		hash = new byte[Constants.PASSWORD_HASH_LENGTH];
		bf.get(hash);
	}

	@Override
	public String toString() {
		return "LoginMessage [message_type=" + messageType + ", username=" + username + ", hash="
				+ DatatypeConverter.printHexBinary(hash) + "]";
	}

	@Override
	public byte[] getBytes() {
		out = ByteBuffer.allocate(message_size);
		out.position(0);
		out.putInt(message_size);
		out.put(messageType.message_id);
		setString(out, username, common.constants.Constants.MAX_USERNAME_LENGTH);
		out.put(hash);
		byte[] arr = new byte[out.capacity()];
		out.position(0);
		out.get(arr);
		System.out.println(Arrays.toString(arr));
		return arr;
	}
}
