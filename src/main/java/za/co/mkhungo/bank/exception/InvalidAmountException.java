package za.co.mkhungo.bank.exception;

import java.io.Serializable;

/**
 * @author Noxolo.Mkhungo
 */
public class InvalidAmountException extends RuntimeException implements Serializable {
    public InvalidAmountException(String message) {
        super( message);
    }
    public InvalidAmountException() {super();}
    public InvalidAmountException(String message, Throwable cause) {super(message,cause);}
}
