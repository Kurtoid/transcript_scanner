package common.networking;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

public abstract class Message {
    private byte messageType;

    public static Message getMessageFromBuffer(ByteBuffer b) {
        b.position(Integer.BYTES);
        byte type = b.get();
        for (MESSAGE_TYPE t : MESSAGE_TYPE.values()) {
            if (type == t.message_id) {
                try {
                    Constructor c = t.mclass.getConstructor(ByteBuffer.class);
                    return (Message) c.newInstance(b);
                } catch (NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException e) {
                    e.printStackTrace();
                }

            }
        }
        return null;
    }

    abstract void readMessage(byte[] message);

    public byte getMessageType() {
        return messageType;
    }

    public static String getStringFromBuffer(ByteBuffer bf, int length) {
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

    public abstract byte[] getBytes();

    public enum MESSAGE_TYPE {
        //		EMPTY_MESSAGE (0x01),
        LOGIN_MESSAGE(0x02, LoginMessage.class);


        public final byte message_id;   // in kilograms
        public final Class<? extends Message> mclass;

        MESSAGE_TYPE(int id, Class<? extends Message> message) {
            this.message_id = (byte) id;
            mclass = message;
        }


    }
}
