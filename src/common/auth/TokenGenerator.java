package common.auth;

import java.security.Key;

import org.jose4j.jwa.AlgorithmConstraints;
import org.jose4j.jwa.AlgorithmConstraints.ConstraintType;
import org.jose4j.jwe.ContentEncryptionAlgorithmIdentifiers;
import org.jose4j.jwe.JsonWebEncryption;
import org.jose4j.jwe.KeyManagementAlgorithmIdentifiers;
import org.jose4j.keys.AesKey;
import org.jose4j.lang.ByteUtil;
import org.jose4j.lang.JoseException;

public class TokenGenerator {
	public static String makeKey(String payload, byte[] aeskey) {
		Key key = new AesKey(aeskey);
		JsonWebEncryption jwe = new JsonWebEncryption();
		jwe.setPayload(payload);
		jwe.setAlgorithmHeaderValue(KeyManagementAlgorithmIdentifiers.A128KW);
		jwe.setEncryptionMethodHeaderParameter(ContentEncryptionAlgorithmIdentifiers.AES_128_CBC_HMAC_SHA_256);
		jwe.setKey(key);
		String serializedJwe = jwe.getCompactSerialization();
		return serializedJwe;
	}

	public static boolean verifyKey(String message, String expected, byte[] key) {
		JsonWebEncryption jwe = new JsonWebEncryption();
		jwe.setAlgorithmConstraints(
				new AlgorithmConstraints(ConstraintType.WHITELIST, KeyManagementAlgorithmIdentifiers.A128KW));
		jwe.setContentEncryptionAlgorithmConstraints(new AlgorithmConstraints(ConstraintType.WHITELIST,
				ContentEncryptionAlgorithmIdentifiers.AES_128_CBC_HMAC_SHA_256));
		Key aesKey = new AesKey(key);
		jwe.setKey(aesKey);
		jwe.setCompactSerialization(message);
		System.out.println("Payload: " + jwe.getPayload());
		if (jwe.getPayload().equals(expected)) {
			return true;
		}
	}

}
