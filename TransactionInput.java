
/**
 * @author artva
 *
 * Transaction input class
 * This class will be used to reference TransactionOutputs that have not yet been spent
 */
public class TransactionInput {

	/**
	 * Reference to TransactionOutputs -> transactionId; allowing miners to check your ownership
	 */
	public String transactionOutputId;
	/**
	 * Contains the Unspent transaction output
	 */
	public TransactionOutput UTXO;

	/**
	 * Constructor for transaction input
	 * @param transactionOutputId Transaction Output Id
	 */
	public TransactionInput(String transactionOutputId) {
		this.transactionOutputId = transactionOutputId;
	}

}
