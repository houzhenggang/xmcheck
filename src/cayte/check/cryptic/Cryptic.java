package cayte.check.cryptic;

import cayte.check.handler.D;

public class Cryptic {

	/**
	 * º”√‹
	 * 
	 * @param encrypt
	 * @return
	 */
	public static String encrypt(String encrypt) {
		try {
			String key = D.TAG;
			String longKey = MD5.encrypt(D.TAG);
			String hex = Hex.encrypt(key, encrypt);
			String decrypt = Des.encrypt(longKey, hex);
			return decrypt;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Ω‚√‹
	 * 
	 * @param decrypt
	 * @return
	 */
	public static String decrypt(String decrypt) {
		try {
			if (decrypt == null)
				return "";
			String key = D.TAG;
			String longKey = MD5.encrypt(D.TAG);
			String hex = Des.decrypt(longKey, decrypt);
			String encrypt = Hex.decrypt(key, hex);
			return encrypt;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "";
	}
}
