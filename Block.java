import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Map;

/**
 * @author lelionbear
 *
 * Simple class demonstrating basic back-end of BlockChain technology
 */
public class Block {
	
	private final int index;
	private final String date;
	private final Map<String, Integer> data;
	private final String previousHash;
	
	/**
	 * Constructor that creates a block to be chained in the blockchain
	 * 
	 * @param index 
	 * @param date
	 * @param data
	 * @param previousHash
	 */
	public Block(int index, String date, Map<String, Integer> data, String previousHash) {
		this.index = index;
		this.date = date;
		this.data = data;
		this.previousHash = previousHash;
	}
	
	/**
	 * Generates the hash for the block using cryptography 
	 * 
	 * @return returns the hash generated for this block, null if execution fails
	 */
	public String generateHash() {
		try {
			MessageDigest md = MessageDigest.getInstance("SHA-512");
			
			byte[] messageDigest = md.digest(previousHash.getBytes());
			
			BigInteger no = new BigInteger(1, messageDigest);
			
			String hashtext = no.toString(16);
			
			while (hashtext.length() < 32) { 
                hashtext = "0" + hashtext; 
            }
			
			return hashtext;
		
		} catch (NoSuchAlgorithmException e) {
			return null;
			// throw new RuntimeException(e);
		}
	}
	
	public String toString() {
		String info = "";
		info += this.index + "\n";
		info += this.date + "\n";
		info += this.data + "\n";
		info += this.previousHash + "\n";
		info += generateHash() + "\n";
		return info;
	}
}
