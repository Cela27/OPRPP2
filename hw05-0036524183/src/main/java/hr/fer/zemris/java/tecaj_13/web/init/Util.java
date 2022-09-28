package hr.fer.zemris.java.tecaj_13.web.init;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

public class Util {
	
	public static void main(String[] args) throws NoSuchAlgorithmException {
		try (BufferedWriter writer = new BufferedWriter(
				new FileWriter("C:\\Eclipse Radne Povrsine\\OPRPP2_DZ5\\hw05-0036524183\\src\\main\\webapp\\WEB-INF\\salt.txt", StandardCharsets.UTF_8, true))) {
			byte[] salt= getBytes();
			writer.append(toHex(salt));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private static byte[] getBytes() throws NoSuchAlgorithmException {
		SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
		byte[] salt = new byte[16];
		sr.nextBytes(salt);
		return salt;
	}
	
	private static String toHex(byte[] array) throws NoSuchAlgorithmException {
		BigInteger bi = new BigInteger(1, array);
		String hex = bi.toString(16);

		int paddingLength = (array.length * 2) - hex.length();
		if (paddingLength > 0) {
			return String.format("%0" + paddingLength + "d", 0) + hex;
		} else {
			return hex;
		}
	}
}
