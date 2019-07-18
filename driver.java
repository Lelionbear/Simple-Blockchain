import java.util.Map;

/**
 * @author lelionbear
 *
 * Simple class demonstrating basic back-end of BlockChain technology
 */
public class driver {

	/**
	 * Driver that initiates basic use of blockchain 
	 * 
	 * @param args not used 
	 */
	public static void main(String[] args) {
		System.out.println("Generating Genesis Blockchain");
		Blockchain blockChain = new Blockchain();
		
		System.out.println("Inserting  2 new blocks");
		blockChain.add(Map.of("Amount",10), blockChain.getLatestHash());
		blockChain.add(Map.of("Amount",25), blockChain.getLatestHash());
		
		System.out.println(blockChain.toString());

	}

}
