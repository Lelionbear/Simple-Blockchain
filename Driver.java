import java.util.ArrayList;
import java.util.Random;

/**
 * @author lelionbear
 *
 * Simple class demonstrating basic back-end of BlockChain technology
 */
public class Driver {

	/**
	 * alpha wallet
	 */
	public static Wallet alpha;
	/**
	 * bravo wallet
	 */
	public static Wallet bravo;
	/**
	 * charlie's wallet
	 */
	public static Wallet charlie;
	/**
	 * delta wallet
	 */
	public static Wallet delta;
	/**
	 * echo's wallet
	 */
	public static Wallet echo;
	/**
	 * coinbase wallet
	 */
	public static Wallet coinBase;
	/**
	 * Genesis transaction
	 */
	public static ArrayList<Wallet> wallets;

	/**
	 * blockchain object
	 */
	public static Blockchain bc;

	/**
	 * Driver that initiates basic use of blockchain
	 *
	 * @param args not used
	 */
	public static void main(String[] args) {

		System.out.println("Initiating Simple Blockchain");
		bc = new Blockchain();
		setup();
		Blockchain.validateBlockchain();
		Block block;
		int randWalletSender, randWalletReciever;
		float randCoins;

		System.out.println(wallets.toString());
		System.out.println(wallets.size());

		walletStats();

		for(int i = 0; i < 50; i++) {
			do {
				randWalletSender = randomWallet();
				randWalletReciever = randomWallet();
				randCoins = randomCoins();
			}while(randWalletSender == randWalletReciever);

			System.out.println("Sender:\t " + wallets.get(randWalletSender));
			System.out.println("Reciever:\t " + wallets.get(randWalletReciever));
			System.out.println("Transaction amount: " + randCoins);

			block = new Block(Blockchain.getLatestHash());
			block.addTransaction(
					wallets.get(randWalletSender).sendFunds(
							wallets.get(randWalletReciever).publicKey,
							randCoins));
			Blockchain.addBlock(block);

			walletStats();
			Blockchain.validateBlockchain();
		}

		System.out.println();
		Blockchain.validateBlockchain();

	}

	private static float randomCoins() {
		return new Random().nextInt(100);
	}

	private static int randomWallet() {
		return new Random().nextInt(100) % wallets.size();
	}

	private static void walletStats() {
		System.out.println("alpha balance is: " + alpha.getBalance());
		System.out.println("bravo balance is: " + bravo.getBalance());
		System.out.println("charlie balance is: " + charlie.getBalance());
		System.out.println("delta balance is: " + delta.getBalance());
		System.out.println("echo balance is: " + echo.getBalance());
	}

	/**
	 * Setup for the initial simulation of the blockchain
	 */
	private static void setup() {
		wallets = new ArrayList<Wallet>();
		wallets.add((alpha = new Wallet("alpha")));
		wallets.add((bravo = new Wallet("bravo")));
		wallets.add((charlie = new Wallet("charlie")));
		wallets.add((delta = new Wallet("delta")));
		wallets.add((echo = new Wallet("echo")));
		coinBase = new Wallet();


		Blockchain.genesisTransaction = new Transaction(coinBase.publicKey, alpha.publicKey, 1000f, null);
		Blockchain.genesisTransaction.generateSignature(coinBase.privateKey);	 // manually sign the genesis transaction
		Blockchain.genesisTransaction.transactionId = "0"; // manually set the transaction id
		Blockchain.genesisTransaction.outputs.add(new TransactionOutput(
				Blockchain.genesisTransaction.recipient,
				Blockchain.genesisTransaction.value,
				Blockchain.genesisTransaction.transactionId)); // manually add the Transactions Output
		// its important to store our first transaction in the UTXOs list.
		Blockchain.UTXOs.put(
				Blockchain.genesisTransaction.outputs.get(0).id,
				Blockchain.genesisTransaction.outputs.get(0));

		System.out.println("Creating and Mining Genesis block... ");
		Block genesis = new Block("0");
		genesis.addTransaction(Blockchain.genesisTransaction);
		Blockchain.addBlock(genesis);

		Block block1 = new Block(Blockchain.getLatestHash());
		block1.addTransaction(alpha.sendFunds(bravo.publicKey, 200f));
		Blockchain.addBlock(block1);

		Block block2 = new Block(Blockchain.getLatestHash());
		block2.addTransaction(alpha.sendFunds(charlie.publicKey, 200f));
		Blockchain.addBlock(block2);

		Block block3 = new Block(Blockchain.getLatestHash());
		block3.addTransaction(alpha.sendFunds( delta.publicKey, 200));
		Blockchain.addBlock(block3);

		Block block4 = new Block(Blockchain.getLatestHash());
		block4.addTransaction(alpha.sendFunds( echo.publicKey, 200));
		Blockchain.addBlock(block4);
	}

//	System.out.println("Wallets created");
//	coinBase = new Wallet();
//	walletA = new Wallet();
//	walletB = new Wallet();
//
//	// create genesis transaction, which sends 100 eCoin to walletA:
//	genesisTransaction = new Transaction(coinBase.publicKey, walletA.publicKey, 100f, null);
//	genesisTransaction.generateSignature(coinBase.privateKey);	 //manually sign the genesis transaction
//	genesisTransaction.transactionId = "0"; //manually set the transaction id
//	genesisTransaction.outputs.add(new TransactionOutput(
//			genesisTransaction.recipient,
//			genesisTransaction.value,
//			genesisTransaction.transactionId)); //manually add the Transactions Output
//	Blockchain.UTXOs.put(genesisTransaction.outputs.get(0).id, genesisTransaction.outputs.get(0)); //its important to store our first transaction in the UTXOs list.
//
//	System.out.println("Creating and Mining Genesis block... ");
//	Block genesis = new Block("0");
//	genesis.addTransaction(genesisTransaction);
//	addBlock(genesis);

//
//	bc.add(Map.of("Genesis Block", 0), "0");
//	System.out.println("\nNew Block Created");
//
//	System.out.println("Mining latest block");
//	Blockchain.mineLatestBlock();

//	for(int i = 1; i < 10; i++) {
//		bc.add(Map.of("Content", 10 - i), Blockchain.getLatestHash());
//		System.out.println("\nNew Block Created");
//
//		System.out.println("Mining latest block");
//		Blockchain.mineLatestBlock();
//	}

	// When uncommented out, blockchain signals that this block has not been mined
//	bc.add(Map.of("Content", 10), Blockchain.getLatestHash());
//	System.out.println("\nNew Block Created");

//	System.out.println("\nIs Blockchain valid: " + Blockchain.validateBlockchain());
//
//	System.out.println("\nBlockchain content:");
//	System.out.println(bc.toString());

//	try {
//		Provider[] p = Security.getProviders();
//		System.out.println(Arrays.toString(p));
//		KeyPairGenerator keyGen = KeyPairGenerator.getInstance("EC", "SunEC");
//		SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
//		ECGenParameterSpec ecsp= new ECGenParameterSpec("secp256k1"); // the Bitcoin curve
//		keyGen.initialize(ecsp, random);
//		KeyPair keyPair = keyGen.generateKeyPair();
//		System.out.println(keyPair.getPrivate());
//		System.out.println(keyPair.getPublic());
//		System.out.println("Wallet A");
//		System.out.println("Private and Public Keys");
//		System.out.println(CryptoUtil.getStringFromKey(walletA.privateKey));
//		System.out.println(CryptoUtil.getStringFromKey(walletA.publicKey));
//		Transaction transaction = new Transaction(walletA.publicKey, walletB.publicKey, 5, null);
//		transaction.generateSignature(walletA.privateKey);
//		System.out.println("Is signature verified:" + transaction.verifiySignature());
//	} catch (Exception e) {
//		e.printStackTrace();
//	}

}
