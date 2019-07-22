import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


/**
 * @author lelionbear
 *
 * Simple class demonstrating basic back-end of BlockChain technology
 */
public class Blockchain {

	/**
	 * List of all unspent transactions.
	 * Unspent Transaction Outputs
	 */
	public static Map<String,TransactionOutput> UTXOs;

	/**
	 * Blockchain list of blocks
	 */
	public static ArrayList<Block> chain;

	/**
	 * Difficulty set for mining blocks on the chain
	 */
	public static int difficulty = 3;

	/**
	 * Minimum transaction allowed
	 */
	public static float minimumTransaction = 0.1f;

	/**
	 * Genesis transaction to startup the blockchain
	 */
	public static Transaction genesisTransaction;

	/**
	 * Constructor that adds the genesis block for the chain
	 */
	public Blockchain() {
		// index, date, data, hash
		chain = new ArrayList<Block>();
		UTXOs = new HashMap<String,TransactionOutput>();
	}

	public static void main(String[] args) {
		Blockchain blockchain = new Blockchain();
		Wallet walletA = new Wallet();
		Wallet walletB = new Wallet();
		Wallet coinbase = new Wallet();


		//create genesis transaction, which sends 100 NoobCoin to walletA:
		genesisTransaction = new Transaction(coinbase.publicKey, walletA.publicKey, 100f, null);
		genesisTransaction.generateSignature(coinbase.privateKey);	 //manually sign the genesis transaction
		genesisTransaction.transactionId = "0"; //manually set the transaction id
		genesisTransaction.outputs.add(new TransactionOutput(
				genesisTransaction.recipient,
				genesisTransaction.value,
				genesisTransaction.transactionId)); //manually add the Transactions Output
		//its important to store our first transaction in the UTXOs list.
		UTXOs.put(genesisTransaction.outputs.get(0).id, genesisTransaction.outputs.get(0));

		System.out.println("Creating and Mining Genesis block... ");
		Block genesis = new Block("0");
		genesis.addTransaction(genesisTransaction);
		addBlock(genesis);


		//setup
		Block block1 = new Block(Blockchain.getLatestHash());
		System.out.println("\nWalletA's balance is: " + walletA.getBalance());
		System.out.println("\nWalletA is Attempting to send funds (40) to WalletB...");
		block1.addTransaction(walletA.sendFunds(walletB.publicKey, 40f));
		addBlock(block1);
		System.out.println("\nWalletA's balance is: " + walletA.getBalance());
		System.out.println("WalletB's balance is: " + walletB.getBalance());

		Block block2 = new Block(Blockchain.getLatestHash());
		System.out.println("\nWalletA Attempting to send more funds (1000) than it has...");
		block2.addTransaction(walletA.sendFunds(walletB.publicKey, 1000f));
		addBlock(block2);
		System.out.println("\nWalletA's balance is: " + walletA.getBalance());
		System.out.println("WalletB's balance is: " + walletB.getBalance());

		Block block3 = new Block(Blockchain.getLatestHash());
		System.out.println("\nWalletB is Attempting to send funds (20) to WalletA...");
		block3.addTransaction(walletB.sendFunds( walletA.publicKey, 20));
		addBlock(block3);
		System.out.println("\nWalletA's balance is: " + walletA.getBalance());
		System.out.println("WalletB's balance is: " + walletB.getBalance());

		System.out.println("Is Blockchain valid: " + validateBlockchain());
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
		//a temporary working list of unspent transactions at a given block state.
		HashMap<String,TransactionOutput> tempUTXOs = new HashMap<String,TransactionOutput>();
		tempUTXOs.put(genesisTransaction.outputs.get(0).id, genesisTransaction.outputs.get(0));


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



			TransactionOutput tempOutput;
			for(int t=0; t <currentBlock.transactions.size(); t++) {
				Transaction currentTransaction = currentBlock.transactions.get(t);

				if(!currentTransaction.verifiySignature()) {
					System.out.println("#Signature on Transaction(" + t + ") is Invalid");
					return false;
				}

				if(currentTransaction.getInputsValue() != currentTransaction.getOutputsValue()) {
					System.out.println("#Inputs are not equal to outputs on Transaction(" + t + ")");
					return false;
				}

				for(TransactionInput input: currentTransaction.inputs) {
					tempOutput = tempUTXOs.get(input.transactionOutputId);

					if(tempOutput == null) {
						System.out.println("#Referenced input on Transaction(" + t + ") is Missing");
						return false;
					}

					if(input.UTXO.value != tempOutput.value) {
						System.out.println("#Referenced input Transaction(" + t + ") value is Invalid");
						return false;
					}

					tempUTXOs.remove(input.transactionOutputId);
				}

				for(TransactionOutput output: currentTransaction.outputs) {
					tempUTXOs.put(output.id, output);
				}

				if( currentTransaction.outputs.get(0).reciepient != currentTransaction.recipient) {
					System.out.println("#Transaction(" + t + ") output reciepient is not who it should be");
					return false;
				}
				if( currentTransaction.outputs.get(1).reciepient != currentTransaction.sender) {
					System.out.println("#Transaction(" + t + ") output 'change' is not sender.");
					return false;
				}
			}
		}

		System.out.println("Blockchain is valid");

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

	/**
	 * Adds block into the ledger
	 *
	 * @param newBlock block to be added into the network
	 */
	public static void addBlock(Block newBlock) {
		newBlock.mineBlock(difficulty);
		chain.add(newBlock);
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