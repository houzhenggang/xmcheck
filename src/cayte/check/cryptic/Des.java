package cayte.check.cryptic;

import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

import android.util.Base64;

public class Des {

	/**
	 * 
	 * <对字符串进行Des加密将字符串转化为字节数组解密>
	 */
	public static String encrypt(String key, String data) throws Exception {
		byte[] result = encrypt(key, data.getBytes());
		return new String(Base64.encode(result, Base64.DEFAULT));
	}

	/**
	 * 
	 * <对字符串进行Des加密将字符串转化为字节数组解密>
	 */
	private static byte[] encrypt(String key, byte[] data) throws Exception {
		SecureRandom random = new SecureRandom();
		DESKeySpec desKey = new DESKeySpec(key.getBytes());
		// 创建一个密匙工厂，然后用它把DESKeySpec转换成
		SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
		SecretKey securekey = keyFactory.generateSecret(desKey);
		// Cipher对象实际完成加密操作
		Cipher cipher = Cipher.getInstance("DES");
		// 用密匙初始化Cipher对象
		cipher.init(Cipher.ENCRYPT_MODE, securekey, random);
		// 现在，获取数据并加密
		// 正式执行加密操作
		return cipher.doFinal(data);
	}

	/**
	 * 
	 * <将加密的密文字节数组转化为明文字节数组>
	 */
	public static String decrypt(String key, String data) throws Exception {
		byte[] decryResult = decrypt(key,
				Base64.decode(data.getBytes(), Base64.DEFAULT));
		return new String(decryResult);
	}

	/**
	 * 
	 * <将加密的密文字节数组转化为明文字节数组>
	 */
	private static byte[] decrypt(String key, byte[] src) throws Exception {
		// DES算法要求有一个可信任的随机数源
		SecureRandom random = new SecureRandom();
		// 创建一个DESKeySpec对象
		DESKeySpec desKey = new DESKeySpec(key.getBytes());
		// 创建一个密匙工厂
		SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
		// 将DESKeySpec对象转换成SecretKey对象
		SecretKey securekey = keyFactory.generateSecret(desKey);
		// Cipher对象实际完成解密操作
		Cipher cipher = Cipher.getInstance("DES");
		// 用密匙初始化Cipher对象
		cipher.init(Cipher.DECRYPT_MODE, securekey, random);
		// 真正开始解密操作
		return cipher.doFinal(src);
	}

}