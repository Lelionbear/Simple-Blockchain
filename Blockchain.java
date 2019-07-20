import java.util.ArrayList;
import java.util.Map;

/**
 * @author lelionbear
 *
 * Simple class demonstrating basic back-end of BlockChain technology
 */
public class Blockchain {

	/**
	 * Blockchain list of blocks
	 */
	public static ArrayList<Block> chain;

	/**
	 * Difficulty set for mining blocks on the chain
	 */
	public static int difficulty = 5;

	/**
	 * Constructor that adds the genesis block for the chain
	 */
	public Blockchain() {
		// index, date, data, hash
		chain = new ArrayList<Block>();
	}

	/**
	 * Method that validates the credibility of the blockchain
	 *
	 * @return returns if the chain is credible or not
	 */
	public static boolean validateBlockchain() {
		Block currentBlock;
		Block previousBlock;
		String hashTarget = new String(new char[difficulty]).replace('\0', '0');

		for(int index = 1; index < chain.size(); index++) {
			currentBlock = chain.get(index);
			previousBlock = chain.get(index - 1);

			if(!currentBlock.getHash().equals(currentBlock.encrypt())) {
				System.out.println("Current Hashes not equal");
				return false;
			}

			if(!previousBlock.getHash().equals(currentBlock.getPreviousHash())) {
				System.out.println("Previous Hashes not equal");
				return false;
			}

			//check if hash is solved
			if(!currentBlock.getHash().substring( 0, difficulty).equals(hashTarget)) {
				System.out.println("This block hasn't been mined");
				return false;
			}
		}

		return true;
	}

	/**
	 * Returns the last Block inserted in the chain
	 *
	 * @return returns the last Block inserted in the chain
	 */
	public static Block getLatestBlock() {
		return chain.get(chain.size() - 1);
	}

	/**
	 * Returns the generated hash from the latest block
	 *
	 * @return returns the generated hash from the latest block
	 */
	public static String getLatestHash() {
		return getLatestBlock().encrypt();
	}

	/**
	 * @param map the data that the block will store into the blockchain, description of what type/kind of transaction took place
	 * @param hash simple cryptography, hash, that ensures the validity of the chain is authentic
	 */
	public void add(Map<String, Integer> map, String hash) {
		chain.add(new Block(chain.size(), map, hash));
	}

	/**
	 * Function to mine the latest block in the chain
	 */
	public static void mineLatestBlock() {
		getLatestBlock().mineBlock(difficulty);
	}

	@Override
	public String toString() {
		String blockchain = "";
		for(Block block : chain) {
			blockchain += block.toString() + "\n";
		}
		return blockchain;
	}
}