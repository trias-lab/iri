package com.trias.resouce.util;

import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

public class RSAUtil {

	private static final String PRIVATE_KEY = "MIICXQIBAAKBgQC88tqCOKwlnMLhfq/yGyxfQ1rGQXhcxYmnPhfsqkqD0IhMIgbV\r\n"
			+ "CoMY4SSUNuJolzxQDRcvdECB/4tGvNOVdRAffwXQf1aiV2WtTnqrDNlIHW+x365i\r\n"
			+ "KpsnvTTMPAIeFNhX/y0xT5rjyuAmRK4rdQ2+lVhWnqwIjdtjRWneGhDwKQIDAQAB\r\n"
			+ "AoGASwDgCjcy5o9OsLJYZ3Ov7nTPMMnGXJUxakj0uEZ049RAdnA/ZAwTNCoTGh6b\r\n"
			+ "S0dVcrVvka/E95WYFNFZ8AcTCa25CNgKEK+B/gNmujSh/7pQI3s9rw3UnsECAOn7\r\n"
			+ "4n2/RqYKewKQwE3NQMtxbfoSoCOwbvc495oOx08XeTeCzHECQQD4iw7Xg6Ps/BSq\r\n"
			+ "cF5OzRKp9nMHWWXvK/fwx+NiV0eOiJfOcvS1hMgsBcrPjtfeHNuIeRpP1YEsPKnT\r\n"
			+ "2lI0TRrdAkEAwp4UBQY00+9h2t+5UZL4d3DN6/3a1BpJg+7alOn1wIo4Ddd7bE0M\r\n"
			+ "bmSZqYLHs8mNbNmy45tjqP/EfugUBV9XvQJBAKaYx20MTx/15IBOa9iUO3Nzv6zo\r\n"
			+ "bpw/s6VQxwjPCibvRhpbEc5uX6Kqi0RPMkEuxLSYwFtM+JXHX+qih8GJAX0CQQCI\r\n"
			+ "suJa+RYIO1+fjn+r1bDirIBnn8KiMuiqXA56hK9Sk4N17YJK+v+dVto8H5lIJm1J\r\n"
			+ "JMyrZM0muqh5f2f4shR9AkBmvToX9cEO/n4xOkPtetz/v2Iu33jdmOrSTDEdrZXX\r\n"
			+ "2aefNFMVkMipBiVvdbriznFTDkiRfApXdPSSEBOVhTtx";

	public static final String SIGN_ALGORITHMS = "SHA1WithRSA";

	/**
	 * @param content:签名的参数内容
	 * @param privateKey：私钥
	 * @return
	 */
	public static String sign(String content) {
		String charset = "utf-8";
		try {
			java.security.Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
			PKCS8EncodedKeySpec priPKCS8 = new PKCS8EncodedKeySpec(Base64.decode(PRIVATE_KEY));
			KeyFactory keyf = KeyFactory.getInstance("RSA");
			PrivateKey priKey = keyf.generatePrivate(priPKCS8);

			java.security.Signature signature = java.security.Signature.getInstance(SIGN_ALGORITHMS);

			signature.initSign(priKey);
			signature.update(content.getBytes(charset));

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
			signature.update(content.getBytes("utf-8"));

			boolean bverify = signature.verify(Base64.decode(sign));
			return bverify;

		} catch (Exception e) {
			e.printStackTrace();
		}

		return false;
	}

	public static String sign(String content, String privateKey) {
		String charset = "utf-8";
		try {
			java.security.Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
			PKCS8EncodedKeySpec priPKCS8 = new PKCS8EncodedKeySpec(Base64.decode(privateKey));
			KeyFactory keyf = KeyFactory.getInstance("RSA");
			PrivateKey priKey = keyf.generatePrivate(priPKCS8);

			java.security.Signature signature = java.security.Signature.getInstance(SIGN_ALGORITHMS);

			signature.initSign(priKey);
			signature.update(content.getBytes(charset));

			byte[] signed = signature.sign();

			return Base64.encode(signed);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

}
