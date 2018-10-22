package common.networking;

import common.constants.Constants;

import java.nio.ByteBuffer;
import java.util.Arrays;

import static common.networking.Message.MESSAGE_TYPE.LOGIN_MESSAGE;

public class LoginMessage extends Message {
    public final int message_size = Integer.BYTES + 1 + Integer.BYTES * common.constants.Constants.MAX_USERNAME_LENGTH
            + Integer.BYTES * common.constants.Constants.PASSWORD_HASH_LENGTH;
    private final byte message_type = LOGIN_MESSAGE.message_id;
    String username;
    byte[] hash;

    ByteBuffer out;

    public LoginMessage(String username, byte[] hash) {
        this.username = username;
        this.hash = hash;
    }

    public LoginMessage(ByteBuffer b) {
        b.position(Integer.BYTES + 1);
        username = getStringFromBuffer(b, Constants.MAX_USERNAME_LENGTH);
    }

    @Override
    void readMessage(byte[] message) {
        ByteBuffer bf = ByteBuffer.wrap(message);
        username = getStringFromBuffer(bf, common.constants.Constants.MAX_USERNAME_LENGTH);
        bf.get(hash, 0, common.constants.Constants.PASSWORD_HASH_LENGTH);
    }

    @Override
    public String toString() {
        return "LoginMessage [message_type=" + message_type + ", username=" + username + ", hash="
                + Arrays.toString(hash) + "]";
    }

    @Override
    public byte[] getBytes() {
        out = ByteBuffer.allocate(message_size);
        out.position(0);
        out.putInt(message_size);
        out.put(message_type);
        setString(out, username, common.constants.Constants.MAX_USERNAME_LENGTH);
        out.put(hash);
        byte[] arr = new byte[out.capacity()];
        out.position(0);
        out.get(arr);
        System.out.println(Arrays.toString(arr));
        return arr;
    }
}
