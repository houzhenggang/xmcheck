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
	 * <���ַ�������Des���ܽ��ַ���ת��Ϊ�ֽ��������>
	 */
	public static String encrypt(String key, String data) throws Exception {
		byte[] result = encrypt(key, data.getBytes());
		return new String(Base64.encode(result, Base64.DEFAULT));
	}

	/**
	 * 
	 * <���ַ�������Des���ܽ��ַ���ת��Ϊ�ֽ��������>
	 */
	private static byte[] encrypt(String key, byte[] data) throws Exception {
		SecureRandom random = new SecureRandom();
		DESKeySpec desKey = new DESKeySpec(key.getBytes());
		// ����һ���ܳ׹�����Ȼ��������DESKeySpecת����
		SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
		SecretKey securekey = keyFactory.generateSecret(desKey);
		// Cipher����ʵ����ɼ��ܲ���
		Cipher cipher = Cipher.getInstance("DES");
		// ���ܳ׳�ʼ��Cipher����
		cipher.init(Cipher.ENCRYPT_MODE, securekey, random);
		// ���ڣ���ȡ���ݲ�����
		// ��ʽִ�м��ܲ���
		return cipher.doFinal(data);
	}

	/**
	 * 
	 * <�����ܵ������ֽ�����ת��Ϊ�����ֽ�����>
	 */
	public static String decrypt(String key, String data) throws Exception {
		byte[] decryResult = decrypt(key,
				Base64.decode(data.getBytes(), Base64.DEFAULT));
		return new String(decryResult);
	}

	/**
	 * 
	 * <�����ܵ������ֽ�����ת��Ϊ�����ֽ�����>
	 */
	private static byte[] decrypt(String key, byte[] src) throws Exception {
		// DES�㷨Ҫ����һ�������ε������Դ
		SecureRandom random = new SecureRandom();
		// ����һ��DESKeySpec����
		DESKeySpec desKey = new DESKeySpec(key.getBytes());
		// ����һ���ܳ׹���
		SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
		// ��DESKeySpec����ת����SecretKey����
		SecretKey securekey = keyFactory.generateSecret(desKey);
		// Cipher����ʵ����ɽ��ܲ���
		Cipher cipher = Cipher.getInstance("DES");
		// ���ܳ׳�ʼ��Cipher����
		cipher.init(Cipher.DECRYPT_MODE, securekey, random);
		// ������ʼ���ܲ���
		return cipher.doFinal(src);
	}

}