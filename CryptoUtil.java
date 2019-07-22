import java.security.Key;
import java.security.MessageDigest;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;

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

	/**
	 * Encodes the specified byte array into a String using the Base64encoding scheme.
	 *
	 * @param key
	 * @return returns encoded string from any key
	 */
	public static String getStringFromKey(Key key) {
		return Base64.getEncoder().encodeToString(key.getEncoded());
	}

	/**
	 * Verifies the signature from the transaction
	 *
	 * @param publicKey
	 * @param data
	 * @param signature
	 * @return true if valid
	 */
	public static boolean verifyECDSASig(PublicKey publicKey, String data, byte[] signature) {
		try {

			Signature ecdsaVerify = Signature.getInstance("SHA256withECDSA", "SunEC");

			ecdsaVerify.initVerify(publicKey);

			ecdsaVerify.update(data.getBytes());

			return ecdsaVerify.verify(signature);

		} catch(Exception e) {
			throw new RuntimeException();
		}
	}

	/**
	 * Applies ECDSA signature on the transaction and returns the result in an array of bytes
	 *
	 * @param privateKey the sender's private key
	 * @param input
	 * @return returns signature as bytes array
	 */
	public static byte[] applyECDSASig(PrivateKey privateKey, String input) {

		byte[] output = new byte[0];

		try {

			Signature ecdsaSign = Signature.getInstance("SHA256withECDSA", "SunEC");

			ecdsaSign.initSign(privateKey);

			byte[] strByte = input.getBytes();

			ecdsaSign.update(strByte);

			byte[] realSig = ecdsaSign.sign();

			output = realSig;

		} catch(Exception e) {
			throw new RuntimeException(e);
		}

		return output;
	}

	/**
	 * Tacks in array of transactions and returns a merkle root
	 *
	 * @param transactions
	 * @return returns merkle root from array of transactions
	 */
	public static String getMerkleRoot(ArrayList<Transaction> transactions) {
		int count = transactions.size();

		ArrayList<String> previousTreeLayer = new ArrayList<String>();

		for(Transaction transaction : transactions) {
			previousTreeLayer.add(transaction.transactionId);
		}

		ArrayList<String> treeLayer = previousTreeLayer;

		while(count > 1) {
			treeLayer = new ArrayList<String>();
			for(int i=1; i < previousTreeLayer.size(); i++) {
				treeLayer.add(sha256(previousTreeLayer.get(i-1) + previousTreeLayer.get(i)));
			}
			count = treeLayer.size();
			previousTreeLayer = treeLayer;
		}

		String merkleRoot = (treeLayer.size() == 1) ? treeLayer.get(0) : "";

		return merkleRoot;
	}

}
