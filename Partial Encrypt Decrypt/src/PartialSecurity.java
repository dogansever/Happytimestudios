import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class PartialSecurity {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// encryption
		try {
			PartialSecurity ps = new PartialSecurity();
			File fileIn = new File("images/original.jpg");
			InputStream in = new FileInputStream(fileIn);
			File fileOut = new File("images/secured.jpg");
			OutputStream out = new FileOutputStream(fileOut);
			ps.encrypt(in, out);
		} catch (Exception e) {
			e.printStackTrace();
		}
		// decryption
		try {
			PartialSecurity ps2 = new PartialSecurity();
			File fileIn = new File("images/secured.jpg");
			InputStream in = new FileInputStream(fileIn);
			File fileOut = new File("images/originalBack.jpg");
			OutputStream out = new FileOutputStream(fileOut);
			ps2.decrypt(in, out);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void setEncryptModeOn() {
		System.out.println("setEncryptModeOn:");
		try {
			cipher.init(Cipher.ENCRYPT_MODE, key, IV);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void setDecryptModeOn() {
		System.out.println("setDecryptModeOn:");
		try {
			cipher.init(Cipher.DECRYPT_MODE, key, IV);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static int BYTES_TO_ENCRYPT = 1024;

	byte[] buf = new byte[4096];

	byte[] keyBytes = "abcdef0123456789".getBytes(); // 16
	final byte[] ivBytes = new byte[] { 0x00, 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08, 0x09, 0x0a, 0x0b, 0x0c, 0x0d, 0x0e, 0x0f }; // example

	private Cipher cipher;

	private SecretKeySpec key;

	private IvParameterSpec IV;

	PartialSecurity() {
		try {
			key = new SecretKeySpec(keyBytes, "AES");
			IV = new IvParameterSpec(ivBytes);
			cipher = Cipher.getInstance("AES/CFB8/NoPadding");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void encrypt(InputStream in, OutputStream out) {
		setEncryptModeOn();
		try {
			OutputStream out_c = new CipherOutputStream(out, cipher);

			int numRead = 0;
			int count = 0;
			boolean first = true;
			numRead = in.read(buf, 0, BYTES_TO_ENCRYPT);
			count += numRead;
			out_c.write(buf, 0, numRead);
			System.out.println("out_c.write:" + numRead);
			BYTES_TO_ENCRYPT = numRead;

			while ((numRead = in.read(buf)) >= 0) {
				count += numRead;
				out.write(buf, 0, numRead);
			}
			out.close();
			out_c.close();
			in.close();
			System.out.println("numRead count:" + count);

		} catch (java.io.IOException e) {
		}
	}

	public void decrypt(InputStream in, OutputStream out) {
		setDecryptModeOn();
		try {
			CipherInputStream in_c = new CipherInputStream(in, cipher);
			int numRead = 0;
			int count = 0;
			numRead = in_c.read(buf, 0, BYTES_TO_ENCRYPT);
			System.out.println("in_c.read:" + numRead);
			count += numRead;
			if (numRead < BYTES_TO_ENCRYPT) {
				out.write(buf, 0, numRead);
				while (count < BYTES_TO_ENCRYPT) {
					numRead = in_c.read(buf, 0, BYTES_TO_ENCRYPT - count);
					System.out.println("in_c.read:" + numRead);
					out.write(buf, 0, numRead);
					count += numRead;
				}
			} else {
				out.write(buf, 0, numRead);
			}

			while ((numRead = in.read(buf)) >= 0) {
				count += numRead;
				out.write(buf, 0, numRead);
			}

			out.close();
			in.close();
			in_c.close();
			System.out.println("numRead count:" + count);
		} catch (java.io.IOException e) {
		}
	}

}
