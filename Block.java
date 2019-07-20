import java.time.LocalDateTime;
import java.util.Map;

/**
 * @author lelionbear
 *
 * Simple class demonstrating basic back-end of BlockChain technology
 */
public class Block {

	/**
	 * The date and time the block was created/born
	 */
	private String timeStamp;
	/**
	 * Map containing the data for the current block
	 */
	private Map<String, Integer> data;
	/**
	 * The index in the Blockchain where the current block was added to
	 */
	private int index;
	/**
	 * The hash generated from this block in the chain
	 */
	private String hash;
	/**
	 * The hash from the predecessor block in the chain
	 */
	private String previousHash;
	/**
	 * Integer used to mine the blockchain
	 */
	private int nonce;

	/**
	 * Constructor that creates a block to be chained in the blockchain
	 *
	 * @param index
	 * @param data
	 * @param previousHash
	 */
	public Block(int index, Map<String, Integer> data, String previousHash) {
		this.index = index;
		this.timeStamp = LocalDateTime.now().toString();
		this.data = data;
		this.previousHash = previousHash;

		this.hash = encrypt();
	}

	/**
	 * Simulates the mining process of blockchain with a set difficulty established
	 *
	 * @param difficulty level of difficulty to simulate mining
	 */
	public void mineBlock(int difficulty) {

		String target = new String(new char[difficulty]).replace('\0', '0');

		while(!hash.substring( 0, difficulty).equals(target)) {

			nonce++;

			hash = encrypt();
		}

		System.out.println("Successfully mined the block: " + hash);
	}

	/**
	 * Encrypts the data from the block
	 *
	 * @return return the encrypted hash
	 */
	public String encrypt() {
		return CryptoUtil.sha256(getInfo());
	}

	/**
	 * Getter method that returns the hash of this block instance
	 *
	 * @return hash of this current block
	 */
	public String getHash() {
		return this.hash;
	}

	/**
	 * Getter method that returns the previous hash of this block instance
	 *
	 * @return hash of previous block
	 */
	public String getPreviousHash() {
		return this.previousHash;
	}

	/**
	 * Returns a string object of the data contained within the block
	 *
	 * @return returns the information contained to form the cryptographic hash
	 */
	private String getInfo() {
		return Integer.toString(this.index) +
				this.timeStamp +
				this.data.toString() +
				this.previousHash +
				Integer.toString(this.nonce);
	}

	@Override
	public String toString() {
		Map<String, String> map = Map.of(
				"Index", Integer.toString(this.index),
				"TimeStamp", this.timeStamp,
				"Data", this.data.toString(),
				"PreviousHash", this.previousHash,
				"Hash", this.hash,
				"nonce", Integer.toString(this.nonce));
		String block = "";
		for(String key : map.keySet()) {
			block += key + ": " + map.get(key) + "\n";
		}
		return block;
	}
}
