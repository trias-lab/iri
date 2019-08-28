package com.trias.resouce.util;

import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.pkcs.RSAPrivateKeyStructure;

import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.RSAPrivateKeySpec;
import java.security.spec.X509EncodedKeySpec;

public class RSAUtil {

	private static final String PRIVATE_KEY = "MIICXQIBAAKBgQCqUIn2OxSbMRA9DWAczrgU53UZHMEUUrulnWemruG6Y6qsYDYo"
			+ "CZvoEooi137rdk6PeNYOjIRwEKmgr5OWuyHcRqe5MLhuPfbtzzoSPBpFsxa87V/D"
			+ "HYgUStqbAVqbCiLQMutsVO/r7fmBZojM63YmNS9JTyDtarSuedQmfw5rdQIDAQAB"
			+ "AoGBAJEDXESMT2JIJzRkhBZjKLebz8dfBUMBooZD/LIeq1HhdLuqe9IhRF8YEgfS"
			+ "hl2D8SYV0+S1Xjpw5Y1MkZTApvWcmbW4cBz4XScBzGhUSz50ENMpwqWbnk7lkJEs"
			+ "FRHkhSZdZH1lU2K6gvno+lXsreajq3fOqSsZSilgxiGO1e4tAkEA0cRl9xfJhSfm"
			+ "uucSYCYVOBrqUoSnhSKffPUGxAfZ1fWdfk3eJZmiu9Z07zAqdQkA+0s83V7NG5fw"
			+ "bY1IXfvV8wJBAM/aIA61xc1GJXpZT8PcbkC2sGjshDxTVRXVusq3Tds6u37UXbKx"
			+ "qNY1lMlIMCQPG/X8wFjaBAApKt+zOUrKivcCQBmdlSIGappzE+7w9sJ9BAxU5RZs"
			+ "kpRkdPtqZIgduVEybCgXVDBdQY0UlDT9OcIO9mq5dlZGFF/xPu/x18t6TFcCQG0w"
			+ "bT7NBsg7XVzUpi6CEfk0/59fAGigbkY2LRZpDSFpzS1naoGBCuzc4PiMT53hwhKL"
			+ "YJBMl2VuOvOhNyuvEykCQQDJAVIZlT/h6VsRtO0bqXqOVRHHMMPoxbVdo9ul/m2D"
			+ "G33qItoRtFRKp7oOu3jp8sTiiLKLTSsNMMizn+GRYRAf";

	public static final String SIGN_ALGORITHMS = "SHA256withRSA";

	/**
	 * @param content:签名的参数内容
	 * @param privateKey：私钥
	 * @return
	 */
	public static String sign(String content) {
		String charset = "utf-8";
		try {
			java.security.Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
			RSAPrivateKeyStructure asn1PrivKey = new RSAPrivateKeyStructure(
					(ASN1Sequence) ASN1Sequence.fromByteArray(Base64.decode(PRIVATE_KEY)));
			RSAPrivateKeySpec rsaPrivKeySpec = new RSAPrivateKeySpec(asn1PrivKey.getModulus(),
					asn1PrivKey.getPrivateExponent());
			KeyFactory keyf = KeyFactory.getInstance("RSA");
			PrivateKey priKey = keyf.generatePrivate(rsaPrivKeySpec);

			java.security.Signature signature = java.security.Signature.getInstance(SIGN_ALGORITHMS);

			signature.initSign(priKey);
			signature.update(content.getBytes());

			byte[] signed = signature.sign();

			return Base64.encode(signed);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * @param content：验证参数的内容
	 * @param sign：签名
	 * @param publicKey：公钥
	 * @return
	 */
	public static boolean doCheck(String content, String sign, String publicKey) {
		try {
			java.security.Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
			KeyFactory keyFactory = KeyFactory.getInstance("RSA");
			byte[] encodedKey = Base64.decode(publicKey);
			PublicKey pubKey = keyFactory.generatePublic(new X509EncodedKeySpec(encodedKey));
			java.security.Signature signature = java.security.Signature.getInstance(SIGN_ALGORITHMS);
			signature.initVerify(pubKey);
			signature.update(content.getBytes());
			boolean bverify = signature.verify(Base64.decode(sign));
			return bverify;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return false;
	}

}
