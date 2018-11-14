package common.auth;


import common.constants.Constants;
import org.jose4j.jwa.AlgorithmConstraints;
import org.jose4j.jwa.AlgorithmConstraints.ConstraintType;
import org.jose4j.jwe.ContentEncryptionAlgorithmIdentifiers;
import org.jose4j.jwe.JsonWebEncryption;
import org.jose4j.jwe.KeyManagementAlgorithmIdentifiers;
import org.jose4j.keys.AesKey;
import org.jose4j.lang.ByteUtil;
import org.jose4j.lang.JoseException;

import java.security.Key;

public class TokenGenerator {
	public static String makeKey(String payload, byte[] aeskey) throws JoseException {
		Key key = new AesKey(aeskey);
		JsonWebEncryption jwe = new JsonWebEncryption();
		jwe.setPayload(payload);
		jwe.setAlgorithmHeaderValue(KeyManagementAlgorithmIdentifiers.A128KW);
		jwe.setEncryptionMethodHeaderParameter(ContentEncryptionAlgorithmIdentifiers.AES_128_CBC_HMAC_SHA_256);
		jwe.setKey(key);
		String serializedJwe = jwe.getCompactSerialization();
		return serializedJwe;
	}

	public static boolean verifyKey(String message, String expected, byte[] key) throws JoseException {
		JsonWebEncryption jwe = new JsonWebEncryption();
		jwe.setAlgorithmConstraints(
				new AlgorithmConstraints(ConstraintType.WHITELIST, KeyManagementAlgorithmIdentifiers.A128KW));
		jwe.setContentEncryptionAlgorithmConstraints(new AlgorithmConstraints(ConstraintType.WHITELIST,
				ContentEncryptionAlgorithmIdentifiers.AES_128_CBC_HMAC_SHA_256));
		Key aesKey = new AesKey(key);
		jwe.setKey(aesKey);
		jwe.setCompactSerialization(message);
		System.out.println("Payload: " + jwe.getPayload());
		return jwe.getPayload().equals(expected);
	}

	public static void main(String[] args) {
		Key key = new AesKey(ByteUtil.randomBytes(16));
		try {
			String k = makeKey(String.format("%-" + Constants.MAX_USERNAME_LENGTH + "s", "hi there!"), key.getEncoded());
			System.out.println(k.length());
			System.out.println(verifyKey(k, String.format("%-" + Constants.MAX_USERNAME_LENGTH + "s", "hi there!"), key.getEncoded()));
		} catch (JoseException e) {
			e.printStackTrace();
		}

	}

}
