import java.util.Map;

/**
 * @author lelionbear
 *
 * Simple class demonstrating basic back-end of BlockChain technology
 */
public class Driver {

	/**
	 * Driver that initiates basic use of blockchain
	 *
	 * @param args not used
	 */
	public static void main(String[] args) {

		System.out.println("Initiating Simple Blockchain");
		Blockchain bc = new Blockchain();

		bc.add(Map.of("Genesis Block", 0), "0");
		System.out.println("\nNew Block Created");

		System.out.println("Mining latest block");
		Blockchain.mineLatestBlock();

		for(int i = 1; i < 10; i++) {
			bc.add(Map.of("Content", 10 - i), Blockchain.getLatestHash());
			System.out.println("\nNew Block Created");

			System.out.println("Mining latest block");
			Blockchain.mineLatestBlock();
		}

		// When uncommented out, blockchain signals that this block has not been mined
//		bc.add(Map.of("Content", 10), Blockchain.getLatestHash());
//		System.out.println("\nNew Block Created");

		System.out.println("\nIs Blockchain valid: " + Blockchain.validateBlockchain());

		System.out.println("\nBlockchain content:");
		System.out.println(bc.toString());

	}

}
