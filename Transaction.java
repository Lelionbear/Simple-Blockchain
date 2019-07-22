import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.ArrayList;

/**
 * @author artva
 *
 * Class that manages transaction processes
 */
public class Transaction {

	/**
	 * The hash of the transaction
	 */
	public String transactionId;
	/**
	 * Sender's public address key
	 */
	public PublicKey sender;
	/**
	 * Recipient's public address key
	 */
	public PublicKey recipient;
	/**
	 * Value of transaction being exchanged
	 */
	public float value;
	/**
	 * Used to authenticate transaction and preventing others from using funds in user's wallet
	 */
	public byte[] signature;


	/**
	 * ArrayList of type Transaction Inputs
	 */
	public ArrayList<TransactionInput> inputs = new ArrayList<TransactionInput>();
	/**
	 * ArrayList of type Transaction Outputs
	 */
	public ArrayList<TransactionOutput> outputs = new ArrayList<TransactionOutput>();


	/**
	 * Estimated count of number of transactions generated
	 */
	private static int sequence = 0;


	/**
	 * Constructor to form transactions between wallets
	 *
	 * @param from publicKey from the sender
	 * @param to publicKey from the recipient
	 * @param value the amount sent from one wallet to another
	 * @param inputs ArrayList of Transaction Inputs
	 */
	public Transaction(PublicKey from, PublicKey to, float value, ArrayList<TransactionInput> inputs) {
		this.sender = from;
		this.recipient = to;
		this.value = value;
		this.inputs = inputs;
	}

	/**
	 * Returns true if new transaction could be created.
	 *
	 * @return true if new transaction can be created
	 */
	public boolean processTransaction() {

		if(verifiySignature() == false) {
			System.out.println("#Transaction Signature failed to verify");
			return false;
		}

		//gather transaction inputs (Make sure they are unspent):
		for(TransactionInput input : inputs) {
			input.UTXO = Blockchain.UTXOs.get(input.transactionOutputId);
		}

		//check if transaction is valid:
		if(getInputsValue() < Blockchain.minimumTransaction) {
			System.out.println("#Transaction Inputs to small: " + getInputsValue());
			return false;
		}

		//generate transaction outputs:
		float leftOver = getInputsValue() - value;
		transactionId = calulateHash();
		//send value to recipient
		outputs.add(new TransactionOutput( this.recipient, value,transactionId));
		//send the left over 'change' back to sender
		outputs.add(new TransactionOutput( this.sender, leftOver,transactionId));

		//add outputs to Unspent list
		for(TransactionOutput output : outputs) {
			Blockchain.UTXOs.put(output.id , output);
		}

		//remove transaction inputs from UTXO lists as spent:
		for(TransactionInput input : inputs) {
			if(input.UTXO == null) {
				continue; //if Transaction can't be found skip it
			}
			Blockchain.UTXOs.remove(input.UTXO.id);
		}

		return true;
	}

	/**
	 * Returns sum of inputs(UTXOs) values
	 *
	 * @return returns sum of inputs(UTXOs) values
	 */
	public float getInputsValue() {
		float total = 0;
		for(TransactionInput input : inputs) {
			if(input.UTXO == null) {
				continue; //if Transaction can't be found skip it
			}
			total += input.UTXO.value;
		}
		return total;
	}

	/**
	 * Returns sum of outputs:
	 *
	 * @return returns sum of outputs:
	 */
	public float getOutputsValue() {
		float total = 0;
		for(TransactionOutput output : outputs) {
			total += output.value;
		}
		return total;
	}


	/**
	 * Signs all the data that should not be tampered with
	 *
	 * @param privateKey private key from sender
	 */
	public void generateSignature(PrivateKey privateKey) {
		String data =
				CryptoUtil.getStringFromKey(sender) +
				CryptoUtil.getStringFromKey(recipient) +
				Float.toString(value);
		signature = CryptoUtil.applyECDSASig(privateKey,data);
	}

	/**
	 * Verifies the data signed hasn't been tampered with
	 *
	 * @return true if transaction is valid
	 */
	public boolean verifiySignature() {
		String data =
				CryptoUtil.getStringFromKey(sender) +
				CryptoUtil.getStringFromKey(recipient) +
				Float.toString(value);
		return CryptoUtil.verifyECDSASig(sender, data, signature);
	}

	/**
	 * Generates a transaction hash to be used as its Id
	 *
	 * @return generated transactional hash as Id
	 */
	private  String calulateHash() {
		sequence++;
		return CryptoUtil.sha256(
				CryptoUtil.getStringFromKey(sender) +
				CryptoUtil.getStringFromKey(recipient) +
				Float.toString(value) + sequence
				);
	}
}
