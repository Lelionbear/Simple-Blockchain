import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.spec.ECGenParameterSpec;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * @author artva
 *
 * Wallet used to make and allow transactions within the blockchain network
 */
public class Wallet {

	/**
	 * Variable containing the private address for the wallet
	 */
	public PrivateKey privateKey;
	/**
	 * Variable containing the public address for the wallet
	 */
	public PublicKey publicKey;
	/**
	 * My UTXOs owned by this wallet
	 */
	public Map<String,TransactionOutput> UTXOs;

	private String owner;


	/**
	 * Constructor that initiates the key pairs of the wallet
	 */
	public Wallet() {
		generateKeyPair();
		UTXOs = new HashMap<String,TransactionOutput>();
	}

	/**
	 * Constructor that initiates the key pairs of the wallet with an owner
	 * @param owner owner of this wallet
	 */
	public Wallet(String owner) {
		this.owner = owner;
		generateKeyPair();
		UTXOs = new HashMap<String,TransactionOutput>();
	}

	/**
	 * Returns balance and stores the UTXO's owned by this wallet in this.UTXOs
	 *
	 * @return returns balance and stores the UTXO's owned by this wallet in this.UTXOs
	 */
	public float getBalance() {
		float total = 0;
		for (Map.Entry<String, TransactionOutput> item: Blockchain.UTXOs.entrySet()){
			TransactionOutput UTXO = item.getValue();
			if(UTXO.isMine(publicKey)) {
				//if output belongs to me ( if coins belong to me )
				UTXOs.put(UTXO.id,UTXO); //add it to our list of unspent transactions.
				total += UTXO.value ;
			}
		}

		return total;
	}


	/**
	 * Generates and returns a new transaction from this wallet.
	 *
	 * @param _recipient
	 * @param value
	 * @return returns a new transaction from this wallet
	 */
	public Transaction sendFunds(PublicKey _recipient,float value ) {

		if(getBalance() < value) { //gather balance and check funds.
			System.out.println("#Not Enough funds to send transaction. Transaction Discarded.");
			return null;
		}

		//create array list of inputs
		ArrayList<TransactionInput> inputs = new ArrayList<TransactionInput>();

		float total = 0;
		for (Map.Entry<String, TransactionOutput> item: UTXOs.entrySet()){
			TransactionOutput UTXO = item.getValue();
			total += UTXO.value;
			inputs.add(new TransactionInput(UTXO.id));
			if(total > value) {
				break;
			}
		}

		Transaction newTransaction = new Transaction(publicKey, _recipient , value, inputs);
		newTransaction.generateSignature(privateKey);

		for(TransactionInput input: inputs){
			UTXOs.remove(input.transactionOutputId);
		}
		return newTransaction;
	}

	/**
	 * Generates private and public keys for the wallet
	 */
	private void generateKeyPair() {
		try {

			// The KeyPairGenerator class is used to generate pairs ofpublic and private keys.
			// creates a public/private key pair that can be used with this algorithm.
//			KeyPairGenerator keyGen = KeyPairGenerator.getInstance("ECDSA","BC");
			KeyPairGenerator keyGen = KeyPairGenerator.getInstance("EC", "SunEC");

			// Object that provides a cryptographically strong random number generator
			// This algorithm uses SHA-1 as the foundation of the PRNG
			SecureRandom random = SecureRandom.getInstance("SHA1PRNG");

			// Creates a parameter specification for EC parameter generation using a standard (or predefined)
			// name stdName in order to generate the corresponding(precomputed) elliptic curve domain parameters.
			// For the list of supported names, please consult the documentation of the provider whose
			// implementation will be used.
			ECGenParameterSpec ecSpec = new ECGenParameterSpec("secp256k1");

			keyGen.initialize(ecSpec, random);

			KeyPair keyPair = keyGen.generateKeyPair();

			privateKey = keyPair.getPrivate();

			publicKey = keyPair.getPublic();

		} catch(Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public String toString() {
		return owner;
	}
}
