import java.security.MessageDigest;
import java.util.Arrays;

/**
 * @author lelionbear
 *
 * Class contains all the utilities needed to maintain security within the network
 */
public class CryptoUtil {

	/**
	 * Main method breaks down the operations done inside the sha256 method
	 *
	 * @param args N/A
	 */
	public static void main(String[] args) {

		String message = "abcdefghijklmnopqrstuvwxyz";
		byte[] messageArr;
		byte[] encryptArr;
		StringBuffer hexStringUncrypted = new StringBuffer();
		StringBuffer hexStringCrypted = new StringBuffer();

		try {
			messageArr = message.getBytes("UTF-8"); // gets binary values of each character

			MessageDigest md = MessageDigest.getInstance("SHA-256"); // object that uses encryption algo

			System.out.println(message); //prints message

			System.out.println(Arrays.toString(messageArr)); // prints !encrypted binary array

			encryptArr = md.digest(messageArr); // encrypts message

			System.out.println(encryptArr.length); // length of encrypted array

			System.out.println(Arrays.toString(encryptArr)); // prints encrypted binary array

			System.out.println("BEFORE\nBIN\tHEX");
			for(int i = 0; i < messageArr.length; i++) {
				int bin = messageArr[i];

				String hex = Integer.toHexString(0xff & bin); // converts binary to a hex string

				System.out.println(bin + "\t" + hex);

				if(hex.length() == 1) {
					hexStringUncrypted.append('0');
				}

				hexStringUncrypted.append(hex);
			}
			System.out.println(hexStringUncrypted.toString());
			System.out.println(hexStringUncrypted.toString().length());
			System.out.println("AFTER\nBIN\tHEX");
			for(int i = 0; i < encryptArr.length; i++) {
				int bin = encryptArr[i];

				// converts binary to a hex string
				// uses AND (&) operator of 255 in binary (0xff)
				// to keep data only of the last 8 bits of the 32 bit length size of binary object
				String hex = Integer.toHexString(0xff & bin);

				System.out.println(bin + "\t" + hex);

				if(hex.length() == 1) {
					hexStringCrypted.append('0');
				}

				hexStringCrypted.append(hex);
			}
			System.out.println(hexStringCrypted.toString());
			System.out.println(hexStringCrypted.toString().length());

		} catch (Exception e) {
			// do nothing
		}
	}

	/**
	 * Encrypts the data from the Block
	 *
	 * @param input String of data passed from the Block
	 * @return Encrypted hash to Block
	 */
	public static String sha256(String input) {
		try {
			MessageDigest md = MessageDigest.getInstance("SHA-256");

			byte[] binArray = md.digest(input.getBytes("UTF-8"));

			StringBuffer hash = new StringBuffer(); // This will contain hash as hexadecimal

			for (int i = 0; i < binArray.length; i++) {
				String hex = Integer.toHexString(0xff & binArray[i]);

				if(hex.length() == 1) {
					hash.append('0');
				}

				hash.append(hex);
			}

			return hash.toString();

		} catch (Exception e) {
			throw new RuntimeException();
		}
	}

}
