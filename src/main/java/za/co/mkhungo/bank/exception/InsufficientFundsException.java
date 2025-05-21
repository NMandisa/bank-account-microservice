package za.co.mkhungo.bank.exception;

import java.io.Serializable;

/**
 * @author Noxolo.Mkhungo
 */
public class InsufficientFundsException extends RuntimeException implements Serializable {
    public InsufficientFundsException(String message) {
        super( message);
    }
    public InsufficientFundsException() {super();}
    public InsufficientFundsException(String message, Throwable cause) {super(message,cause);}
}
