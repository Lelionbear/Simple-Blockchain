import java.util.ArrayList;
import java.util.Map;
import java.time.LocalDateTime;

/**
 * @author lelionbear
 *
 * Simple class demonstrating basic back-end of BlockChain technology
 */
public class Blockchain {
	
	private ArrayList<Block> chain;
	
	/**
	 * Constructor that adds the genesis block for the chain
	 */
	public Blockchain() {
		// index, date, data, hash
		this.chain = new ArrayList<Block>();
		this.chain.add(new Block(0, LocalDateTime.now().toString(), Map.of("Genesis Block",0), "0"));
	}
	
	/**
	 * Returns the last Block inserted in the chain
	 * 
	 * @return returns the last Block inserted in the chain
	 */
	public Block getLatestBlock() {
		return this.chain.get(this.chain.size() - 1);
	}
	
	/**
	 * Returns the generated hash from the latest block
	 * 
	 * @return returns the generated hash from the latest block
	 */
	public String getLatestHash() {
		return getLatestBlock().generateHash();
	}

	/**
	 * @param map the data that the block will store into the blockchain, description of what type/kind of transaction took place
	 * @param hash simple cryptography, hash, that ensures the validity of the chain is authentic 
	 */
	public void add(Map<String, Integer> map, String hash) {
		this.chain.add(new Block(chain.size(), LocalDateTime.now().toString(), map, hash));
	}
	
	public String toString() {
		String blockchain = "";
		for(Block block : this.chain) {
			blockchain += block.toString() + "\n";
		}
		return blockchain;
	}
}