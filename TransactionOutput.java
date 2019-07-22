import java.security.PublicKey;

/**
 * @author artva
 *
 * Transaction output class
 */
public class TransactionOutput {

	/**
	 * Id of the transaction
	 */
	public String id;
	/**
	 * Owner of these coins
	 */
	public PublicKey reciepient;
	/**
	 * The amount of coins they own
	 */
	public float value;
	/**
	 * The Id of transaction this output was created in
	 */
	public String parentTransactionId;

	/**
	 * Constructor for transaction output
	 *
	 * @param reciepient
	 * @param value
	 * @param parentTransactionId
	 */
	public TransactionOutput(PublicKey reciepient, float value, String parentTransactionId) {
		this.reciepient = reciepient;
		this.value = value;
		this.parentTransactionId = parentTransactionId;
		this.id = CryptoUtil.sha256(
				CryptoUtil.getStringFromKey(reciepient) +
				Float.toString(value) +
				parentTransactionId);
	}

	/**
	 * Method that checks if coin belongs to owner
	 *
	 * @param publicKey
	 * @return true if coins belong to owner
	 */
	public boolean isMine(PublicKey publicKey) {
		return (publicKey == reciepient);
	}

}
