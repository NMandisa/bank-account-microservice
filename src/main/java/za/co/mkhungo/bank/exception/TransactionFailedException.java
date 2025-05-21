package za.co.mkhungo.bank.exception;

import java.io.Serializable;

/**
 * @author Noxolo.Mkhungo
 */
public class TransactionFailedException extends RuntimeException implements Serializable {
    public TransactionFailedException(String message) {
        super( message);
    }
    public TransactionFailedException() {super();}
    public TransactionFailedException(String message, Throwable cause) {super(message,cause);}
}
