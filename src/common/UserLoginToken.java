package common;

import org.jose4j.lang.ByteUtil;

public class UserLoginToken {
    public int id;
    public int userId;
    public byte[] key;

    public static UserLoginToken generate(User u) {
        UserLoginToken tok = new UserLoginToken();
        tok.userId = u.id;
        tok.key = ByteUtil.randomBytes(16);
        return tok;
    }
}
