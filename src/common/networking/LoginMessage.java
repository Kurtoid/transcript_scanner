package common.networking;

import common.constants.Constants;
import org.jose4j.lang.ByteUtil;

import javax.xml.bind.DatatypeConverter;
import java.nio.ByteBuffer;
import java.util.Arrays;


public class LoginMessage extends Message {
    public final int message_size = Integer.BYTES + 1 + Constants.MAX_USERNAME_LENGTH
            + Constants.PASSWORD_HASH_LENGTH;
	public String username;
	public byte[] hash;
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
        bf.position(Integer.BYTES + 1);
        username = getStringFromBuffer(bf, Constants.MAX_USERNAME_LENGTH);
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
        setString(out, username, Constants.MAX_USERNAME_LENGTH);
		out.put(hash);
		byte[] arr = new byte[out.capacity()];
		out.position(0);
		out.get(arr);
		System.out.println(Arrays.toString(arr));
		return arr;
	}

    public static void main(String[] args) {
        LoginMessage m = new LoginMessage();
        m.username = "Kurt";
        m.hash = ByteUtil.randomBytes(Constants.PASSWORD_HASH_LENGTH);
        System.out.println(m.toString());
        LoginMessage r = new LoginMessage(ByteBuffer.wrap(m.getBytes()));
        System.out.println(r.toString());
    }
}
